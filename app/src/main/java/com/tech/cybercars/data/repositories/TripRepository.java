package com.tech.cybercars.data.repositories;

import com.tech.cybercars.data.models.trip.Trip;
import com.tech.cybercars.data.remote.retrofit.ResFailCallback;
import com.tech.cybercars.data.remote.retrofit.ResSuccessCallback;
import com.tech.cybercars.data.remote.retrofit.RetrofitRequest;
import com.tech.cybercars.data.remote.retrofit.RetrofitResponse;
import com.tech.cybercars.data.remote.trip.TripBodyAndResponse;
import com.tech.cybercars.data.remote.trip.TripServiceRetrofit;

public class TripRepository {
    private final TripServiceRetrofit trip_service;
    private static TripRepository trip_repository;

    public static TripRepository GetInstance() {
        if (trip_repository == null) {
            trip_repository = new TripRepository();
        }
        return trip_repository;
    }

    private TripRepository() {
        trip_service = RetrofitRequest.getInstance().create(TripServiceRetrofit.class);
    }

    public void CreateTripSharing(String user_token, TripBodyAndResponse trip_body, ResSuccessCallback<TripBodyAndResponse> success_callback, ResFailCallback fail_callback){
        trip_service.CreateTripSharingRequest(user_token, trip_body)
                .enqueue(new RetrofitResponse<TripBodyAndResponse>().GetResponse(success_callback, fail_callback));
    }

}
