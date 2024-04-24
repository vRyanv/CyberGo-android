package com.tech.cybercars.ui.main.fragment.trip;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.DelayTime;
import com.tech.cybercars.constant.StatusCode;
import com.tech.cybercars.constant.Tag;
import com.tech.cybercars.data.models.TripManagement;
import com.tech.cybercars.data.models.User;
import com.tech.cybercars.data.models.trip.Trip;
import com.tech.cybercars.data.remote.retrofit.ResFailCallback;
import com.tech.cybercars.data.remote.retrofit.ResSuccessCallback;
import com.tech.cybercars.data.remote.trip.TripManagementResponse;
import com.tech.cybercars.data.remote.user.profile.ProfileResponse;
import com.tech.cybercars.data.repositories.TripRepository;
import com.tech.cybercars.ui.base.BaseViewModel;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class TripViewModel extends BaseViewModel {
    public MutableLiveData<List<TripManagement>> joined_trip_list = new MutableLiveData<>();
    public MutableLiveData<List<TripManagement>> shared_trip_list = new MutableLiveData<>();
    private TripRepository trip_repo;
    public TripViewModel(@NonNull Application application) {
        super(application);
        trip_repo = TripRepository.GetInstance();
    }

    public void HandleGetTripList(){
        is_loading.setValue(true);
        String user_token = SharedPreferencesUtil.GetString(getApplication(), SharedPreferencesUtil.USER_TOKEN_KEY);

        trip_repo.GetTripManagement(
                user_token,
                this::CallGetTripListSuccess,
                this::CallGetTripListFailed
        );
    }

    private void CallGetTripListSuccess(Response<TripManagementResponse> response) {
        new Handler().postDelayed(() -> {
            if(!response.isSuccessful() || response.body() == null){
                Log.e(Tag.CYBER_DEBUG, "response.message()" + response.message());
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
                is_loading.postValue(false);
                return;
            }
            if (response.body().code == StatusCode.OK) {
                shared_trip_list.postValue(response.body().shared_trip_list);
                joined_trip_list.postValue(response.body().join_trip_list);
            } else {
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
            }
            is_loading.postValue(false);
        }, DelayTime.CALL_API);
    }


    private void CallGetTripListFailed(Throwable error) {
        is_loading.postValue(false);
        error_call_server.postValue(getApplication().getString(R.string.can_not_connect_to_server));
        Log.e(Tag.CYBER_DEBUG, "CallGetTripListFailed" + error.getMessage());
    }
}
