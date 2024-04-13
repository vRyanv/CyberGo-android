package com.tech.cybercars.data.remote.trip;

import com.tech.cybercars.constant.URL;
import com.tech.cybercars.data.models.trip.Destination;
import com.tech.cybercars.data.models.trip.Trip;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface TripServiceRetrofit {
    @POST(URL.CREATE_TRIP)
    Call<TripBodyAndResponse> CreateTripSharingRequest(
            @Header("authorization") String user_token,
            @Body TripBodyAndResponse trip_body
    );
}
