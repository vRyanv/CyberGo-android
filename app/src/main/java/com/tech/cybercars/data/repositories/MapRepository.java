package com.tech.cybercars.data.repositories;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.tech.cybercars.data.remote.retrofit.ResFailCallback;
import com.tech.cybercars.data.remote.retrofit.ResSuccessCallback;
import com.tech.cybercars.data.remote.retrofit.RetrofitRequest;
import com.tech.cybercars.data.remote.retrofit.RetrofitResponse;
import com.tech.cybercars.data.remote.map.MapServiceRetrofit;
import com.tech.cybercars.data.remote.map.reverse_geocoding.ReverseGeocodingResponse;

import java.util.List;

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
