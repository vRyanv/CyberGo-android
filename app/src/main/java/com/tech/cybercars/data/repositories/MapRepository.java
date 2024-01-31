package com.tech.cybercars.data.repositories;

import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.tech.cybercars.data.remote.api.ResFailCallback;
import com.tech.cybercars.data.remote.api.ResSuccessCallback;
import com.tech.cybercars.data.remote.api.RetrofitRequest;
import com.tech.cybercars.data.remote.api.RetrofitResponse;
import com.tech.cybercars.data.remote.map.MapServiceRetrofit;
import com.tech.cybercars.data.remote.map.reverse_geocoding.ReverseGeocodingResponse;

import java.util.List;

import retrofit2.Callback;

public class MapRepository {
    private MapServiceRetrofit map_service_retrofit;
    private static MapRepository map_repository;
    public static MapRepository GetInstance(){
        if(map_repository == null){
            map_repository = new MapRepository();
        }
        return map_repository;
    }

    private MapRepository(){
        map_service_retrofit = RetrofitRequest.getInstance().create(MapServiceRetrofit.class);
    }

    public void FindAddress(LatLng lat_lng,ResSuccessCallback<ReverseGeocodingResponse> success_callback, ResFailCallback fail_callback) {
        map_service_retrofit.ReverseGeocodingRequest(lat_lng.getLatitude(), lat_lng.getLongitude()).enqueue(
                new RetrofitResponse<ReverseGeocodingResponse>().GetResponse(success_callback, fail_callback)
        );
    }

    public void SearchAddress(String address, int limit, ResSuccessCallback<List<ReverseGeocodingResponse>> success_callback, ResFailCallback fail_callback) {
        map_service_retrofit.SearchAddress(address, limit).enqueue(
                new RetrofitResponse<List<ReverseGeocodingResponse>>().GetResponse(success_callback, fail_callback)
        );
    }
}
