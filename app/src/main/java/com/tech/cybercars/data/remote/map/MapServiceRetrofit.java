package com.tech.cybercars.data.remote.map;

import com.tech.cybercars.constant.URL;
import com.tech.cybercars.data.remote.map.reverse_geocoding.ReverseGeocodingResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MapServiceRetrofit {
    @GET(URL.MAP_REVERSE_GEOCODING)
    Call<ReverseGeocodingResponse> ReverseGeocodingRequest(@Path("lat") double lat, @Path("lng") double lng);

    @GET(URL.SEARCH_ADDRESS)
    Call<List<ReverseGeocodingResponse>> SearchAddress(@Path("address") String address, @Path("limit") int limit);
}
