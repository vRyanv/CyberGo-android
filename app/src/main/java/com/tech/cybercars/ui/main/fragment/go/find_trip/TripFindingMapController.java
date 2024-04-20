package com.tech.cybercars.ui.main.fragment.go.find_trip;

import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleRadius;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.tech.cybercars.R;
import com.tech.cybercars.adapter.trip_found.TripFoundAdapter;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.VehicleType;
import com.tech.cybercars.data.remote.trip.find_trip.MemberBody;
import com.tech.cybercars.data.models.TripFound;
import com.tech.cybercars.data.models.trip.Destination;
import com.tech.cybercars.databinding.ActivityFindTripBinding;
import com.tech.cybercars.services.mapbox.MapboxMapService;
import com.tech.cybercars.ui.main.fragment.go.find_trip.trip_found_detail.TripFoundDetailActivity;
import com.tech.cybercars.utils.AnimatorUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TripFindingMapController {
    private List<TripFound> trip_found_list;
    private TripFoundAdapter trip_found_adapter;
    private final Context context;
    private final FindTripViewModel view_model;
    private final MapboxMapService mapbox_service;
    private AnimatorSet animator_set;
    private final List<Feature> trip_features = new ArrayList<>();
    private final long ACTIVE_TRIP_ANIMATION_DURATION = 1500;
    private final String DESTINATION_MARKER_LAYER_ID = "DESTINATION_MARKER_LAYER_ID";
    private final String DESTINATION_POINT_SOURCE_ID = "DESTINATION_POINT_SOURCE_ID";
    private final String DESTINATION_POINT_LAYER_ID = "DESTINATION_POINT_LAYER_ID";
    private final String TRIP_ROUTE_SOURCE_ID = "TRIP_ROUTE_SOURCE_ID";
    private final String TRIP_SOURCE_ID = "TRIP_SOURCE_ID";
    private final String TRIP_LAYER_ID = "TRIP_LAYER_ID";
    private final String ACTIVE_TRIP_LAYER_ID = "ACTIVE_TRIP_LAYER_ID";
    private final String INDEX_PROP = "INDEX_PROP";
    private final String IS_ACTIVE = "IS_ACTIVE";
    private final String ICON_IMAGE = "ICON_IMAGE";
    private final String SIZE_ICON_IMAGE = "SIZE_ICON_IMAGE";
    private final String CAR_IMAGE_ID = "CAR_IMAGE_ID";
    private final String MOTO_IMAGE_ID = "MOTO_IMAGE_ID";
    private final String TRUCK_IMAGE_ID = "TRUCK_IMAGE_ID";

    private final ActivityFindTripBinding find_trip_binding;
    public TripFindingMapController(Context context, FindTripViewModel view_model, MapboxMapService mapbox_service, ActivityFindTripBinding find_trip_binding) {
        this.find_trip_binding = find_trip_binding;
        this.context = context;
        this.view_model = view_model;
        this.mapbox_service = mapbox_service;
        InitRecyclerViewTripFound();
        InitVehicleTypeFilter();
        InitTripSource();
    }
    public void UpdateTripFoundAdapter(List<TripFound> trip_found_list) {
        trip_found_adapter.UpdateAdapter(trip_found_list);
        this.trip_found_list = trip_found_list;
        if (trip_found_list.isEmpty()) {
            ClearTripFoundData();
            return;
        }
        mapbox_service.GetMapBoxMap().getStyle(style -> {
            ExecutorService executor_service = Executors.newSingleThreadExecutor();
            executor_service.execute(() -> {
                BindDataToTripSource(style);
            });
            executor_service.shutdown();
        });
    }
    private void ClearTripFoundData() {
        find_trip_binding.imgCarSelected.setBackground(AppCompatResources.getDrawable(context, R.drawable.shape_transport_type_selected));
        find_trip_binding.imgTruckSelected.setBackground(AppCompatResources.getDrawable(context, R.drawable.shape_transport_type_selected));
        find_trip_binding.imgMotoSelected.setBackground(AppCompatResources.getDrawable(context, R.drawable.shape_transport_type_selected));
        find_trip_binding.imgAllVehicleTypeSelected.setBackground(AppCompatResources.getDrawable(context, R.drawable.shape_vehicle_type_selected));
        mapbox_service.GetMapBoxMap().getStyle(style -> {
            GeoJsonSource trip_src = style.getSourceAs(TRIP_SOURCE_ID);
            GeoJsonSource trip_route_src = style.getSourceAs(TRIP_ROUTE_SOURCE_ID);
            GeoJsonSource destination_src = style.getSourceAs(DESTINATION_POINT_SOURCE_ID);

            assert trip_src != null;
            trip_src.setGeoJson(FeatureCollection.fromFeatures(new ArrayList<>()));

            assert trip_route_src != null;
            trip_route_src.setGeoJson(FeatureCollection.fromFeatures(new ArrayList<>()));

            assert destination_src != null;
            destination_src.setGeoJson(FeatureCollection.fromFeatures(new ArrayList<>()));
        });
    }
    private void InitRecyclerViewTripFound() {
        trip_found_adapter = new TripFoundAdapter(context, trip_found_list);
        trip_found_adapter.SetOnItemClicked(this::TripFoundItemClicked);
        trip_found_adapter.SetOnSeeOverviewButtonClicked(this::TripFoundSeeOverviewClicked);
        trip_found_adapter.SetOnTargetVehicleButtonClicked(this::TripFoundTargetVehicleClicked);
        LinearLayoutManager layout_manager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        find_trip_binding.rcvTripFound.setLayoutManager(layout_manager);
        find_trip_binding.rcvTripFound.setItemAnimator(new DefaultItemAnimator());
        find_trip_binding.rcvTripFound.setAdapter(trip_found_adapter);
        find_trip_binding.rcvTripFound.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int index = layout_manager.findFirstVisibleItemPosition();
                    SetActiveTrip(index, true);
                    ReFreshTripSource();
                }
            }
        });

        SnapHelper snap_helper = new PagerSnapHelper();
        snap_helper.attachToRecyclerView(find_trip_binding.rcvTripFound);
    }
    private void InitTripSource() {
        mapbox_service.GetMapBoxMap().getStyle(style -> {
            // destination point layer
            Bitmap destination_point_bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_destination_point_bitmap);
            String DESTINATION_POINT_IMAGE_ID = "DESTINATION_POINT_IMAGE_ID";
            style.addImage(DESTINATION_POINT_IMAGE_ID, destination_point_bitmap);
            style.addSource(new GeoJsonSource(DESTINATION_POINT_SOURCE_ID));
            style.addLayer(
                    new SymbolLayer(DESTINATION_POINT_LAYER_ID, DESTINATION_POINT_SOURCE_ID)
                            .withProperties(
                                    iconImage(DESTINATION_POINT_IMAGE_ID),
                                    iconOffset(new Float[]{0f, -256f}),
                                    iconIgnorePlacement(true),
                                    iconAllowOverlap(true),
                                    iconSize(.08f)
                            ));

            // trip route
            style.addSource(new GeoJsonSource(TRIP_ROUTE_SOURCE_ID));
            LineLayer route_layer = new LineLayer("TRIP_ROUTE_LAYER_ID", TRIP_ROUTE_SOURCE_ID);
            route_layer.setProperties(
                    lineCap(Property.LINE_CAP_ROUND),
                    lineJoin(Property.LINE_JOIN_ROUND),
                    lineWidth(6f),
                    lineColor(context.getColor(R.color.blue_special)),
                    lineOpacity(.7f)
            );

            style.addLayer(route_layer);

            // vehicle icon image
            Bitmap car_bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_car_bitmap);
            style.addImage(CAR_IMAGE_ID, car_bitmap);
            Bitmap moto_bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_motorcycle_bitmap);
            style.addImage(MOTO_IMAGE_ID, moto_bitmap);
            Bitmap truck_bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_truck_bitmap);
            style.addImage(TRUCK_IMAGE_ID, truck_bitmap);

            // trip layer
            style.addSource(new GeoJsonSource(TRIP_SOURCE_ID));
            SymbolLayer trip_layer = new SymbolLayer(TRIP_LAYER_ID, TRIP_SOURCE_ID)
                    .withProperties(
                            iconImage(get(ICON_IMAGE)),
                            iconOffset(new Float[]{0f, -25f}),
                            iconIgnorePlacement(true),
                            iconAllowOverlap(true),
                            iconSize(get(SIZE_ICON_IMAGE))
                    );
            style.addLayerBelow(trip_layer, DESTINATION_MARKER_LAYER_ID);

            // active trip
            float start_radius = 10f;
            float end_radius = 40f;
            CircleLayer active_trip_layer = new CircleLayer(ACTIVE_TRIP_LAYER_ID, TRIP_SOURCE_ID)
                    .withProperties(
                            circleRadius(start_radius),
                            circleColor(context.getColor(R.color.orange)),
                            circleOpacity(0.4f)
                    );
            active_trip_layer.withFilter(Expression.eq(get(IS_ACTIVE), true));
            ValueAnimator animator = ValueAnimator.ofFloat(start_radius, end_radius);
            animator.setDuration(1000);
            animator.setRepeatMode(ValueAnimator.REVERSE);
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.addUpdateListener(valueAnimator -> {
                float currentRadius = (float) valueAnimator.getAnimatedValue();
                active_trip_layer.setProperties(circleRadius(currentRadius));
            });
            animator.start();
            style.addLayerBelow(active_trip_layer, TRIP_LAYER_ID);


        });
    }
    private void ActiveTripRoute(TripFound trip_found) {
        StringBuilder total_geometry = new StringBuilder();
        List<Feature> marker_feature_list = new ArrayList<>();
        for (Destination destination : trip_found.destination_list) {
            total_geometry.append(destination.geometry);

            double longitude = destination.longitude;
            double latitude = destination.latitude;
            Feature marker_feature = Feature.fromGeometry(Point.fromLngLat(longitude, latitude));
            marker_feature_list.add(marker_feature);
        }


        mapbox_service.GetMapBoxMap().getStyle(style -> {
            GeoJsonSource trip_route_src = style.getSourceAs(TRIP_ROUTE_SOURCE_ID);
            assert trip_route_src != null;
            trip_route_src.setGeoJson(LineString.fromPolyline(total_geometry.toString(), PRECISION_6));

            GeoJsonSource trip_marker_src = style.getSourceAs(DESTINATION_POINT_SOURCE_ID);
            assert trip_marker_src != null;
            trip_marker_src.setGeoJson(FeatureCollection.fromFeatures(marker_feature_list));
        });
    }
    private void InitVehicleTypeFilter() {
        find_trip_binding.imgCarSelected.setOnClickListener(view -> ActiveVehicleTypeFilter(find_trip_binding.imgCarSelected, VehicleType.CAR));
        find_trip_binding.imgTruckSelected.setOnClickListener(view -> ActiveVehicleTypeFilter(find_trip_binding.imgTruckSelected, VehicleType.TRUCK));
        find_trip_binding.imgMotoSelected.setOnClickListener(view -> ActiveVehicleTypeFilter(find_trip_binding.imgMotoSelected, VehicleType.MOTO));
        find_trip_binding.imgAllVehicleTypeSelected.setOnClickListener(view -> ActiveVehicleTypeFilter(find_trip_binding.imgAllVehicleTypeSelected, VehicleType.ALL));
    }
    private void ActiveVehicleTypeFilter(ImageView img_view, String vehicle_type) {
        find_trip_binding.imgCarSelected.setBackground(AppCompatResources.getDrawable(context, R.drawable.shape_transport_type_selected));
        find_trip_binding.imgTruckSelected.setBackground(AppCompatResources.getDrawable(context, R.drawable.shape_transport_type_selected));
        find_trip_binding.imgMotoSelected.setBackground(AppCompatResources.getDrawable(context, R.drawable.shape_transport_type_selected));
        find_trip_binding.imgAllVehicleTypeSelected.setBackground(AppCompatResources.getDrawable(context, R.drawable.shape_transport_type_selected));
        img_view.setBackground(AppCompatResources.getDrawable(context, R.drawable.shape_vehicle_type_selected));

        List<Feature> trip_feature_filter;
        List<TripFound> trip_found_filter;
        if(vehicle_type.equals(VehicleType.ALL)){
            trip_feature_filter = trip_features;
            trip_found_filter = trip_found_list;
        } else {
            trip_found_filter = new ArrayList<>();
            trip_feature_filter = new ArrayList<>();
            for (int i = 0; i < trip_found_list.size(); i++) {
                TripFound trip_found = trip_found_list.get(i);
                if (trip_found.vehicle_type.equals(vehicle_type)) {
                    trip_found_filter.add(trip_found);
                    trip_feature_filter.add(trip_features.get(i));
                }
            }
        }

        trip_found_adapter.UpdateAdapter(trip_found_filter);
        mapbox_service.GetMapBoxMap().getStyle(style -> {
            //remove current trip route
            GeoJsonSource trip_route_src = style.getSourceAs(TRIP_ROUTE_SOURCE_ID);
            assert trip_route_src != null;
            trip_route_src.setGeoJson(FeatureCollection.fromFeatures(new ArrayList<>()));

            //remove current destination of trip
            GeoJsonSource destination_point_src = style.getSourceAs(DESTINATION_POINT_SOURCE_ID);
            assert destination_point_src != null;
            destination_point_src.setGeoJson(FeatureCollection.fromFeatures(new ArrayList<>()));

            //active first trip
            if(trip_feature_filter.size() > 0){
                SetInactiveAllTrip(trip_feature_filter);
                Feature trip_feature = trip_feature_filter.get(0);
                trip_feature.addBooleanProperty(IS_ACTIVE, true);
                AnimateCameraToActiveTrip(trip_feature);
            }

            GeoJsonSource trip_src = style.getSourceAs(TRIP_SOURCE_ID);
            assert trip_src != null;
            trip_src.setGeoJson(FeatureCollection.fromFeatures(trip_feature_filter));

            //active trip route
            if(trip_found_filter.size() > 0){
                ActiveTripRoute(trip_found_filter.get(0));
                find_trip_binding.setIsShowRcvTripFound(true);
                find_trip_binding.setIsShowThumbNotFound(false);
                find_trip_binding.rcvTripFound.scrollToPosition(0);
            } else {
                find_trip_binding.setIsShowRcvTripFound(false);
                find_trip_binding.setIsShowThumbNotFound(true);
            }
        });
    }
    private void TripFoundItemClicked(TripFound trip_found) {
        Intent trip_found_detail_intent = new Intent(context, TripFoundDetailActivity.class);
        trip_found_detail_intent.putExtra(FieldName.TRIP_FOUND, trip_found);

        MemberBody.Location origin = new MemberBody.Location();
        origin.latitude = view_model.origin_reverse.lat;
        origin.longitude = view_model.origin_reverse.lng;
        origin.address = view_model.origin_reverse.display_name;
        MemberBody.Location destination = new MemberBody.Location();
        destination.latitude = view_model.destination_reverse.lat;
        destination.longitude = view_model.destination_reverse.lng;
        destination.address = view_model.destination_reverse.display_name;
        MemberBody member = new MemberBody(
                trip_found.trip_id,
                origin,
                destination,
                trip_found.destination_list.get(0).geometry
        );
        trip_found_detail_intent.putExtra(FieldName.MEMBER, member);

        context.startActivity(trip_found_detail_intent);
    }
    private void TripFoundSeeOverviewClicked(TripFound trip_found) {
        int destination_size = trip_found.destination_list.size();
        Destination destination = trip_found.destination_list.get(destination_size - 1);

        LatLng northeast = new LatLng(trip_found.origin_latitude, trip_found.origin_longitude);
        LatLng southwest = new LatLng(destination.latitude, destination.longitude);
        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                .include(northeast)
                .include(southwest)
                .build();

        mapbox_service.GetMapBoxMap().easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 50), 1500);
    }
    private void TripFoundTargetVehicleClicked(int trip_index) {
        Feature vehicle_feature = trip_features.get(trip_index);
        AnimateCameraToActiveTrip(vehicle_feature);
    }
    private void SetActiveTrip(int index, boolean with_scroll) {
        SetInactiveAllTrip(trip_features);
        Feature trip_feature = trip_features.get(index);
        trip_feature.addBooleanProperty(IS_ACTIVE, true);
        TripFound trip_found = trip_found_list.get(index);
        ActiveTripRoute(trip_found);
        AnimateCameraToActiveTrip(trip_feature);
        if (!with_scroll) {
            find_trip_binding.rcvTripFound.scrollToPosition(index);
        }
    }
    private void SetInactiveAllTrip(List<Feature> trip_feature_list) {
        for (Feature trip_feature : trip_feature_list) {
            trip_feature.addBooleanProperty(IS_ACTIVE, false);
        }
    }
    private void ReFreshTripSource() {
        mapbox_service.GetMapBoxMap().getStyle(style -> {
            GeoJsonSource trip_src = style.getSourceAs(TRIP_SOURCE_ID);
            assert trip_src != null;
            trip_src.setGeoJson(FeatureCollection.fromFeatures(trip_features));
        });
    }
    private void BindDataToTripSource(Style style) {
        for (int i = 0; i < trip_found_list.size(); i++) {
            TripFound trip_found = trip_found_list.get(i);
            Feature feature = Feature.fromGeometry(Point.fromLngLat(
                    trip_found.origin_longitude,
                    trip_found.origin_latitude
            ));
            feature.addNumberProperty(INDEX_PROP, i);
            feature.addBooleanProperty(IS_ACTIVE, false);
            switch (trip_found.vehicle_type) {
                case VehicleType.CAR:
                    feature.addStringProperty(ICON_IMAGE, CAR_IMAGE_ID);
                    feature.addNumberProperty(SIZE_ICON_IMAGE, .06f);
                    feature.addStringProperty(FieldName.VEHICLE_TYPE, VehicleType.CAR);
                    break;
                case VehicleType.MOTO:
                    feature.addStringProperty(ICON_IMAGE, MOTO_IMAGE_ID);
                    feature.addNumberProperty(SIZE_ICON_IMAGE, .07f);
                    feature.addStringProperty(FieldName.VEHICLE_TYPE, VehicleType.MOTO);
                    break;
                default:
                    feature.addStringProperty(ICON_IMAGE, TRUCK_IMAGE_ID);
                    feature.addNumberProperty(SIZE_ICON_IMAGE, .11f);
                    feature.addStringProperty(FieldName.VEHICLE_TYPE, VehicleType.TRUCK);
                    break;
            }
            trip_features.add(feature);
        }

        Handler main_handler = new Handler(Looper.getMainLooper());
        main_handler.post(() -> {
            GeoJsonSource trip_src = style.getSourceAs(TRIP_SOURCE_ID);
            FeatureCollection trip_feature_collection = FeatureCollection.fromFeatures(trip_features);
            assert trip_src != null;
            Feature active_trip_feature = trip_features.get(0);
            active_trip_feature.addBooleanProperty(IS_ACTIVE, true);
            trip_src.setGeoJson(trip_feature_collection);
            ActiveTripRoute(trip_found_list.get(0));
            AnimateCameraToActiveTrip(active_trip_feature);
        });
    }
    public boolean onMapClick(@NonNull LatLng point) {
        PointF screenPoint = mapbox_service.GetMapBoxMap().getProjection().toScreenLocation(point);
        List<Feature> features = mapbox_service.GetMapBoxMap().queryRenderedFeatures(screenPoint, TRIP_LAYER_ID);
        if (!features.isEmpty()) {
            int index = features.get(0).getNumberProperty(INDEX_PROP).intValue();
            SetActiveTrip(index, false);
            ReFreshTripSource();
            return true;
        }
        return false;
    }
    private void AnimateCameraToActiveTrip(Feature feature) {
        CameraPosition cameraPosition = mapbox_service.GetMapBoxMap().getCameraPosition();
        if (animator_set != null) {
            animator_set.cancel();
        }
        animator_set = new AnimatorSet();
        AnimatorUtil animatorUtil = new AnimatorUtil(mapbox_service.GetMapBoxMap());
        Point symbolPoint = (Point) feature.geometry();
        LatLng latLng = new LatLng(symbolPoint.latitude(), symbolPoint.longitude());

        double bearing = new Random().nextInt(90);;
        animator_set.playTogether(
                animatorUtil.CreateLatLngAnimator(cameraPosition.target, latLng, ACTIVE_TRIP_ANIMATION_DURATION),
                animatorUtil.CreateZoomAnimator(cameraPosition.zoom, 16, ACTIVE_TRIP_ANIMATION_DURATION),
                animatorUtil.CreateBearingAnimator(cameraPosition.bearing, bearing, ACTIVE_TRIP_ANIMATION_DURATION),
                animatorUtil.CreateTiltAnimator(cameraPosition.tilt, 45, ACTIVE_TRIP_ANIMATION_DURATION)
        );
        animator_set.start();
    }
}
