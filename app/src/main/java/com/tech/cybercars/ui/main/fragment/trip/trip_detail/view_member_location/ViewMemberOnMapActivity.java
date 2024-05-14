package com.tech.cybercars.ui.main.fragment.trip.trip_detail.view_member_location;

import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineDasharray;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineOpacity;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineResult;
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
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.PickLocation;
import com.tech.cybercars.constant.ThemeMode;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.constant.VehicleType;
import com.tech.cybercars.data.models.TripManagement;
import com.tech.cybercars.data.models.trip.Destination;
import com.tech.cybercars.databinding.ActivityViewMemberOnMapBinding;
import com.tech.cybercars.services.location.LocationService;
import com.tech.cybercars.services.mapbox.MapboxMapService;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.component.dialog.NotificationDialog;
import com.tech.cybercars.utils.DateUtil;
import com.tech.cybercars.utils.Helper;

import java.util.ArrayList;
import java.util.List;

public class ViewMemberOnMapActivity extends AppCompatActivity {
    private MapboxMapService mapbox_service;

    private ActivityViewMemberOnMapBinding binding;
    private Bundle savedInstanceState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        InitFirst();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_member_on_map);
        InitView();
    }

    private void InitFirst() {
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
    }

    private void InitView() {
        InitMapBox();

        binding.btnFocusCurrentLocationShareTrip.setOnClickListener(view -> {
            StartTracking();
        });

        binding.btnOutShareTransport.setOnClickListener(view -> {
            finish();
        });


    }


    private void DrawRoute(Style style, String owner_geo, String member_geo) {
        GeoJsonSource owner_route_source = style.getSourceAs(OWNER_ROUTE_SRC_ID);
        assert owner_route_source != null;
        owner_route_source.setGeoJson(LineString.fromPolyline(owner_geo, PRECISION_6));

        GeoJsonSource member_route_source = style.getSourceAs(MEMBER_ROUTE_SRC_ID);
        assert member_route_source != null;
        member_route_source.setGeoJson(LineString.fromPolyline(member_geo, PRECISION_6));
    }

    private void MakeBoundingBox(LatLng northeast, LatLng southwest) {
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

                    }
                });
    }

    private void MarkLocationOnMap(Style style, Point destination_point, Point vehicle_point, String vehicle_type, FeatureCollection member_feature_collection) {
        //owner
        GeoJsonSource owner_destination_source = style.getSourceAs(OWNER_DESTINATION_SRC_ID);
        assert owner_destination_source != null;
        owner_destination_source.setGeoJson(destination_point);

        GeoJsonSource owner_vehicle_source = style.getSourceAs(OWNER_VEHICLE_SRC_ID);
        assert owner_vehicle_source != null;
        Feature vehicle_feature = Feature.fromGeometry(vehicle_point);

        switch (vehicle_type){
            case VehicleType.TRUCK:
                vehicle_feature.addStringProperty(ICON_IMAGE, TRUCK_IMAGE_ID);
                vehicle_feature.addNumberProperty(SIZE_ICON_IMAGE, .11f);
                break;
            case VehicleType.MOTO:
                vehicle_feature.addStringProperty(ICON_IMAGE, MOTO_IMAGE_ID);
                vehicle_feature.addNumberProperty(SIZE_ICON_IMAGE, .07f);
                break;
            default:
                vehicle_feature.addStringProperty(ICON_IMAGE, CAR_IMAGE_ID);
                vehicle_feature.addNumberProperty(SIZE_ICON_IMAGE, .06f);
        }
        owner_vehicle_source.setGeoJson(vehicle_feature);

        //member
        GeoJsonSource member_marker_source = style.getSourceAs(MEMBER_MARKER_SRC_ID);
        assert member_marker_source != null;
        member_marker_source.setGeoJson(member_feature_collection);

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
                        }
                    });

                    InitLayerAndSource(style);
                });
    }
    private final String OWNER_DESTINATION_LAYER_ID = "OWNER_DESTINATION_LAYER_ID";
    private final String OWNER_DESTINATION_SRC_ID = "OWNER_DESTINATION_SRC_ID";
    private final String OWNER_VEHICLE_LAYER_ID = "OWNER_VEHICLE_LAYER_ID";
    private final String OWNER_VEHICLE_SRC_ID = "OWNER_VEHICLE_SRC_ID";
    private final String OWNER_ROUTE_LAYER_ID = "OWNER_ROUTE_LAYER_ID";
    private final String OWNER_ROUTE_SRC_ID = "OWNER_ROUTE_SRC_ID";
    private final String DESTINATION_MARKER_ICON_IMAGE_ID = "DESTINATION_MARKER_ICON_IMAGE_ID";
    private final String SIZE_ICON_IMAGE = "SIZE_ICON_IMAGE";
    private final String CAR_IMAGE_ID = "CAR_IMAGE_ID";
    private final String MOTO_IMAGE_ID = "MOTO_IMAGE_ID";
    private final String TRUCK_IMAGE_ID = "TRUCK_IMAGE_ID";
    private final String MEMBER_MARKER_LAYER_ID = "MEMBER_MARKER_LAYER_ID";
    private final String MEMBER_MARKER_SRC_ID = "MEMBER_MARKER_SRC_ID";
    private final String MEMBER_ROUTE_SRC_ID = "MEMBER_ROUTE_SRC_ID";
    private final String MEMBER_ROUTE_LAYER_ID = "MEMBER_ROUTE_LAYER_ID";
    private final String START_MARKER_ICON_IMAGE_ID = "START_MARKER_ICON_IMAGE_ID";
    private final String DESTINATION_MARKER_LAYER_ID = "DESTINATION_MARKER_LAYER_ID";


    private final String ICON_IMAGE = "ICON_IMAGE";
    private void InitLayerAndSource(Style style) {
        //member
        //marker image
        Bitmap start_icon_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_placeholder_red_bitmap);
        style.addImage(START_MARKER_ICON_IMAGE_ID, start_icon_bitmap);

        Bitmap destination_icon_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_placeholder_green_bitmap);
        style.addImage(DESTINATION_MARKER_ICON_IMAGE_ID, destination_icon_bitmap);

        // marker layer
        style.addSource(new GeoJsonSource(MEMBER_MARKER_SRC_ID));
        style.addLayer(
                new SymbolLayer(MEMBER_MARKER_LAYER_ID, MEMBER_MARKER_SRC_ID)
                        .withProperties(
                                iconImage(Expression.get(ICON_IMAGE)),
                                iconOffset(new Float[]{0f, -65f}),
                                iconIgnorePlacement(true),
                                iconAllowOverlap(true),
                                iconSize(.3f)
                        ));

        // route layer
        style.addSource(new GeoJsonSource(MEMBER_ROUTE_SRC_ID));
        LineLayer member_route_layer = new LineLayer(MEMBER_ROUTE_LAYER_ID, MEMBER_ROUTE_SRC_ID);
        member_route_layer.setProperties(
                lineDasharray(new Float[]{0.01f, 2f}),
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(6f),
                lineColor(getColor(R.color.orange_red))
        );
        style.addLayer(member_route_layer);


        //owner
        // destination point layer
        Bitmap destination_point_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_destination_point_bitmap);
        String DESTINATION_POINT_IMAGE_ID = "DESTINATION_POINT_IMAGE_ID";
        style.addImage(DESTINATION_POINT_IMAGE_ID, destination_point_bitmap);
        style.addSource(new GeoJsonSource(OWNER_DESTINATION_SRC_ID));
        style.addLayer(
                new SymbolLayer(OWNER_DESTINATION_LAYER_ID, OWNER_DESTINATION_SRC_ID)
                        .withProperties(
                                iconImage(DESTINATION_POINT_IMAGE_ID),
                                iconOffset(new Float[]{0f, -256f}),
                                iconIgnorePlacement(true),
                                iconAllowOverlap(true),
                                iconSize(.08f)
                        ));

        // owner route
        style.addSource(new GeoJsonSource(OWNER_ROUTE_SRC_ID));
        LineLayer owner_route_layer = new LineLayer(OWNER_ROUTE_LAYER_ID, OWNER_ROUTE_SRC_ID);
        owner_route_layer.setProperties(
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(6f),
                lineColor(getColor(R.color.blue_special)),
                lineOpacity(.7f)
        );

        style.addLayer(owner_route_layer);

        // vehicle icon image
        Bitmap car_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_car_bitmap);
        style.addImage(CAR_IMAGE_ID, car_bitmap);
        Bitmap moto_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_motorcycle_bitmap);
        style.addImage(MOTO_IMAGE_ID, moto_bitmap);
        Bitmap truck_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_truck_bitmap);
        style.addImage(TRUCK_IMAGE_ID, truck_bitmap);

        // vehicle layer
            style.addSource(new GeoJsonSource(OWNER_VEHICLE_SRC_ID));
        SymbolLayer trip_layer = new SymbolLayer(OWNER_VEHICLE_LAYER_ID, OWNER_VEHICLE_SRC_ID)
                .withProperties(
                        iconImage(get(ICON_IMAGE)),
                        iconOffset(new Float[]{0f, -25f}),
                        iconIgnorePlacement(true),
                        iconAllowOverlap(true),
                        iconSize(get(SIZE_ICON_IMAGE))
                );
        style.addLayerBelow(trip_layer, DESTINATION_MARKER_LAYER_ID);


        BindDataToUI(style);
    }

    private void BindDataToUI(Style style) {
        TripManagement trip_management = (TripManagement) getIntent().getSerializableExtra(FieldName.TRIP);
        TripManagement.Member member = (TripManagement.Member) getIntent().getSerializableExtra(FieldName.MEMBER);

        assert member != null;
        String time = DateUtil.ConvertSecondToHour(member.destination.time);
        binding.txtTime.setText(time);
        double distance_meter = member.destination.distance;

        String avatar_full_path = URL.BASE_URL + URL.AVATAR_RES_PATH + member.avatar;
        Glide.with(this).load(avatar_full_path).into(binding.imgAvatar);


        if (distance_meter < 1000) {
            binding.txtDistance.setText(Math.round(distance_meter) + "m");
        } else {
            String rounded_distance = Helper.ConvertMeterToKiloMeterString(distance_meter);
            binding.txtDistance.setText(rounded_distance + " Km");
        }

        //owner
        assert trip_management != null;
        Destination destination = trip_management.destinations.get(0);
        Point vehicle_point = Point.fromLngLat(trip_management.origin_longitude, trip_management.origin_latitude);
        Point destination_point = Point.fromLngLat(destination.longitude, destination.latitude);

        //member
        List<Feature> member_feature = new ArrayList<>();

        Point start_point = Point.fromLngLat(member.origin.longitude, member.origin.latitude);
        Feature feature_start_point = Feature.fromGeometry(start_point);
        feature_start_point.addStringProperty(ICON_IMAGE, START_MARKER_ICON_IMAGE_ID);
        member_feature.add(feature_start_point);

        Point end_point = Point.fromLngLat(member.destination.longitude, member.destination.latitude);
        Feature feature_end_point = Feature.fromGeometry(end_point);
        feature_end_point.addStringProperty(ICON_IMAGE, DESTINATION_MARKER_ICON_IMAGE_ID);
        member_feature.add(feature_end_point);

        binding.btnBound.setOnClickListener(view -> {
            MakeBoundingBox(
                    new LatLng(member.origin.latitude, member.origin.longitude),
                    new LatLng(member.destination.latitude, member.destination.longitude)
            );
        });

        String vehicle_type = trip_management.vehicle.type;
        MarkLocationOnMap(style, destination_point, vehicle_point, vehicle_type, FeatureCollection.fromFeatures(member_feature));

        String owner_geo = destination.geometry;
        String member_geo = member.geometry;
        DrawRoute(style, owner_geo, member_geo);
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
                Toast.makeText(ViewMemberOnMapActivity.this, "WaitingGPS onFailure", Toast.LENGTH_SHORT).show();
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