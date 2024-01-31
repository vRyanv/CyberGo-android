package com.tech.cybercars.data.remote.map;

import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.tech.cybercars.data.remote.map.reverse_geocoding.ReverseGeocodingResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface MapServiceRetrofit {
    @GET("/map/reverse-geocoding/{lat}/{lng}")
    Call<ReverseGeocodingResponse> ReverseGeocodingRequest(@Path("lat") double lat, @Path("lng") double lng);

    @GET("/map/search-address/{address}/{limit}")
    Call<List<ReverseGeocodingResponse>> SearchAddress(@Path("address") String address, @Path("limit") int limit);
}
