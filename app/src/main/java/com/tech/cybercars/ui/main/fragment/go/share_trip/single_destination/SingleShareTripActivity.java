package com.tech.cybercars.ui.main.fragment.go.share_trip.single_destination;

import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
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
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.tech.cybercars.R;
import com.tech.cybercars.constant.DestinationType;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.PickLocation;
import com.tech.cybercars.constant.ThemeMode;
import com.tech.cybercars.constant.VehicleType;
import com.tech.cybercars.data.models.Vehicle;
import com.tech.cybercars.data.models.trip.Destination;
import com.tech.cybercars.data.models.trip.Trip;
import com.tech.cybercars.databinding.ActivitySingleShareTripBinding;
import com.tech.cybercars.services.location.LocationService;
import com.tech.cybercars.services.mapbox.MapboxMapService;
import com.tech.cybercars.services.mapbox.MapboxNavigationService;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.component.dialog.NotificationDialog;
import com.tech.cybercars.ui.main.fragment.go.share_trip.add_share_trip_information.AddShareTripInformationActivity;
import com.tech.cybercars.utils.KeyBoardUtil;

import java.util.ArrayList;

public class SingleShareTripActivity extends BaseActivity<ActivitySingleShareTripBinding, SingleShareTripViewModel> {
    private BottomSheetBehavior<LinearLayout> bottom_sheet_behavior;
    private final String START_POINT_GEO_JSON_SOURCE_LAYER_ID = "start-point-source";
    private final String DESTINATION_GEO_JSON_SOURCE_LAYER_ID = "destination-source";
    private final String ROUTE_GEO_JSON_SOURCE_LAYER_ID = "share-route-source";
    private int pick_location;
    private MapboxMapService mapbox_service;
    private final Handler search_debounce_handler = new Handler(Looper.getMainLooper());
    private Runnable search_debounce_runnable;
    private int min_height_bottom_sheet;
    @NonNull
    @Override
    protected SingleShareTripViewModel InitViewModel() {
        return new ViewModelProvider(this).get(SingleShareTripViewModel.class);
    }

    @Override
    protected ActivitySingleShareTripBinding InitBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_single_share_trip);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        min_height_bottom_sheet = (int) getResources().getDimension(com.intuit.sdp.R.dimen._130sdp);
    }

    @Override
    protected void InitView() {
        InitMapBox();
        InitSearchAddressResultView();
        InitBottomSheet();
        InitInputLocation();
        InitOptionNext();

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

        view_model.vehicle.observe(this, vehicle -> {
            if (vehicle.vehicle_type != null) {
                switch (vehicle.vehicle_type) {
                    case VehicleType.CAR:
                        binding.imgTransportTypeToShare.setImageResource(R.drawable.ic_car);
                        break;
                    case VehicleType.MOTO:
                        binding.imgTransportTypeToShare.setImageResource(R.drawable.ic_motorcycle);
                        break;
                    case VehicleType.TRUCK:
                        binding.imgTransportTypeToShare.setImageResource(R.drawable.ic_truck);
                        break;
                }
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

    }

    @Override
    protected void InitCommon() {
        binding.setIsShowBtnChooseOnMap(false);
        binding.setIsGettingLocation(false);
        binding.setIsActivatedPlacePicker(false);
        binding.setIsExpandBottomSheet(false);
        binding.setIsShowOptionNextStep(false);

        Vehicle vehicle = (Vehicle) getIntent().getSerializableExtra(FieldName.VEHICLE);
        assert vehicle != null;
        view_model.vehicle.setValue(vehicle);
    }

    @Override
    protected void OnBackPress() {
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

    private void InitOptionNext() {
        binding.btnNextStepShareTrip.setOnClickListener(view -> {
            startAddTripInformationActivity();
        });

        binding.btnCancelNextStepShareTrip.setOnClickListener(view -> {
            binding.setIsShowOptionNextStep(false);
            binding.bsLocationPicker.setVisibility(View.VISIBLE);
        });
    }

    private void startAddTripInformationActivity() {
        String origin_city = view_model.origin_reverse.address.city;
        String origin_state = view_model.origin_reverse.address.state;
        String origin_county = view_model.origin_reverse.address.county;
        String origin_address = view_model.origin_address.getValue();
        ArrayList<Destination> destination_list = new ArrayList<>();
        String geometry = view_model.current_route.getValue().geometry();
        double time = view_model.current_route.getValue().duration();
        double distance = view_model.current_route.getValue().distance();
        String city = view_model.destination_reverse.address.city;
        String state = view_model.destination_reverse.address.state;
        String county = view_model.destination_reverse.address.county;
        String address = view_model.destination_address.getValue();
        double longitude = view_model.destination_reverse.lng;
        double latitude = view_model.destination_reverse.lat;
        Destination destination = new Destination(
                "",
                "",
                geometry,
                time,
                distance,
                city,
                state,
                county,
                address,
                longitude,
                latitude
        );
        destination_list.add(destination);
        double origin_longitude = view_model.origin_reverse.lng;
        double origin_latitude = view_model.origin_reverse.lat;
        Trip trip = new Trip(
                "",
                "",
                origin_city,
                origin_state,
                origin_county,
                origin_address,
                origin_longitude,
                origin_latitude,
                DestinationType.SINGLE,
                view_model.vehicle.getValue().vehicle_id,
                null,
                null,
                0
        );
        Intent trip_information_intent = new Intent(this, AddShareTripInformationActivity.class);
        trip_information_intent.putExtra(FieldName.TRIP, trip);
        trip_information_intent.putExtra(FieldName.DESTINATIONS, destination_list);
        startActivity(trip_information_intent);
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
            GeoJsonSource route_source = style.getSourceAs(ROUTE_GEO_JSON_SOURCE_LAYER_ID);
            assert route_source != null;
            route_source.setGeoJson(LineString.fromPolyline(geometry, PRECISION_6));
            binding.bsLocationPicker.setVisibility(View.GONE);
            if(geometry.length() > MapboxNavigationService.GEOMETRY_LIMIT){
                MapboxNavigationService.ShowRouteOutOfLimit(this);
                binding.setIsShowOptionNextStep(false);
            }
            MakeBoundingBox();
        });
    }

    private void MakeBoundingBox() {
        binding.setIsShowOverlay(true);
        LatLng northeast = new LatLng(view_model.origin_reverse.lat, view_model.origin_reverse.lng);
        LatLng southwest = new LatLng(view_model.destination_reverse.lat, view_model.destination_reverse.lng);
        LatLngBounds latLng_bounds = new LatLngBounds.Builder()
                .include(northeast)
                .include(southwest)
                .build();

        mapbox_service.GetMapBoxMap().easeCamera(CameraUpdateFactory.newLatLngBounds(
                        latLng_bounds, 50),
                3000, new MapboxMap.CancelableCallback() {
                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onFinish() {
                        binding.setIsShowOverlay(false);
                        boolean is_valid_limit = view_model.current_route.getValue().geometry().length() < MapboxNavigationService.GEOMETRY_LIMIT;
                        binding.setIsShowOptionNextStep(is_valid_limit);
                        binding.bsLocationPicker.setVisibility(is_valid_limit ? View.GONE : View.VISIBLE);
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
        binding.setIsShowOverlay(false);
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

    private void FindAddress() {
        final LatLng target_select = mapbox_service.GetMapBoxMap().getCameraPosition().target;
        view_model.HandleFindAddress(target_select, pick_location);
    }

    private void MarkLocationOnMap(Point point) {
        mapbox_service.GetMapBoxMap().getStyle(style -> {
            GeoJsonSource source;
            if (pick_location == PickLocation.PICK_START_POINT) {
                source = style.getSourceAs(START_POINT_GEO_JSON_SOURCE_LAYER_ID);
            } else {
                source = style.getSourceAs(DESTINATION_GEO_JSON_SOURCE_LAYER_ID);
            }
            assert source != null;
            source.setGeoJson(point);
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
        if(getResources().getString(R.string.theme_mode).equals(ThemeMode.DARK)){
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
                });
    }

    private void InitLayerAndSource(Style style) {
        Bitmap start_icon_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_placeholder_red_bitmap);
        String start_point_img_id = "start-point-id";
        style.addImage(start_point_img_id, start_icon_bitmap);
        style.addSource(new GeoJsonSource(START_POINT_GEO_JSON_SOURCE_LAYER_ID));
        style.addLayer(
                new SymbolLayer("start-point-layer-id", START_POINT_GEO_JSON_SOURCE_LAYER_ID)
                        .withProperties(
                                iconImage(start_point_img_id),
                                iconOffset(new Float[]{0f, -65f}),
                                iconIgnorePlacement(true),
                                iconAllowOverlap(true),
                                iconSize(.3f)
                        ));

        Bitmap destination_icon_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_placeholder_green_bitmap);
        String destination_img_id = "destination-point-id";
        style.addImage(destination_img_id, destination_icon_bitmap);
        style.addSource(new GeoJsonSource(DESTINATION_GEO_JSON_SOURCE_LAYER_ID));
        style.addLayer(
                new SymbolLayer("destination-layer-id", DESTINATION_GEO_JSON_SOURCE_LAYER_ID)
                        .withProperties(
                                iconImage(destination_img_id),
                                iconOffset(new Float[]{0f, -65f}),
                                iconIgnorePlacement(true),
                                iconAllowOverlap(true),
                                iconSize(.3f)
                        ));

        style.addSource(new GeoJsonSource(ROUTE_GEO_JSON_SOURCE_LAYER_ID));
        LineLayer route_layer = new LineLayer("route-layer-id", ROUTE_GEO_JSON_SOURCE_LAYER_ID);
        route_layer.setProperties(
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(6f),
                lineColor(getColor(R.color.orange))
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
                Toast.makeText(SingleShareTripActivity.this, "WaitingGPS onFailure", Toast.LENGTH_SHORT).show();
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