package com.tech.cybercars.ui.main.fragment.trip.edit_trip.map_edit_location.review_location;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.DelayTime;
import com.tech.cybercars.constant.StatusCode;
import com.tech.cybercars.data.models.TripManagement;
import com.tech.cybercars.data.models.User;
import com.tech.cybercars.data.models.trip.Destination;
import com.tech.cybercars.data.remote.base.BaseResponse;
import com.tech.cybercars.data.remote.trip.UpdateTripLocationBody;
import com.tech.cybercars.data.remote.user.profile.ProfileResponse;
import com.tech.cybercars.data.repositories.TripRepository;
import com.tech.cybercars.ui.base.BaseViewModel;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import retrofit2.Response;

public class ReviewLocationViewModel extends BaseViewModel {
    public MutableLiveData<TripManagement> trip_management = new MutableLiveData<>();
    private TripRepository trip_repo;

    public ReviewLocationViewModel(@NonNull Application application) {
        super(application);
        trip_repo = TripRepository.GetInstance();
    }
    public void HandleUpdateLocation(){
        is_loading.setValue(true);
        String trip_id = trip_management.getValue().trip_id;
        double origin_longitude = trip_management.getValue().origin_longitude;
        double origin_latitude = trip_management.getValue().origin_latitude;
        String origin_city = trip_management.getValue().origin_city;
        String origin_state = trip_management.getValue().origin_state;
        String origin_county = trip_management.getValue().origin_county;
        String origin_address = trip_management.getValue().origin_address;
        Destination destination = trip_management.getValue().destinations.get(0);
        UpdateTripLocationBody update_trip_location_body = new UpdateTripLocationBody(
                trip_id,
                origin_longitude,
                origin_latitude,
                origin_city,
                origin_state,
                origin_county,
                origin_address,
                destination
        );
        String user_token = SharedPreferencesUtil.GetString(getApplication(), SharedPreferencesUtil.USER_TOKEN_KEY);
        trip_repo.UpdateTripLocation(
                user_token,
                update_trip_location_body,
                this::CallUpdateLocationSuccess,
                this::CallUpdateLocationFailed
        );
    }

    private void CallUpdateLocationSuccess(Response<BaseResponse> response) {
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


    private void CallUpdateLocationFailed(Throwable error) {
        is_loading.postValue(false);
        error_call_server.postValue(getApplication().getString(R.string.can_not_connect_to_server));
    }


}
