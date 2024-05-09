package com.tech.cybercars.ui.main.fragment.trip.edit_trip;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.DelayTime;
import com.tech.cybercars.constant.StatusCode;
import com.tech.cybercars.data.models.User;
import com.tech.cybercars.data.remote.base.BaseResponse;
import com.tech.cybercars.data.remote.trip.UpdateTripInformationBody;
import com.tech.cybercars.data.remote.user.profile.ProfileResponse;
import com.tech.cybercars.data.repositories.TripRepository;
import com.tech.cybercars.ui.base.BaseViewModel;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class EditTripInformationViewModel extends BaseViewModel {
    public String trip_id;
    public MutableLiveData<String> trip_name = new MutableLiveData<>();
    public MutableLiveData<String> trip_name_error = new MutableLiveData<>();
    public MutableLiveData<String> start_date = new MutableLiveData<>();
    public MutableLiveData<String> start_date_error = new MutableLiveData<>();
    public String start_date_data;
    public MutableLiveData<String> start_time = new MutableLiveData<>();
    public MutableLiveData<String> start_time_error = new MutableLiveData<>();
    public MutableLiveData<String> price = new MutableLiveData<>();
    public MutableLiveData<String> price_error = new MutableLiveData<>();
    public MutableLiveData<String> description = new MutableLiveData<>();
    private TripRepository trip_repo;
    public EditTripInformationViewModel(@NonNull Application application) {
        super(application);

        trip_repo = TripRepository.GetInstance();
    }

    public void HandleUpdateTripInformation(){
        if(!IsValidData()){
            return;
        }
        is_loading.setValue(true);
        String user_token = SharedPreferencesUtil.GetString(getApplication(), SharedPreferencesUtil.USER_TOKEN_KEY);
        UpdateTripInformationBody update_trip_body = new UpdateTripInformationBody(
                trip_id,
                trip_name.getValue(),
                start_date_data,
                start_time.getValue(),
                price.getValue(),
                description.getValue()
        );

        trip_repo.UpdateTripInformation(
                user_token,
                update_trip_body,
                this::CallUpdateTripInfoSuccess,
                this::CallUpdateTripInfoFailed
        );
    }

    private void CallUpdateTripInfoSuccess(Response<BaseResponse> response) {
        new Handler().postDelayed(() -> {
            if(!response.isSuccessful() || response.body() == null){
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
                is_loading.postValue(false);
                return;
            }
            if (response.body().code == StatusCode.OK) {
                is_success.setValue(true);
            } else if (response.body().code == StatusCode.NOT_FOUND) {
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
            }
            is_loading.postValue(false);
        }, DelayTime.CALL_API);
    }


    private void CallUpdateTripInfoFailed(Throwable error) {
        is_loading.postValue(false);
        error_call_server.postValue(getApplication().getString(R.string.can_not_connect_to_server));
    }

    private boolean IsValidData(){
        boolean is_valid = true;
        String error_mess = "";
        String can_not_empty_str = getApplication().getString(R.string.can_not_empty);
        if(trip_name.getValue().equals("")){
            is_valid = false;
            error_mess = getApplication().getString(R.string.trip_name) + " " + can_not_empty_str;
            trip_name_error.setValue(error_mess);
        }

        if(start_date.equals("")){
            is_valid = false;
            error_mess = getApplication().getString(R.string.start_date) + " " + can_not_empty_str;
            start_date_error.setValue(error_mess);
        }

        if(start_time.equals("")){
            is_valid = false;
            error_mess = getApplication().getString(R.string.start_time) + " " + can_not_empty_str;
            start_time_error.setValue(error_mess);
        }

        if(price.equals("")){
            is_valid = false;
            error_mess = getApplication().getString(R.string.price) + " " + can_not_empty_str;
            price_error.setValue(error_mess);
        }

        return is_valid;
    }
}
