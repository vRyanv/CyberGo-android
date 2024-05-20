package com.tech.cybercars.data.remote.vehicle;

import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.URL;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface VehicleServiceRetrofit {
    @DELETE(URL.DELETE_VEHICLE)
    Call<DeleteVehicleResponse> DeleteVehicleRequest(
            @Header("authorization") String user_token,
            @Path(FieldName.VEHICLE_ID) String vehicle_id
    );
    @GET(URL.VEHICLE_LIST)
    Call<ManyVehicleResponse> GetVehicleList(
            @Header("authorization") String user_token
    );

    @GET(URL.VEHICLE_ACCEPTED_LIST)
    Call<ManyVehicleResponse> GetVehicleAcceptedList(
            @Header("authorization") String user_token
    );
}
