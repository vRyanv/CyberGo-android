package com.tech.cybercars.ui.main.fragment.trip.trip_detail;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.DelayTime;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.StatusCode;
import com.tech.cybercars.constant.Tag;
import com.tech.cybercars.data.models.TripManagement;
import com.tech.cybercars.data.models.User;
import com.tech.cybercars.data.remote.trip.UpdateTripResponse;
import com.tech.cybercars.data.remote.user.profile.ProfileResponse;
import com.tech.cybercars.data.repositories.TripRepository;
import com.tech.cybercars.ui.base.BaseViewModel;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Response;

public class TripDetailViewModel extends BaseViewModel {
    public MutableLiveData<TripManagement> trip_management = new MutableLiveData<>();
    public MutableLiveData<Boolean> can_perform_trip = new MutableLiveData<>();
    public MutableLiveData<String> update_trip_status = new MutableLiveData<>();
    public TripRepository trip_repo;
    public TripDetailViewModel(@NonNull Application application) {
        super(application);
        trip_repo = TripRepository.GetInstance();
    }

    public void UpdateTripStatus(String trip_status){
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
            if(!response.isSuccessful() || response.body() == null){
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
                is_loading.postValue(false);
                return;
            }
            if (response.body().code == StatusCode.OK) {
                trip_management.getValue().trip_status = response.body().status;
                TripManagement temp_trip = trip_management.getValue();
                trip_management.setValue(null);
                trip_management.setValue(temp_trip);
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
}
