package com.tech.cybercars.ui.main.fragment.trip.trip_detail;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.DelayTime;
import com.tech.cybercars.constant.MemberStatus;
import com.tech.cybercars.constant.StatusCode;
import com.tech.cybercars.constant.Tag;
import com.tech.cybercars.data.models.TripManagement;
import com.tech.cybercars.data.remote.base.BaseResponse;
import com.tech.cybercars.data.remote.trip.MemberLeaveTripBody;
import com.tech.cybercars.data.remote.trip.UpdateTripResponse;
import com.tech.cybercars.data.remote.trip.member_status.UpdateMemberStatusBody;
import com.tech.cybercars.data.remote.trip.member_status.UpdateMemberStatusResponse;
import com.tech.cybercars.data.repositories.TripRepository;
import com.tech.cybercars.services.eventbus.ActionEvent;
import com.tech.cybercars.services.eventbus.UpdateTripInformationEvent;
import com.tech.cybercars.ui.base.BaseViewModel;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class TripDetailViewModel extends BaseViewModel {
    public MutableLiveData<TripManagement> trip_management = new MutableLiveData<>();
    public MutableLiveData<Boolean> can_perform_trip = new MutableLiveData<>();
    public MutableLiveData<Boolean> is_leave_trip_success = new MutableLiveData<>();
    public MutableLiveData<Boolean> is_delete_trip_success = new MutableLiveData<>();
    public TripRepository trip_repo;

    public TripDetailViewModel(@NonNull Application application) {
        super(application);
        trip_repo = TripRepository.GetInstance();
    }

    public void HandleDeleteTrip(){
        String trip_id = trip_management.getValue().trip_id;
        String user_token = SharedPreferencesUtil.GetString(getApplication(), SharedPreferencesUtil.USER_TOKEN_KEY);
        trip_repo.RemoveTrip(
                user_token,
                trip_id,
                this::CallDeleteTripSuccess,
                this::CallDeleteTripFailed
        );
    }

    private void CallDeleteTripSuccess(Response<BaseResponse> response) {
        new Handler().postDelayed(() -> {
            if (!response.isSuccessful() || response.body() == null) {
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
                is_loading.postValue(false);
                return;
            }
            if (response.body().code == StatusCode.DELETED) {
                is_delete_trip_success.postValue(true);
                EventBus.getDefault().post(new ActionEvent(ActionEvent.REFRESH_TRIP_LIST));
            } else if (response.body().code == StatusCode.BAD_REQUEST) {
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
            }
            is_loading.postValue(false);
        }, DelayTime.CALL_API);
    }


    private void CallDeleteTripFailed(Throwable error) {
        is_loading.postValue(false);
        error_call_server.postValue(getApplication().getString(R.string.can_not_connect_to_server));
        Log.e(Tag.CYBER_DEBUG, "error call server: " + error.getMessage());
    }

    public void HandleLeaveTrip(String trip_id, TripManagement.Member member) {
        String user_token = SharedPreferencesUtil.GetString(getApplication(), SharedPreferencesUtil.USER_TOKEN_KEY);
        MemberLeaveTripBody mem_leave_trip_body = new MemberLeaveTripBody(trip_id, member.member_id, member.full_name, member.avatar);
        trip_repo.MemberLeaveTrip(
                user_token,
                mem_leave_trip_body,
                this::CallMemberLeaveTripSuccess,
                this::CallMemberLeaveTripFailed
        );
    }

    private void CallMemberLeaveTripSuccess(Response<BaseResponse> response) {
        new Handler().postDelayed(() -> {
            if (!response.isSuccessful() || response.body() == null) {
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
                is_loading.postValue(false);
                return;
            }
            if (response.body().code == StatusCode.UPDATED) {
                is_leave_trip_success.postValue(true);
                EventBus.getDefault().post(new ActionEvent(ActionEvent.REFRESH_TRIP_LIST));
            } else if (response.body().code == StatusCode.BAD_REQUEST) {
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
            }
            is_loading.postValue(false);
        }, DelayTime.CALL_API);
    }


    private void CallMemberLeaveTripFailed(Throwable error) {
        is_loading.postValue(false);
        error_call_server.postValue(getApplication().getString(R.string.can_not_connect_to_server));
        Log.e(Tag.CYBER_DEBUG, "error call server: " + error.getMessage());
    }

    public void HandleMemberRequestJoin(String member_id, String user_id, String status) {
        is_loading.setValue(true);
        String user_token = SharedPreferencesUtil.GetString(getApplication(), SharedPreferencesUtil.USER_TOKEN_KEY);
        String trip_id = trip_management.getValue().trip_id;
        UpdateMemberStatusBody.Member member = new UpdateMemberStatusBody.Member();
        member.member_id = member_id;
        member.user_id = user_id;

        UpdateMemberStatusBody update_member_status_body = new UpdateMemberStatusBody(
                trip_id,
                member,
                status
        );

        trip_repo.UpdateMemberStatus(
                user_token,
                update_member_status_body,
                this::CallUpdateMemberStatusSuccess,
                this::CallUpdateMemberStatusFailed
        );
    }

    private void CallUpdateMemberStatusSuccess(Response<UpdateMemberStatusResponse> response) {
        new Handler().postDelayed(() -> {
            if (!response.isSuccessful() || response.body() == null) {
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
                is_loading.postValue(false);
                return;
            }
            if (response.body().code == StatusCode.UPDATED) {
                ExecutorService executor_service = Executors.newSingleThreadExecutor();
                executor_service.execute(() -> {
                    List<TripManagement.Member> members = trip_management.getValue().members;
                    String update_member_id = response.body().member_id;
                    String update_status = response.body().status;
                    for (TripManagement.Member member : members) {
                        if (member.member_id.equals(update_member_id)) {
                            if (update_status.equals(MemberStatus.JOINED)) {
                                member.status = MemberStatus.JOINED;
                            } else {
                                members.remove(member);
                            }
                            break;
                        }
                    }

                    TripManagement temp_trip = trip_management.getValue();
                    trip_management.postValue(null);
                    trip_management.postValue(temp_trip);
                    EventBus.getDefault().post(new ActionEvent(ActionEvent.REFRESH_TRIP_LIST));
                });
                executor_service.shutdown();

            } else if (response.body().code == StatusCode.BAD_REQUEST) {
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
            }
            is_loading.postValue(false);
        }, DelayTime.CALL_API);
    }


    private void CallUpdateMemberStatusFailed(Throwable error) {
        is_loading.postValue(false);
        error_call_server.postValue(getApplication().getString(R.string.can_not_connect_to_server));
        Log.e(Tag.CYBER_DEBUG, "error call server: " + error.getMessage());
    }

    public void UpdateTripStatus(String trip_status) {
        is_loading.setValue(true);
        String user_token = SharedPreferencesUtil.GetString(getApplication(), SharedPreferencesUtil.USER_TOKEN_KEY);
        String trip_id = trip_management.getValue().trip_id;
        trip_repo.UpdateTripStatus(
                user_token,
                trip_id,
                trip_status,
                this::CallUpdateTripStatusSuccess,
                this::CallUpdateTripStatusFailed
        );
    }

    private void CallUpdateTripStatusSuccess(Response<UpdateTripResponse> response) {
        new Handler().postDelayed(() -> {
            if (!response.isSuccessful() || response.body() == null) {
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
                is_loading.postValue(false);
                return;
            }
            if (response.body().code == StatusCode.OK) {
                trip_management.getValue().trip_status = response.body().status;
                TripManagement temp_trip = trip_management.getValue();
                trip_management.setValue(null);
                trip_management.setValue(temp_trip);
                EventBus.getDefault().post(new UpdateTripInformationEvent(temp_trip));
            } else if (response.body().code == StatusCode.NOT_FOUND) {
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
            }
            is_loading.postValue(false);
        }, DelayTime.CALL_API);
    }


    private void CallUpdateTripStatusFailed(Throwable error) {
        is_loading.postValue(false);
        error_call_server.postValue(getApplication().getString(R.string.can_not_connect_to_server));
        Log.e(Tag.CYBER_DEBUG, "error call server: " + error.getMessage());
    }

    public void UpdateTripFinish(String status) {
        trip_management.getValue().trip_status = status;
        TripManagement temp_trip = trip_management.getValue();
        trip_management.setValue(null);
        trip_management.setValue(temp_trip);
    }
}
