package com.tech.cybercars.ui.main.fragment.go.find_trip.trip_found_detail;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.DelayTime;
import com.tech.cybercars.constant.StatusCode;
import com.tech.cybercars.constant.Tag;
import com.tech.cybercars.data.local.AppDBContext;
import com.tech.cybercars.data.local.trip.MemberDAO;
import com.tech.cybercars.data.local.trip.TripDAO;
import com.tech.cybercars.data.models.Member;
import com.tech.cybercars.data.models.trip.Trip;
import com.tech.cybercars.data.remote.trip.TripBodyAndResponse;
import com.tech.cybercars.data.remote.trip.find_trip.MemberBody;
import com.tech.cybercars.data.models.TripFound;
import com.tech.cybercars.data.remote.base.BaseResponse;
import com.tech.cybercars.data.repositories.TripRepository;
import com.tech.cybercars.ui.base.BaseViewModel;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class TripFoundDetailViewModel extends BaseViewModel {
    public MutableLiveData<TripFound> trip_found = new MutableLiveData<>();
    public MemberBody member;
    private final TripRepository trip_repo;
    private MemberDAO member_dao;
    private TripDAO trip_dao;
    public TripFoundDetailViewModel(@NonNull Application application) {
        super(application);
        trip_repo = TripRepository.GetInstance();
        member_dao = AppDBContext.GetInstance(application).MemberDAO();
        trip_dao = AppDBContext.GetInstance(application).TripDAO();
    }

    public void HandleRequestToJoin(){
        is_loading.setValue(true);
        String user_token = SharedPreferencesUtil.GetString(getApplication(),SharedPreferencesUtil.USER_TOKEN_KEY);
        trip_repo.MemberRequestToJoin(
                user_token,
                member,
                this::CallMemberRequestToJoinSuccess,
                this::CallMemberRequestToJoinFailed
        );
    }

    private void CallMemberRequestToJoinSuccess(Response<TripBodyAndResponse> response) {
        new Handler().postDelayed(() -> {
            if (!response.isSuccessful() || response.body() == null) {
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
                Log.e(Tag.CYBER_DEBUG, response.message() );
                is_loading.postValue(false);
                return;
            }
            if (response.body().code == StatusCode.OK) {
                is_success.postValue(true);
                ExecutorService executor_service = Executors.newSingleThreadExecutor();
                executor_service.execute(() -> {
                    SaveTripData(response.body().trip);
                });
                executor_service.shutdown();
            } else if (response.body().code == StatusCode.BAD_REQUEST) {
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
            }
            is_loading.postValue(false);
        }, DelayTime.CALL_API);
    }

    private void CallMemberRequestToJoinFailed(Throwable error) {
        is_loading.postValue(false);
        Log.e(Tag.CYBER_DEBUG, error.getMessage() );
        error_call_server.postValue(getApplication().getString(R.string.can_not_connect_to_server));
    }

    private void SaveTripData(Trip trip){
        trip_dao.InsertTrip(trip);

        List<Member> members = new ArrayList<>();
        for (MemberBody member_body:trip.members) {
            Member member = new Member(
                    member_body.member_id,
                    member_body.trip_id,
                    member_body.user_id,
                    member_body.origin.longitude,
                    member_body.origin.latitude,
                    member_body.destination.longitude,
                    member_body.destination.latitude,
                    member_body.origin.address,
                    member_body.destination.address,
                    member_body.geometry,
                    member_body.request_at,
                    member_body.status
            );
            members.add(member);
        }
        member_dao.InsertMembers(members);
    }

}
