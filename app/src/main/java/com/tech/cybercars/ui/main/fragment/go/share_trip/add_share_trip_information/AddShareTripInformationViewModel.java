package com.tech.cybercars.ui.main.fragment.go.share_trip.add_share_trip_information;

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
import com.tech.cybercars.data.local.trip.TripDAO;
import com.tech.cybercars.data.models.trip.Destination;
import com.tech.cybercars.data.models.trip.Trip;
import com.tech.cybercars.data.remote.trip.TripBodyAndResponse;
import com.tech.cybercars.data.repositories.TripRepository;
import com.tech.cybercars.ui.base.BaseViewModel;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class AddShareTripInformationViewModel extends BaseViewModel {
    public MutableLiveData<Integer> current_page = new MutableLiveData<>();
    public MutableLiveData<Trip> trip = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Destination>> destination_list = new MutableLiveData<>();
    public MutableLiveData<String> start_date = new MutableLiveData<>();
    public MutableLiveData<String> start_time = new MutableLiveData<>();
    public MutableLiveData<String> price = new MutableLiveData<>();
    public MutableLiveData<String> description = new MutableLiveData<>();
    public TripRepository trip_repo;
    public TripDAO trip_dao;
    public AddShareTripInformationViewModel(@NonNull Application application) {
        super(application);
        trip_repo = TripRepository.GetInstance();
        trip_dao = AppDBContext.GetInstance(application).TripDAO();
    }

    public void HandleCreateTripSharing(){
        if(trip.getValue() == null){
            return;
        }
        is_loading.setValue(true);
        trip.getValue().start_date_time = start_date.getValue() + " " + start_time.getValue();
        trip.getValue().price = Double.parseDouble(price.getValue());
        trip.getValue().description = description.getValue();
        TripBodyAndResponse trip_body= new TripBodyAndResponse();
        trip_body.trip = trip.getValue();
        trip_body.destinations = destination_list.getValue();
        String user_token = SharedPreferencesUtil.GetString(getApplication(), SharedPreferencesUtil.USER_TOKEN_KEY);
        trip_repo.CreateTripSharing(
                user_token,
                trip_body,
                this::CallCreateTripSharingSuccess,
                this::CallCreateTripSharingFailed
        );
    }

    private void CallCreateTripSharingSuccess(Response<TripBodyAndResponse> response) {
        new Handler().postDelayed(() -> {
            if(!response.isSuccessful() || response.body() == null){
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
                is_loading.postValue(false);
                return;
            }
            if (response.body().code == StatusCode.CREATED) {
                Log.e(Tag.CYBER_DEBUG, String.valueOf( response.body().code));
                is_success.postValue(true);
//                ExecutorService executor_service = Executors.newSingleThreadExecutor();
//                executor_service.execute(()-> {
//                    trip_dao.InsertTrip(response.body());
//                });
//                executor_service.shutdown();
            } else if (response.body().code == StatusCode.BAD_REQUEST) {
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
            }
            is_loading.postValue(false);
        }, DelayTime.CALL_API);
    }

    private void CallCreateTripSharingFailed(Throwable error) {
        is_loading.postValue(false);
        error_call_server.postValue(getApplication().getString(R.string.can_not_connect_to_server));
    }

}
