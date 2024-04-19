package com.tech.cybercars.ui.main.fragment.go.find_trip.trip_found_detail;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.tech.cybercars.R;
import com.tech.cybercars.constant.DelayTime;
import com.tech.cybercars.constant.StatusCode;
import com.tech.cybercars.constant.Tag;
import com.tech.cybercars.data.models.TripFound;
import com.tech.cybercars.data.models.trip.Destination;
import com.tech.cybercars.data.models.trip.Trip;
import com.tech.cybercars.data.remote.base.BaseResponse;
import com.tech.cybercars.data.remote.retrofit.ResFailCallback;
import com.tech.cybercars.data.remote.retrofit.ResSuccessCallback;
import com.tech.cybercars.data.remote.trip.RequestToJoinBody;
import com.tech.cybercars.data.remote.trip.TripBodyAndResponse;
import com.tech.cybercars.data.repositories.TripRepository;
import com.tech.cybercars.ui.base.BaseViewModel;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripFoundDetailViewModel extends BaseViewModel {
    public MutableLiveData<TripFound> trip_found = new MutableLiveData<>();
    private final TripRepository trip_repo;
    public TripFoundDetailViewModel(@NonNull Application application) {
        super(application);
        trip_repo = TripRepository.GetInstance();
    }

    public void HandleRequestToJoin(){
        RequestToJoinBody request_to_join_body = new RequestToJoinBody();
        request_to_join_body.trip_id = trip_found.getValue().trip_id;


        String user_token = SharedPreferencesUtil.GetString(getApplication(),SharedPreferencesUtil.USER_TOKEN_KEY);
        trip_repo.MemberRequestToJoin(
                user_token,
                request_to_join_body,
                this::CallMemberRequestToJoinSuccess,
                this::CallMemberRequestToJoinFailed
        );
    }

    private void CallMemberRequestToJoinSuccess(Response<BaseResponse> response) {
        new Handler().postDelayed(() -> {
            if (!response.isSuccessful() || response.body() == null) {
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
                Log.e(Tag.CYBER_DEBUG, response.message() );
                is_loading.postValue(false);
                return;
            }
            if (response.body().code == StatusCode.CREATED) {

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

}
