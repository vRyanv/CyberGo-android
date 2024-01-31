package com.tech.cybercars.services.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationManager;
import android.os.Looper;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;

public class LocationService {
    private LocationEngine location_engine;
    private LocationEngineCallback<LocationEngineResult> callback;
    public LocationService(Context context){
        location_engine = LocationEngineProvider.getBestLocationEngine(context);
    }
    @SuppressLint("MissingPermission")
    public void Start(){
        LocationEngineRequest request = new LocationEngineRequest.Builder(1000) // update every 1000ms
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(5000) //waiting max time is 5000ms for get new location
                .build();
        location_engine.requestLocationUpdates(request, callback, Looper.getMainLooper());
    }
    public LocationService SetCallback(LocationEngineCallback<LocationEngineResult> callback){
        this.callback = callback;
        return this;
    }

    public void Stop(){
        location_engine.removeLocationUpdates(callback);
    }
    public static boolean IsGPSActivated(Context context){
        LocationManager location_manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return location_manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
