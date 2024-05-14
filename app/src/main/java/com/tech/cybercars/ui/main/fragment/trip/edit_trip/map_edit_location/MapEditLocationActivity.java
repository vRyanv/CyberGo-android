package com.tech.cybercars.ui.main.fragment.trip.edit_trip.map_edit_location;

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
import com.mapbox.api.directions.v5.models.DirectionsRoute;
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
import com.tech.cybercars.constant.ActivityResult;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.PickLocation;
import com.tech.cybercars.constant.ThemeMode;
import com.tech.cybercars.constant.VehicleType;
import com.tech.cybercars.data.models.TripManagement;
import com.tech.cybercars.data.models.trip.Destination;
import com.tech.cybercars.data.remote.map.reverse_geocoding.ReverseGeocodingResponse;
import com.tech.cybercars.databinding.ActivityMapEditLocationBinding;
import com.tech.cybercars.services.location.LocationService;
import com.tech.cybercars.services.mapbox.MapboxMapService;
import com.tech.cybercars.services.mapbox.MapboxNavigationService;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.component.dialog.NotificationDialog;
import com.tech.cybercars.ui.main.fragment.trip.edit_trip.map_edit_location.review_location.ReviewLocationActivity;
import com.tech.cybercars.utils.DateUtil;
import com.tech.cybercars.utils.Helper;
import com.tech.cybercars.utils.KeyBoardUtil;

public class MapEditLocationActivity extends BaseActivity<ActivityMapEditLocationBinding, MapEditLocationViewModel> {

    private BottomSheetBehavior<LinearLayout> bottom_sheet_behavior;
    private final String START_POINT_GEO_JSON_SOURCE_LAYER_ID = "start-point-source";
    private final String DESTINATION_GEO_JSON_SOURCE_LAYER_ID = "destination-source";
    private final String ROUTE_GEO_JSON_SOURCE_LAYER_ID = "share-route-source";
    private int pick_location;
    private MapboxMapService mapbox_service;
    private final Handler search_debounce_handler = new Handler(Looper.getMainLooper());
    private Runnable search_debounce_runnable;
    private int min_height_bottom_sheet;
    private boolean is_prepare_data = true;

    @NonNull
    @Override
    protected MapEditLocationViewModel InitViewModel() {
        return new ViewModelProvider(this).get(MapEditLocationViewModel.class);
    }

    @Override
    protected ActivityMapEditLocationBinding InitBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map_edit_location);
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
            StartReviewLocationActivity();
        });

        binding.btnCancelNextStepShareTrip.setOnClickListener(view -> {
            binding.setIsShowOptionNextStep(false);
            binding.bsLocationPicker.setVisibility(View.VISIBLE);
        });
    }

    private void StartReviewLocationActivity() {
        view_model.trip_management.origin_latitude = view_model.origin_reverse.lat;
        view_model.trip_management.origin_longitude = view_model.origin_reverse.lng;
        view_model.trip_management.origin_city = view_model.origin_reverse.address.city;
        view_model.trip_management.origin_state = view_model.origin_reverse.address.state;
        view_model.trip_management.origin_county = view_model.origin_reverse.address.county;
        view_model.trip_management.origin_address = view_model.origin_address.getValue();

        Destination destination = view_model.trip_management.destinations.get(0);
        destination.geometry = view_model.current_route.getValue().geometry();
        destination.time = view_model.current_route.getValue().duration();
        destination.distance = view_model.current_route.getValue().distance();
        destination.city = view_model.destination_reverse.address.city;
        destination.state = view_model.destination_reverse.address.state;
        destination.county = view_model.destination_reverse.address.county;
        destination.address = view_model.destination_address.getValue();

        destination.longitude = view_model.destination_reverse.lng;
        destination.latitude = view_model.destination_reverse.lat;

        Intent review_location_intent = new Intent(this, ReviewLocationActivity.class);
        review_location_intent.putExtra(FieldName.TRIP, view_model.trip_management);
        review_location_launcher.launch(review_location_intent);
    }

    private final ActivityResultLauncher<Intent> review_location_launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == ActivityResult.UPDATED){
                    finish();
                }
            }
    );

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
            if (geometry.length() > MapboxNavigationService.GEOMETRY_LIMIT) {
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
                        if (is_prepare_data) {
                            is_prepare_data = false;
                            binding.bsLocationPicker.setVisibility(View.VISIBLE);
                            return;
                        }
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
                    BindDataToUI(style);
                });
    }

    private void BindDataToUI(Style style) {
        view_model.trip_management = (TripManagement) getIntent().getSerializableExtra(FieldName.TRIP);
        switch (view_model.trip_management.vehicle.type) {
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

        //origin reverse
        ReverseGeocodingResponse origin_reverse = new ReverseGeocodingResponse();
        origin_reverse.lat = view_model.trip_management.origin_latitude;
        origin_reverse.lng = view_model.trip_management.origin_longitude;

        origin_reverse.address = new ReverseGeocodingResponse.Address();
        origin_reverse.address.city = view_model.trip_management.origin_city;
        origin_reverse.address.state = view_model.trip_management.origin_state;
        origin_reverse.address.county = view_model.trip_management.origin_county;
        view_model.origin_reverse = origin_reverse;
        String origin_address = view_model.trip_management.origin_address;
        view_model.origin_address.setValue(origin_address);


        //destination reverse
        Destination destination = view_model.trip_management.destinations.get(0);
        ReverseGeocodingResponse destination_reverse = new ReverseGeocodingResponse();
        destination_reverse.lng = destination.longitude;
        destination_reverse.lat = destination.latitude;
        destination_reverse.address = new ReverseGeocodingResponse.Address();
        destination_reverse.address.city = destination.city;
        destination_reverse.address.state = destination.state;
        destination_reverse.address.county = destination.county;
        view_model.destination_reverse = destination_reverse;
        view_model.destination_address.setValue(destination.address);

        //current route
        DirectionsRoute current_route = DirectionsRoute.builder()
                .geometry(destination.geometry)
                .distance(destination.distance)
                .duration(destination.time)
                .build();
        view_model.current_route.setValue(current_route);

        String time = DateUtil.ConvertSecondToHour(destination.time);
        view_model.route_time.setValue(time);

        double distance_meter = destination.distance;
        if (distance_meter < 1000) {
            view_model.route_distance.setValue(Math.round(distance_meter) + "m");
        } else {
            String rounded_distance = Helper.ConvertMeterToKiloMeterString(distance_meter);
            view_model.route_distance.setValue(rounded_distance + " Km");
        }

        view_model.origin_address.setValue(view_model.trip_management.origin_address);
        view_model.destination_address.setValue(destination.address);

        //mark point
        Point start_point = Point.fromLngLat(view_model.trip_management.origin_longitude, view_model.trip_management.origin_latitude);
        Point destination_point = Point.fromLngLat(destination.longitude, destination.latitude);

        GeoJsonSource start_point_source = style.getSourceAs(START_POINT_GEO_JSON_SOURCE_LAYER_ID);
        GeoJsonSource destination_point_source = style.getSourceAs(DESTINATION_GEO_JSON_SOURCE_LAYER_ID);
        start_point_source.setGeoJson(start_point);
        destination_point_source.setGeoJson(destination_point);
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
                Toast.makeText(MapEditLocationActivity.this, "WaitingGPS onFailure", Toast.LENGTH_SHORT).show();
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