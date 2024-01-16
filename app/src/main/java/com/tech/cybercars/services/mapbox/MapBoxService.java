package com.tech.cybercars.services.mapbox;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.OnLocationCameraTransitionListener;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.tech.cybercars.R;

import java.util.ArrayList;
import java.util.List;

public class MapBoxService {
    private final Context context;
    private MapboxMap mapbox_map;

    public MapboxMap GetMapBoxMap() {
        return mapbox_map;
    }

    public void SetOnCameraStartListener(MapboxMap.OnCameraMoveStartedListener on_camera_start_move_listener) {
        mapbox_map.addOnCameraMoveStartedListener(on_camera_start_move_listener);
    }

    public void SetOnCameraIdleListener(MapboxMap.OnCameraIdleListener on_camera_idle_listener) {
        mapbox_map.addOnCameraIdleListener(on_camera_idle_listener);
    }

    @SuppressLint("MissingPermission")
    public void StartTrackingMode() {
        LocationComponent locationComponent = mapbox_map.getLocationComponent();
        ChangeCameraToLocationWithAnimation(3000);
        locationComponent.setCameraMode(CameraMode.TRACKING_COMPASS, new OnLocationCameraTransitionListener() {
            @Override
            public void onLocationCameraTransitionFinished(int cameraMode) {
                if (cameraMode != CameraMode.NONE) {
                    locationComponent.zoomWhileTracking(16, 750, new MapboxMap.CancelableCallback() {
                        @Override
                        public void onCancel() {
                            // No impl
                        }

                        @Override
                        public void onFinish() {
                            locationComponent.tiltWhileTracking(45);
                        }
                    });
                } else {
                    mapbox_map.easeCamera(CameraUpdateFactory.tiltTo(0));
                }
            }

            @Override
            public void onLocationCameraTransitionCanceled(int cameraMode) {

            }
        });
        locationComponent.setRenderMode(RenderMode.COMPASS);
    }

    public static boolean CheckPermissionUserLocation(Context context) {
        return PermissionsManager.areLocationPermissionsGranted(context);
    }

    /**
     * must be called before use mapboxmap
     *
     * @param context the context of the activity
     * @param token   The string of user access token
     */
    public static void MapBoxInstance(Context context, String token) {
        Mapbox.getInstance(context, token);
    }

    /**
     * Custom map constructor
     *
     * @param context               context
     * @param map_view              view component
     * @param style_id              style id or style url
     * @param mapBoxServiceCallback callback when map is finish construct
     */
    public MapBoxService(Context context, MapView map_view, String style_id, MapBoxServiceCallback mapBoxServiceCallback) {
        this.context = context;
        List< Feature > symbolLayerIconFeatureList = new ArrayList<>();
        symbolLayerIconFeatureList.add(Feature.fromGeometry(
                Point.fromLngLat(-57.225365, -33.213144)));
        symbolLayerIconFeatureList.add(Feature.fromGeometry(
                Point.fromLngLat(-54.14164, -33.981818)));
        symbolLayerIconFeatureList.add(Feature.fromGeometry(
                Point.fromLngLat(-56.990533, -30.583266)));
        map_view.getMapAsync(mapbox_map -> {
            this.mapbox_map = mapbox_map;
            mapbox_map.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/cjf4m44iw0uza2spb3q0a7s41")
                    .withImage("ICON_ID", BitmapFactory.decodeResource(
                            context.getResources(), R.drawable.location_pin))

                    // Adding a GeoJson source for the SymbolLayer icons.
                    .withSource(new GeoJsonSource("SOURCE_ID",
                            FeatureCollection.fromFeatures(symbolLayerIconFeatureList)))

                    // Adding the actual SymbolLayer to the map style. An offset is added that the bottom of the red
                    // marker icon gets fixed to the coordinate, rather than the middle of the icon being fixed to
                    // the coordinate point. This is offset is not always needed and is dependent on the image
                    // that you use for the SymbolLayer icon.
                    .withLayer(new SymbolLayer("LAYER_ID", "SOURCE_ID")
                            .withProperties(
                                    iconImage("ICON_ID"),
                                    iconAllowOverlap(true),
                                    iconIgnorePlacement(true),
                                    iconSize(0.5f)
                            )
                    )
            , mapBoxServiceCallback::OnStyleReadyCallback);
        });

    }

    /**
     * Activate tracking location
     */
    @SuppressLint("MissingPermission")
    public void ActivateLocationComponent() {
        LocationComponent location_component = mapbox_map.getLocationComponent();
        LocationComponentOptions custom_location_component_options = LocationComponentOptions.builder(context)
                .pulseEnabled(true)
                .build();
        LocationComponentActivationOptions options = LocationComponentActivationOptions
                .builder(context, mapbox_map.getStyle())
                .useDefaultLocationEngine(true)
                .locationComponentOptions(custom_location_component_options)
                .locationEngineRequest(new LocationEngineRequest.Builder(750).setFastestInterval(750).setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY).build()).build();

        location_component.activateLocationComponent(options);
        location_component.setLocationComponentEnabled(true);
        location_component.setCameraMode(CameraMode.TRACKING);
        location_component.setRenderMode(RenderMode.COMPASS);
        if (location_component.getLastKnownLocation() != null) {
            ChangeCameraToLocationWithAnimation(17, 180, 45, 3000);
        }
    }

    /**
     * @return user's last location
     */
    public Location GetLastLocationOfUser() {
        return mapbox_map.getLocationComponent().getLastKnownLocation();
    }

    public void ChangeCameraToLocationWithAnimation(int duration) {
        Location location = mapbox_map.getLocationComponent().getLastKnownLocation();
        _ChangeCameraToLocationWithAnimation(location, 17, 180, 45, duration);
    }

    public void ChangeCameraToLocationWithAnimation(int zoom, int bearing, int tilt, int duration) {
        Location location = mapbox_map.getLocationComponent().getLastKnownLocation();
        _ChangeCameraToLocationWithAnimation(location, zoom, bearing, tilt, duration);
    }

    public void ChangeCameraToLocationWithAnimation(Location location, int zoom, int bearing, int tilt, int duration) {
        _ChangeCameraToLocationWithAnimation(location, zoom, bearing, tilt, duration);
    }

    private void _ChangeCameraToLocationWithAnimation(Location location, int zoom, int bearing, int tilt, int duration) {
        double current_lat = location.getLatitude();
        double current_lng = location.getLongitude();
        LatLng latLng = new LatLng(current_lat, current_lng);
        CameraPosition position = new CameraPosition.Builder()
                .target(latLng) // Sets the new camera position
                .zoom(zoom) // Sets the zoom
                .bearing(bearing) // Rotate the camera
                .tilt(tilt) // Set the camera tilt
                .build(); // Creates a CameraPosition from the builder
        mapbox_map.animateCamera(CameraUpdateFactory.newCameraPosition(position), duration);
    }
}
