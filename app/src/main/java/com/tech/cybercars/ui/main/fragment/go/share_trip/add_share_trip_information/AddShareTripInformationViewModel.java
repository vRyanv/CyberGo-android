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
import com.tech.cybercars.data.local.trip.DestinationDAO;
import com.tech.cybercars.data.local.trip.TripDAO;
import com.tech.cybercars.data.models.trip.Destination;
import com.tech.cybercars.data.models.trip.Trip;
import com.tech.cybercars.data.remote.trip.TripBodyAndResponse;
import com.tech.cybercars.data.repositories.TripRepository;
import com.tech.cybercars.services.eventbus.ActionEvent;
import com.tech.cybercars.ui.base.BaseViewModel;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class AddShareTripInformationViewModel extends BaseViewModel {
    public MutableLiveData<Integer> current_page = new MutableLiveData<>();
    public MutableLiveData<String> trip_name = new MutableLiveData<>();
    public MutableLiveData<String> trip_name_error = new MutableLiveData<>();
    public MutableLiveData<Trip> trip = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Destination>> destination_list = new MutableLiveData<>();
    public MutableLiveData<String> start_date = new MutableLiveData<>();
    public MutableLiveData<String> start_date_error = new MutableLiveData<>();
    public String start_date_data;
    public MutableLiveData<String> start_time = new MutableLiveData<>();
    public MutableLiveData<String> start_time_error = new MutableLiveData<>();
    public MutableLiveData<String> price = new MutableLiveData<>();
    public MutableLiveData<String> price_error = new MutableLiveData<>();
    public MutableLiveData<String> description = new MutableLiveData<>();
    public TripRepository trip_repo;
    public TripDAO trip_dao;
    public DestinationDAO destination_dao;

    public AddShareTripInformationViewModel(@NonNull Application application) {
        super(application);
        trip_repo = TripRepository.GetInstance();
        trip_dao = AppDBContext.GetInstance(application).TripDAO();
        destination_dao = AppDBContext.GetInstance(application).DestinationDAO();
    }

    public void HandleCreateTripSharing() {
        if (trip.getValue() == null) {
            return;
        }

        if(!IsValidTripData()){
            return;
        }

        is_loading.setValue(true);

        trip.getValue().name = trip_name.getValue();
        trip.getValue().start_date = start_date_data;
        trip.getValue().start_time = start_time.getValue();
        trip.getValue().price = Double.parseDouble(price.getValue());
        trip.getValue().description = description.getValue();
        TripBodyAndResponse trip_body = new TripBodyAndResponse();
        trip_body.trip = trip.getValue();
        trip_body.trip.destinations = destination_list.getValue();
        String user_token = SharedPreferencesUtil.GetString(getApplication(), SharedPreferencesUtil.USER_TOKEN_KEY);
        trip_repo.CreateTripSharing(
                user_token,
                trip_body,
                this::CallCreateTripSharingSuccess,
                this::CallCreateTripSharingFailed
        );
    }

    private boolean IsValidTripData() {
        boolean is_valid = true;
        String error_mess = "";
        String can_not_empty = getApplication().getString(R.string.can_not_empty);
        if(trip_name.getValue() == null || trip_name.getValue().equals("")){
            error_mess = getApplication().getString(R.string.trip_name) + " " + can_not_empty;
            trip_name_error.setValue(error_mess);
            is_valid = false;
        }

        if(start_date.getValue() == null || start_date.getValue().equals("")){
            error_mess = getApplication().getString(R.string.start_date) + " " + can_not_empty;
            start_date_error.setValue(error_mess);
            is_valid = false;
        }

        if(start_time.getValue() == null || start_time.getValue().equals("")){
            error_mess = getApplication().getString(R.string.start_time) + " " + can_not_empty;
            start_time_error.setValue(error_mess);
            is_valid = false;
        }

        if(price.getValue() == null || price.getValue().equals("")){
            error_mess = getApplication().getString(R.string.price) + " " + can_not_empty;
            price_error.setValue(error_mess);
            is_valid = false;
        }

        return is_valid;
    }

    private void CallCreateTripSharingSuccess(Response<TripBodyAndResponse> response) {
        new Handler().postDelayed(() -> {
            if (!response.isSuccessful() || response.body() == null) {
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
                Log.e(Tag.CYBER_DEBUG, response.message() );
                is_loading.postValue(false);
                return;
            }
            if (response.body().code == StatusCode.CREATED) {
                is_success.postValue(true);
                EventBus.getDefault().post(new ActionEvent(ActionEvent.REFRESH_TRIP_LIST));
//                ExecutorService executor_service = Executors.newSingleThreadExecutor();
//                executor_service.execute(() -> {
//                    Trip trip = response.body().trip;
//                    trip_dao.InsertTrip(trip);
//
//                    List<Destination> destinations = trip.destinations;
//                    String trip_id = trip.trip_id;
//                    for (Destination des : destinations){
//                        des.trip_id = trip_id;
//                    }
//                    destination_dao.InsertDestinations(destinations);
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
        Log.e(Tag.CYBER_DEBUG, error.getMessage() );
        error_call_server.postValue(getApplication().getString(R.string.can_not_connect_to_server));
    }

}
