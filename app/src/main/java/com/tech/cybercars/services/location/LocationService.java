package com.tech.cybercars.services.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class LocationService {
    private LocationManager location_manager;
    public static boolean IsGPSActivated(Context context){
        LocationManager location_manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return location_manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public LocationService(Context context){
        location_manager =  (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);;
    }

    @SuppressLint("MissingPermission")
    public void SetOnLocationChange(LocationCallback.Change on_changed_callback, LocationCallback.Provider provider_callback, LocationCallback.Status status_changed_callback){
        LocationListener location_listener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                on_changed_callback.OnChanged(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // Xử lý khi trạng thái của nguồn cung cấp vị trí thay đổi
                status_changed_callback.OnChange(provider, status, extras);
            }

            @Override
            public void onProviderEnabled(String provider) {
                // Xử lý khi nguồn cung cấp vị trí được bật
                provider_callback.OnEnabled(provider);
            }

            @Override
            public void onProviderDisabled(String provider) {
                // Xử lý khi nguồn cung cấp vị trí bị tắt
                provider_callback.OnDisabled(provider);
            }
        };

        location_manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, location_listener);
    }



}
