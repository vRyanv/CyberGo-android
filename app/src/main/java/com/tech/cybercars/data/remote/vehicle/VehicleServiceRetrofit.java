package com.tech.cybercars.data.remote.vehicle;

import com.tech.cybercars.constant.URL;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface VehicleServiceRetrofit {
    @GET(URL.VEHICLE_LIST)
    Call<ManyVehicleResponse> GetVehicleList(
            @Header("authorization") String user_token
    );

    @GET(URL.VEHICLE_ACCEPTED_LIST)
    Call<ManyVehicleResponse> GetVehicleAcceptedList(
            @Header("authorization") String user_token
    );
}
