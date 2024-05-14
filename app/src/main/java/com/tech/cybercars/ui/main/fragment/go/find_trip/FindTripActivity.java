package com.tech.cybercars.ui.main.fragment.go.find_trip;

import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineDasharray;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.tech.cybercars.R;
import com.tech.cybercars.constant.PickLocation;
import com.tech.cybercars.constant.ThemeMode;
import com.tech.cybercars.databinding.ActivityFindTripBinding;
import com.tech.cybercars.services.location.LocationService;
import com.tech.cybercars.services.mapbox.MapboxMapService;
import com.tech.cybercars.services.mapbox.MapboxNavigationService;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.component.dialog.NotificationDialog;
import com.tech.cybercars.utils.DateTimePicker;
import com.tech.cybercars.utils.DateUtil;
import com.tech.cybercars.utils.KeyBoardUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class FindTripActivity extends BaseActivity<ActivityFindTripBinding, FindTripViewModel> {
    private BottomSheetBehavior<LinearLayout> bottom_sheet_behavior;
    private final String MARKER_SOURCE_ID = "MARKER_SOURCE_ID";
    private final String PASSENGER_ROUTE_SOURCE_LAYER_ID = "PASSENGER_ROUTE_SOURCE_LAYER_ID";
    private int pick_location;
    private MapboxMapService mapbox_service;
    private final Handler search_debounce_handler = new Handler(Looper.getMainLooper());
    private Runnable search_debounce_runnable;
    private TripFindingMapController find_trip_controller;
    private final String START_MARKER_ICON_IMAGE_ID = "START_MARKER_ICON_IMAGE_ID";
    private final String DESTINATION_MARKER_ICON_IMAGE_ID = "DESTINATION_MARKER_ICON_IMAGE_ID";
    private final String DESTINATION_MARKER_LAYER_ID = "DESTINATION_MARKER_LAYER_ID";
    private final String ICON_IMAGE = "ICON_IMAGE";
    private DateTimePicker date_picker_dialog;
    private int min_height_bottom_sheet;

    @NonNull
    @Override
    protected FindTripViewModel InitViewModel() {
        return new ViewModelProvider(this).get(FindTripViewModel.class);
    }

    @Override
    protected ActivityFindTripBinding InitBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_find_trip);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        min_height_bottom_sheet = (int) getResources().getDimension(com.intuit.sdp.R.dimen._190sdp);
    }

    @Override
    protected void InitView() {
        InitMapBox();
        InitSearchAddressResultView();
        InitBottomSheet();
        InitInputLocation();
        InitDatePickerDialog();

        binding.inputStartDate.getEditText().setOnClickListener(view -> {
            date_picker_dialog.Run();
        });

        binding.btnFocusCurrentLocationShareTrip.setOnClickListener(view -> {
            StartTracking();
        });

        binding.btnPickLocationShareTrip.setOnClickListener(view -> {
            FindAddress();
        });

        binding.btnCancelPickAction.setOnClickListener(view -> {
            DonePickLocationAction();
        });

        binding.btnOutShareTransport.setOnClickListener(view -> {
            OnBackPress();
        });
    }

    private void InitDatePickerDialog() {
        date_picker_dialog = new DateTimePicker(getSupportFragmentManager(), DateTimePicker.M_D_Y);
        date_picker_dialog.SetOnDateTimePicked((calendar, date_time_format) -> {
            view_model.trip_start_date.setValue(date_time_format);
            view_model.trip_start_date_data = DateUtil.YMDFormat(calendar);
            if (view_model.origin_reverse != null && view_model.destination_reverse != null) {
                view_model.HandleFindTrip();
            }
        });
        String current_date = DateUtil.GetCurrentDate();
        view_model.trip_start_date.setValue(current_date);

        Calendar calendar = Calendar.getInstance();
        view_model.trip_start_date_data = DateUtil.YMDFormat(calendar);
    }


    @Override
    protected void InitObserve() {
        view_model.is_success_reverse_geocoding.observe(this, is_success -> {
            if (is_success != null && is_success) {
                double lat;
                double lng;
                if (pick_location == PickLocation.PICK_START_POINT) {
                    lat = view_model.origin_reverse.lat;
                    lng = view_model.origin_reverse.lng;
                } else {
                    lat = view_model.destination_reverse.lat;
                    lng = view_model.destination_reverse.lng;
                }
                MarkLocationOnMap(Point.fromLngLat(lng, lat));
                DonePickLocationAction();
                GetNavigation();

            } else {
                ShowErrorPickLocation();
            }
        });

        view_model.getErrorCallServerLive().observe(this, error_call_server -> {
            if (error_call_server != null && !error_call_server.equals("")) {
                ShowErrorCallServer(error_call_server);
            }
        });

        view_model.error_get_route.observe(this, this::ShowErrorGetRoute);

        view_model.current_route.observe(this, current_route -> {
            if (current_route != null) {
                DrawRoute(current_route.geometry());
            }
        });


        view_model.search_address_result.observe(this, search_address_result -> {
            if (search_address_result != null) {
                if (search_address_result.size() == 0) {
                    binding.viewSearchAddressResultViewShareTrip.GetSearchAddressResultList().clear();
                    binding.viewSearchAddressResultViewShareTrip.setVisibility(View.GONE);
                } else {
                    binding.viewSearchAddressResultViewShareTrip.setVisibility(View.VISIBLE);
                    binding.viewSearchAddressResultViewShareTrip.GetSearchAddressResultList().clear();
                    binding.viewSearchAddressResultViewShareTrip.GetSearchAddressResultList().addAll(search_address_result);
                    binding.viewSearchAddressResultViewShareTrip.NotifyDataSetChanged();
                }
            } else {
                binding.viewSearchAddressResultViewShareTrip.GetSearchAddressResultList().clear();
                binding.viewSearchAddressResultViewShareTrip.setVisibility(View.GONE);
            }

            binding.setIsShowSearchAddressLoading(false);
            binding.skeletonLoading.stopShimmerAnimation();
        });

        view_model.trip_found_list.observe(this, trip_found_list -> {
            binding.bsLocationPicker.setVisibility(View.GONE);
            find_trip_controller.UpdateTripFoundAdapter(trip_found_list);
            binding.setIsShowRcvTripFound(!trip_found_list.isEmpty());
            binding.setIsShowVehicleFilter(!trip_found_list.isEmpty());
            binding.setIsShowThumbNotFound(trip_found_list.isEmpty());
            binding.setIsShowOverlay(false);
        });
    }

    @Override
    protected void InitCommon() {
        binding.setIsShowBtnChooseOnMap(false);
        binding.setIsGettingLocation(false);
        binding.setIsActivatedPlacePicker(false);
        binding.setIsExpandBottomSheet(false);
        binding.setIsShowRcvTripFound(false);
        binding.setIsShowThumbNotFound(false);
    }

    @Override
    protected void OnBackPress() {
        if (binding.getIsShowRcvTripFound() || binding.getIsShowThumbNotFound()) {
            CancelFindTrip();
            return;
        }

        if (binding.getIsActivatedPlacePicker()) {
            DonePickLocationAction();
            return;
        }

        if (binding.getIsExpandBottomSheet()) {
            CollapsedBottomSheet();
            return;
        }
        finish();
    }

    private void CancelFindTrip() {
        find_trip_controller.UpdateTripFoundAdapter(new ArrayList<>());
        binding.setIsShowRcvTripFound(false);
        binding.setIsShowVehicleFilter(false);
        binding.setIsShowThumbNotFound(false);
        ShowBottomSheet();
    }

    private void InitSearchAddressResultView() {
        binding.viewSearchAddressResultViewShareTrip.setVisibility(View.GONE);
        binding.viewSearchAddressResultViewShareTrip.SetOnItemClick(reverse_geocoding -> {
            binding.setIsShowOverlay(true);
            Point point = Point.fromLngLat(reverse_geocoding.lng, reverse_geocoding.lat);
            MarkLocationOnMap(point);
            if (pick_location == PickLocation.PICK_START_POINT) {
                view_model.origin_reverse = reverse_geocoding;
                view_model.origin_address.setValue(reverse_geocoding.display_name);
            } else {
                view_model.destination_reverse = reverse_geocoding;
                view_model.destination_address.setValue(reverse_geocoding.display_name);
            }
            CollapsedBottomSheet();
            mapbox_service.ChangeCameraToLocationWithAnimation(
                    new LatLng(point.latitude(), point.longitude()),
                    4000,
                    0,
                    new MapboxMap.CancelableCallback() {
                        @Override
                        public void onCancel() {

                        }

                        @Override
                        public void onFinish() {
                            GetNavigation();
                        }
                    }
            );
        });

        binding.viewSearchAddressResultViewShareTrip.SetOnScroll((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (oldScrollY != 0) {
                KeyBoardUtil.HideKeyBoard(this);
            }
        });

    }

    private void DrawRoute(String geometry) {
        mapbox_service.GetMapBoxMap().getStyle(style -> {
            GeoJsonSource route_source = style.getSourceAs(PASSENGER_ROUTE_SOURCE_LAYER_ID);
            assert route_source != null;
            route_source.setGeoJson(LineString.fromPolyline(geometry, PRECISION_6));
            if(geometry.length() > MapboxNavigationService.GEOMETRY_LIMIT){
                MapboxNavigationService.ShowRouteOutOfLimit(this);
            } else {
                view_model.HandleFindTrip();
            }
            MakeBoundingBox();
        });

    }



    private void MakeBoundingBox() {
        binding.setIsShowOverlay(true);
        LatLng northeast = new LatLng(view_model.origin_reverse.lat, view_model.origin_reverse.lng);
        LatLng southwest = new LatLng(view_model.destination_reverse.lat, view_model.destination_reverse.lng);
        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                .include(northeast)
                .include(southwest)
                .build();

        mapbox_service.GetMapBoxMap().easeCamera(CameraUpdateFactory.newLatLngBounds(
                        latLngBounds, 50),
                3000, new MapboxMap.CancelableCallback() {
                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onFinish() {
                        binding.setIsShowOverlay(false);
                    }
                });
    }

    private void ShowErrorGetRoute(String error_get_route) {
        binding.setIsShowOverlay(false);
        NotificationDialog.Builder(this)
                .SetIcon(R.drawable.ic_warning)
                .SetTitle(getResources().getString(R.string.route_not_found))
                .SetSubtitle(error_get_route)
                .SetTextMainButton(getResources().getString(R.string.close))
                .SetOnMainButtonClicked(Dialog::dismiss).show();
    }

    private void ShowErrorCallServer(String error_call_server) {
        NotificationDialog.Builder(this)
                .SetIcon(R.drawable.ic_error)
                .SetTitle(getResources().getString(R.string.something_went_wrong))
                .SetSubtitle(error_call_server)
                .SetTextMainButton(getResources().getString(R.string.close))
                .SetOnMainButtonClicked(Dialog::dismiss).show();
    }

    private void DonePickLocationAction() {
        binding.setIsActivatedPlacePicker(false);
        bottom_sheet_behavior.setPeekHeight(Math.round(min_height_bottom_sheet));
        bottom_sheet_behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        binding.inputFromMap.setEndIconVisible(false);
        binding.inputToMap.setEndIconVisible(false);
    }

    private void ShowBottomSheet() {
        binding.bsLocationPicker.setVisibility(View.VISIBLE);
        bottom_sheet_behavior.setPeekHeight(Math.round(min_height_bottom_sheet));
        bottom_sheet_behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void FindAddress() {
        final LatLng target_select = mapbox_service.GetMapBoxMap().getCameraPosition().target;
        view_model.HandleFindAddress(target_select, pick_location);
    }

    private void MarkLocationOnMap(Point point) {
        List<Feature> features = new ArrayList<>();
        mapbox_service.GetMapBoxMap().getStyle(style -> {
            GeoJsonSource source = style.getSourceAs(MARKER_SOURCE_ID);
            Feature new_feature = Feature.fromGeometry(point);
            if (pick_location == PickLocation.PICK_START_POINT) {
                new_feature.addStringProperty(ICON_IMAGE, START_MARKER_ICON_IMAGE_ID);
                if (view_model.destination_reverse != null) {
                    Feature old_feature = Feature.fromGeometry(Point.fromLngLat(
                            view_model.destination_reverse.lng,
                            view_model.destination_reverse.lat
                    ));
                    old_feature.addStringProperty(ICON_IMAGE, DESTINATION_MARKER_ICON_IMAGE_ID);
                    features.add(old_feature);
                }
            } else {
                new_feature.addStringProperty(ICON_IMAGE, DESTINATION_MARKER_ICON_IMAGE_ID);
                if (view_model.origin_reverse != null) {
                    Feature old_feature = Feature.fromGeometry(Point.fromLngLat(
                            view_model.origin_reverse.lng,
                            view_model.origin_reverse.lat
                    ));
                    old_feature.addStringProperty(ICON_IMAGE, START_MARKER_ICON_IMAGE_ID);
                    features.add(old_feature);
                }
            }
            features.add(new_feature);
            assert source != null;
            source.setGeoJson(FeatureCollection.fromFeatures(features));
        });
    }

    private void GetNavigation() {
        if (view_model.origin_reverse != null && view_model.destination_reverse != null) {
            view_model.HandleGetRoute(DirectionsCriteria.PROFILE_DRIVING);
        } else {
            binding.setIsShowOverlay(false);
        }
    }

    private void ShowErrorPickLocation() {
        NotificationDialog.Builder(this)
                .SetIcon(R.drawable.ic_warning)
                .SetTitle(getResources().getString(R.string.address_not_found))
                .SetSubtitle(getResources().getString(R.string.we_couldnt_find_an_address_that_matches_your_selection))
                .SetTextMainButton(getResources().getString(R.string.try_again))
                .SetOnMainButtonClicked(Dialog::dismiss).show();
    }

    private void InitInputLocation() {
        binding.inputFromMap.getEditText().addTextChangedListener(location_text_watcher);
        binding.inputFromMap.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                KeyBoardUtil.HideKeyBoard(this);
                return true;
            }
            return false;
        });
        binding.inputFromMap.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                pick_location = PickLocation.PICK_START_POINT;
                ExpandBottomSheet(getResources().getString(R.string.select_the_starting_point_on_the_map));
            } else {
                binding.inputFromMap.setEndIconVisible(false);
            }
        });


        binding.inputToMap.getEditText().addTextChangedListener(location_text_watcher);
        binding.inputToMap.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                KeyBoardUtil.HideKeyBoard(this);
                return true;
            }
            return false;
        });
        binding.inputToMap.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                pick_location = PickLocation.PICK_DESTINATION;
                ExpandBottomSheet(getResources().getString(R.string.select_destination_on_the_map));
            } else {
                binding.inputToMap.setEndIconVisible(false);
            }
        });
    }

    private final TextWatcher location_text_watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            binding.setIsShowSearchAddressLoading(true);
            binding.skeletonLoading.startShimmerAnimation();
            if (!s.toString().isEmpty() && bottom_sheet_behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                search_debounce_handler.removeCallbacks(search_debounce_runnable);
                search_debounce_runnable = () -> {
                    view_model.HandleSearchAddress(s.toString());
                };
                search_debounce_handler.postDelayed(search_debounce_runnable, 1000);
            } else {
                binding.setIsShowSearchAddressLoading(false);
                binding.skeletonLoading.stopShimmerAnimation();
                binding.viewSearchAddressResultViewShareTrip.GetSearchAddressResultList().clear();
                binding.viewSearchAddressResultViewShareTrip.setVisibility(View.GONE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void ExpandBottomSheet(String txt_choose_location) {
        bottom_sheet_behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        binding.viewSearchAddressResultViewShareTrip.setVisibility(View.VISIBLE);
        binding.btnCloseBottomSheet.setVisibility(View.VISIBLE);
        binding.txtChooseLocationOnMap.setText(txt_choose_location);
        binding.setIsExpandBottomSheet(true);
        binding.setIsShowBtnChooseOnMap(true);
    }

    private void InitBottomSheet() {
        this.bottom_sheet_behavior = BottomSheetBehavior.from(binding.bsLocationPicker);
        bottom_sheet_behavior.setPeekHeight(min_height_bottom_sheet);
        bottom_sheet_behavior.setDraggable(false);
        binding.btnCloseBottomSheet.setVisibility(View.GONE);
        binding.btnCloseBottomSheet.setOnClickListener(view -> {
            CollapsedBottomSheet();
        });

        binding.btnChooseLocationOnMap.setOnClickListener(view -> {
            bottom_sheet_behavior.setPeekHeight(0, true);
            binding.setIsActivatedPlacePicker(true);
            if (binding.inputFromMap.getEditText().isFocused()) {
                pick_location = PickLocation.PICK_START_POINT;
                binding.iconLocationPicker.setImageResource(R.drawable.ic_placeholder_red);
            } else {
                pick_location = PickLocation.PICK_DESTINATION;
                binding.iconLocationPicker.setImageResource(R.drawable.ic_placeholder_green);
            }
            CollapsedBottomSheet();
        });
    }

    private void CollapsedBottomSheet() {
        KeyBoardUtil.HideKeyBoard(this);
        binding.viewSearchAddressResultViewShareTrip.setVisibility(View.GONE);
        bottom_sheet_behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        binding.inputToMap.getEditText().clearFocus();
        binding.inputFromMap.getEditText().clearFocus();
        binding.btnCloseBottomSheet.setVisibility(View.GONE);
        binding.setIsExpandBottomSheet(false);
        binding.setIsShowBtnChooseOnMap(false);
    }

    private void InitMapBox() {
        String map_style = "";
        if (getResources().getString(R.string.theme_mode).equals(ThemeMode.DARK)) {
            map_style = Style.DARK;
        } else {
            map_style = Style.LIGHT;
        }

        binding.mapShareTransport.onCreate(savedInstanceState);
        mapbox_service = new MapboxMapService(
                this,
                binding.mapShareTransport,
                map_style,
                style -> {
                    mapbox_service.GetMapBoxMap().getUiSettings().setCompassEnabled(false);
                    mapbox_service.GetMapBoxMap().getUiSettings().setLogoEnabled(false);
                    mapbox_service.GetMapBoxMap().getUiSettings().setAttributionEnabled(false);

                    StartMapBoxService();
                    mapbox_service.SetOnCameraStartListener((reason) -> {
                        if (reason == MapboxMap.OnCameraMoveStartedListener.REASON_API_GESTURE) {
                            binding.btnFocusCurrentLocationShareTrip.setImageResource(R.drawable.ic_not_focus_location);
                            bottom_sheet_behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                    });

                    InitLayerAndSource(style);
                    find_trip_controller = new TripFindingMapController(
                            this,
                            view_model,
                            mapbox_service,
                            binding
                    );
                    mapbox_service.GetMapBoxMap().addOnMapClickListener(point -> find_trip_controller.onMapClick(point));
                });
    }

    private void InitLayerAndSource(Style style) {
        //marker image
        Bitmap start_icon_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_placeholder_red_bitmap);
        style.addImage(START_MARKER_ICON_IMAGE_ID, start_icon_bitmap);

        Bitmap destination_icon_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_placeholder_green_bitmap);
        style.addImage(DESTINATION_MARKER_ICON_IMAGE_ID, destination_icon_bitmap);

        // marker layer
        style.addSource(new GeoJsonSource(MARKER_SOURCE_ID));
        style.addLayer(
                new SymbolLayer(DESTINATION_MARKER_LAYER_ID, MARKER_SOURCE_ID)
                        .withProperties(
                                iconImage(Expression.get(ICON_IMAGE)),
                                iconOffset(new Float[]{0f, -65f}),
                                iconIgnorePlacement(true),
                                iconAllowOverlap(true),
                                iconSize(.3f)
                        ));

        // route layer
        style.addSource(new GeoJsonSource(PASSENGER_ROUTE_SOURCE_LAYER_ID));
        LineLayer route_layer = new LineLayer("route-layer-id", PASSENGER_ROUTE_SOURCE_LAYER_ID);
        route_layer.setProperties(
                lineDasharray(new Float[]{0.01f, 2f}),
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(6f),
                lineColor(getColor(R.color.orange_red))
        );
        style.addLayer(route_layer);
    }

    private void StartMapBoxService() {
        if (MapboxMapService.CheckPermissionUserLocation(this)) {
            if (LocationService.IsGPSActivated(this)) {
                mapbox_service.ActivateLocationComponent(new MapboxMap.CancelableCallback() {
                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onFinish() {
                        StartTracking();
                        binding.btnFocusCurrentLocationShareTrip.setImageResource(R.drawable.ic_focus_my_location);
                    }
                });
            } else {
                OpenLocationSetting();
            }
        } else {
            result_location_permission.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void OpenLocationSetting() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        open_GPS_launcher.launch(intent);
    }

    private final ActivityResultLauncher<String> result_location_permission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if (result) {
            StartMapBoxService();
        } else {
            binding.setIsGettingLocation(false);
            NotificationDialog.Builder(this)
                    .SetIcon(R.drawable.ic_choose_map)
                    .SetTitle(getResources().getString(R.string.allow_location_access))
                    .SetSubtitle(getResources().getString(R.string.this_helps_us_determine_your_exact_location))
                    .SetTextMainButton(getResources().getString(R.string.close))
                    .SetOnMainButtonClicked(Dialog::dismiss).show();
        }
    });
    private final ActivityResultLauncher<Intent> open_GPS_launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (LocationService.IsGPSActivated(this)) {
            WaitingGPS();
        } else {
            binding.setIsGettingLocation(false);
            NotificationDialog.Builder(this)
                    .SetIcon(R.drawable.ic_choose_map)
                    .SetTitle(getResources().getString(R.string.enable_gps))
                    .SetSubtitle(getResources().getString(R.string.this_helps_us_determine_your_exact_location))
                    .SetTextMainButton(getResources().getString(R.string.close))
                    .SetOnMainButtonClicked(Dialog::dismiss).show();
        }
    });

    private void WaitingGPS() {
        binding.setIsGettingLocation(true);
        LocationService locationService = new LocationService(this);
        LocationEngineCallback<LocationEngineResult> callback = new LocationEngineCallback<LocationEngineResult>() {
            @Override
            public void onSuccess(LocationEngineResult result) {
                StartMapBoxService();
                binding.setIsGettingLocation(false);
                locationService.Stop();
            }

            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(FindTripActivity.this, "WaitingGPS onFailure", Toast.LENGTH_SHORT).show();
            }
        };
        locationService.SetCallback(callback).Start();
    }

    private void StartTracking() {
        if (MapboxMapService.CheckPermissionUserLocation(this)) {
            if (LocationService.IsGPSActivated(this)) {
                if (!mapbox_service.GetMapBoxMap().getLocationComponent().isLocationComponentActivated()) {
                    mapbox_service.ActivateLocationComponent(new MapboxMap.CancelableCallback() {
                        @Override
                        public void onCancel() {

                        }

                        @Override
                        public void onFinish() {
                            mapbox_service.StartTrackingMode(null);
                        }
                    });
                } else {
                    mapbox_service.StartTrackingMode(null);
                }

                binding.btnFocusCurrentLocationShareTrip.setImageResource(R.drawable.ic_focus_my_location);
            } else {
                OpenLocationSetting();
            }
        } else {
            result_location_permission.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.mapShareTransport.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.mapShareTransport.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        binding.mapShareTransport.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.mapShareTransport.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.mapShareTransport.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.mapShareTransport.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        binding.mapShareTransport.onSaveInstanceState(outState);
    }
}