package com.tech.cybercars.data.repositories;

import com.tech.cybercars.data.remote.retrofit.ResFailCallback;
import com.tech.cybercars.data.remote.retrofit.ResSuccessCallback;
import com.tech.cybercars.data.remote.retrofit.RetrofitRequest;
import com.tech.cybercars.data.remote.retrofit.RetrofitResponse;
import com.tech.cybercars.data.remote.vehicle.DeleteVehicleResponse;
import com.tech.cybercars.data.remote.vehicle.ManyVehicleResponse;
import com.tech.cybercars.data.remote.vehicle.VehicleServiceRetrofit;

public class VehicleRepository {
    private final VehicleServiceRetrofit vehicle_service;
    private static VehicleRepository vehicle_repository;

    public static VehicleRepository GetInstance() {
        if (vehicle_repository == null) {
            vehicle_repository = new VehicleRepository();
        }
        return vehicle_repository;
    }

    private VehicleRepository() {
        vehicle_service = RetrofitRequest.getInstance().create(VehicleServiceRetrofit.class);
    }

    public void DeleteVehicle(String user_token, String vehicle_id, ResSuccessCallback<DeleteVehicleResponse> success_callback, ResFailCallback fail_callback){
        vehicle_service.DeleteVehicleRequest(user_token, vehicle_id).enqueue(
                new RetrofitResponse<DeleteVehicleResponse>().GetResponse(success_callback, fail_callback)
        );
    }

    public void GetVehicleList(String user_token, ResSuccessCallback<ManyVehicleResponse> success_callback, ResFailCallback fail_callback){
        vehicle_service.GetVehicleList(user_token).enqueue(
                new RetrofitResponse<ManyVehicleResponse>().GetResponse(success_callback, fail_callback)
        );
    }

    public void GetVehicleAcceptedList(String user_token, ResSuccessCallback<ManyVehicleResponse> success_callback, ResFailCallback fail_callback){
        vehicle_service.GetVehicleAcceptedList(user_token).enqueue(
                new RetrofitResponse<ManyVehicleResponse>().GetResponse(success_callback, fail_callback)
        );
    }
}
