package com.tech.cybercars.data.remote.trip;

import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.data.models.TripManagement;
import com.tech.cybercars.data.remote.base.BaseResponse;
import com.tech.cybercars.data.remote.trip.find_trip.FindTripBody;
import com.tech.cybercars.data.remote.trip.find_trip.FindTripResponse;
import com.tech.cybercars.data.remote.trip.find_trip.MemberBody;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface TripServiceRetrofit {

    @FormUrlEncoded
    @POST(URL.UPDATE_TRIP_STATUS)
    Call<UpdateTripResponse> UpdateStatusRequest(
            @Header("authorization") String user_token,
            @Field(FieldName.TRIP_ID) String trip_id,
            @Field(FieldName.STATUS) String trip_status
    );
    @GET(URL.TRIP_LIST)
    Call<TripManagementResponse> GetTripListRequest(@Header("authorization") String user_token);

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
