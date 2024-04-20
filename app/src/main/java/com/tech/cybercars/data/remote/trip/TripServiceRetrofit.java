package com.tech.cybercars.data.remote.trip;

import com.tech.cybercars.constant.URL;
import com.tech.cybercars.data.remote.base.BaseResponse;
import com.tech.cybercars.data.remote.trip.find_trip.FindTripBody;
import com.tech.cybercars.data.remote.trip.find_trip.FindTripResponse;
import com.tech.cybercars.data.remote.trip.find_trip.MemberBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface TripServiceRetrofit {
    @POST(URL.MEMBER_REQUEST_TO_JOIN)
    Call<TripBodyAndResponse> MemberRequestToJoinRequest(
            @Header("authorization") String user_token,
            @Body MemberBody member
    );

    @POST(URL.CREATE_TRIP)
    Call<TripBodyAndResponse> CreateTripSharingRequest(
            @Header("authorization") String user_token,
            @Body TripBodyAndResponse trip_body
    );

    @POST(URL.PASSENGER_FIND_TRIP)
    Call<FindTripResponse> PassengerFindTripRequest(
            @Header("authorization") String user_token,
            @Body FindTripBody trip_body
    );
}
