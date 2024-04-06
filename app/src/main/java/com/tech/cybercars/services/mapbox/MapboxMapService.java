package com.tech.cybercars.services.mapbox;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;

import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.permissions.PermissionsManager;
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

public class MapboxMapService {
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
    public void StartTrackingMode(MapboxMap.CancelableCallback callback) {
        LocationComponent locationComponent = mapbox_map.getLocationComponent();
        ChangeCameraToLocationWithAnimation(3000, callback);
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
     * @param style_id              style id
     * @param mapBoxServiceCallback callback when map is finish construct
     */
    public MapboxMapService(Context context, MapView map_view, String style_id, MapboxMapServiceCallback mapBoxServiceCallback) {
        this.context = context;
        map_view.getMapAsync(mapbox_map -> {
            this.mapbox_map = mapbox_map;
            mapbox_map.setStyle(style_id, mapBoxServiceCallback::OnStyleReadyCallback);
        });
    }

    /**
     * Activate tracking location
     */
    @SuppressLint("MissingPermission")
    public void ActivateLocationComponent(MapboxMap.CancelableCallback callback) {
        LocationComponent location_component = mapbox_map.getLocationComponent();
        LocationComponentOptions custom_location_component_options = LocationComponentOptions.builder(context)
                .pulseEnabled(true)
                .build();
        LocationComponentActivationOptions options = LocationComponentActivationOptions
                .builder(context, mapbox_map.getStyle())
                .useDefaultLocationEngine(true)
                .locationComponentOptions(custom_location_component_options)
                .locationEngineRequest(
                        new LocationEngineRequest.Builder(750)
                                .setFastestInterval(750)
                                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                                .build()
                ).build();

        location_component.activateLocationComponent(options);
        location_component.setLocationComponentEnabled(true);
        location_component.setCameraMode(CameraMode.TRACKING);
        location_component.setRenderMode(RenderMode.COMPASS);
        if (location_component.getLastKnownLocation() != null) {
            ChangeCameraToLocationWithAnimation(15, 180, 0, 4000, callback);
        }
    }

    /**
     * @return user's last location
     */
    public Location GetLastLocationOfUser() {
        return mapbox_map.getLocationComponent().getLastKnownLocation();
    }

    public void ChangeCameraToLocationWithAnimation(LatLng lat_lng, int duration, int bearing, MapboxMap.CancelableCallback callback) {
        _ChangeCameraToLocationWithAnimation(lat_lng, 15, bearing, 45, duration, callback);
    }

    public void ChangeCameraToLocationWithAnimation(int duration, MapboxMap.CancelableCallback callback) {
        Location location = mapbox_map.getLocationComponent().getLastKnownLocation();
        LatLng lat_lng = new LatLng(location.getLatitude(), location.getLongitude());
        _ChangeCameraToLocationWithAnimation(lat_lng, 17, 180, 45, duration, callback);
    }

    public void ChangeCameraToLocationWithAnimation(int zoom, int bearing, int tilt, int duration, MapboxMap.CancelableCallback callback) {
        Location location = mapbox_map.getLocationComponent().getLastKnownLocation();
        LatLng lat_lng = new LatLng(location.getLatitude(), location.getLongitude());
        _ChangeCameraToLocationWithAnimation(lat_lng, zoom, bearing, tilt, duration, callback);
    }

    public void ChangeCameraToLocationWithAnimation(LatLng lat_lng, int zoom, int bearing, int tilt, int duration, MapboxMap.CancelableCallback callback) {
        _ChangeCameraToLocationWithAnimation(lat_lng, zoom, bearing, tilt, duration, callback);
    }

    private void _ChangeCameraToLocationWithAnimation(LatLng lat_lng, int zoom, int bearing, int tilt, int duration, MapboxMap.CancelableCallback callback) {
        CameraPosition position = new CameraPosition.Builder()
                .target(lat_lng) // Sets the new camera position
                .zoom(zoom) // Sets the zoom
                .bearing(bearing) // Rotate the camera
                .tilt(tilt) // Set the camera tilt
                .build(); // Creates a CameraPosition from the builder
        mapbox_map.animateCamera(CameraUpdateFactory.newCameraPosition(position), duration, callback);
    }
}
