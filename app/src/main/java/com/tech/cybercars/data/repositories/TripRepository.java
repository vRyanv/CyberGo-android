package com.tech.cybercars.data.repositories;

import com.tech.cybercars.data.remote.trip.TripManagementResponse;
import com.tech.cybercars.data.remote.trip.member_status.UpdateMemberStatusBody;
import com.tech.cybercars.data.remote.trip.UpdateTripInformationBody;
import com.tech.cybercars.data.remote.trip.UpdateTripLocationBody;
import com.tech.cybercars.data.remote.trip.UpdateTripResponse;
import com.tech.cybercars.data.remote.trip.find_trip.MemberBody;
import com.tech.cybercars.data.remote.base.BaseResponse;
import com.tech.cybercars.data.remote.retrofit.ResFailCallback;
import com.tech.cybercars.data.remote.retrofit.ResSuccessCallback;
import com.tech.cybercars.data.remote.retrofit.RetrofitRequest;
import com.tech.cybercars.data.remote.retrofit.RetrofitResponse;
import com.tech.cybercars.data.remote.trip.TripBodyAndResponse;
import com.tech.cybercars.data.remote.trip.TripServiceRetrofit;
import com.tech.cybercars.data.remote.trip.find_trip.FindTripBody;
import com.tech.cybercars.data.remote.trip.find_trip.FindTripResponse;
import com.tech.cybercars.data.remote.trip.member_status.UpdateMemberStatusResponse;

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
    public void UpdateMemberStatus(String user_token, UpdateMemberStatusBody update_member_status_body, ResSuccessCallback<UpdateMemberStatusResponse> success_callback, ResFailCallback fail_callback){
        trip_service.UpdateMemberStatusRequest(user_token, update_member_status_body)
                .enqueue(new RetrofitResponse<UpdateMemberStatusResponse>().GetResponse(success_callback, fail_callback));
    }

    public void UpdateTripLocation(String user_token, UpdateTripLocationBody update_trip_location_body, ResSuccessCallback<BaseResponse> success_callback, ResFailCallback fail_callback){
        trip_service.UpdateTripLocationRequest(user_token, update_trip_location_body)
                .enqueue(new RetrofitResponse<BaseResponse>().GetResponse(success_callback, fail_callback));
    }
    public void UpdateTripInformation(String user_token, UpdateTripInformationBody update_trip_info_body, ResSuccessCallback<BaseResponse> success_callback, ResFailCallback fail_callback){
        trip_service.UpdateTripInformationRequest(user_token, update_trip_info_body)
                .enqueue(new RetrofitResponse<BaseResponse>().GetResponse(success_callback, fail_callback));
    }

    public void UpdateTripStatus(String user_token, String trip_id, String trip_status, ResSuccessCallback<UpdateTripResponse> success_callback, ResFailCallback fail_callback){
        trip_service.UpdateStatusRequest(user_token, trip_id, trip_status)
                .enqueue(new RetrofitResponse<UpdateTripResponse>().GetResponse(success_callback, fail_callback));
    }

    public void GetTripManagement(String user_token, ResSuccessCallback<TripManagementResponse> success_callback, ResFailCallback fail_callback){
        trip_service.GetTripListRequest(user_token)
                .enqueue(new RetrofitResponse<TripManagementResponse>().GetResponse(success_callback, fail_callback));
    }
    public void CreateTripSharing(String user_token, TripBodyAndResponse trip_body, ResSuccessCallback<TripBodyAndResponse> success_callback, ResFailCallback fail_callback){
        trip_service.CreateTripSharingRequest(user_token, trip_body)
                .enqueue(new RetrofitResponse<TripBodyAndResponse>().GetResponse(success_callback, fail_callback));
    }

    public void PassengerFindTrip(String user_token, FindTripBody find_trip_body, ResSuccessCallback<FindTripResponse> success_callback, ResFailCallback fail_callback){
        trip_service.PassengerFindTripRequest(user_token, find_trip_body)
                .enqueue(new RetrofitResponse<FindTripResponse>().GetResponse(success_callback, fail_callback));
    }

    public void MemberRequestToJoin(String user_token, MemberBody member, ResSuccessCallback<TripBodyAndResponse> success_callback, ResFailCallback fail_callback){
        trip_service.MemberRequestToJoinRequest(user_token, member)
                .enqueue(new RetrofitResponse<TripBodyAndResponse>().GetResponse(success_callback, fail_callback));
    }

}
