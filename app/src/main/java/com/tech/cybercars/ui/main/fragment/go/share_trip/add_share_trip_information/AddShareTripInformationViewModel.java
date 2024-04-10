package com.tech.cybercars.ui.main.fragment.go.share_trip.add_share_trip_information;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.data.sub_models.TripSharing;
import com.tech.cybercars.ui.base.BaseViewModel;

import java.time.LocalTime;

public class AddShareTripInformationViewModel extends BaseViewModel {
    public MutableLiveData<Integer> current_page = new MutableLiveData<>();
    public MutableLiveData<TripSharing> trip_sharing = new MutableLiveData<>();
    public MutableLiveData<String> destination_type = new MutableLiveData<>();
    public MutableLiveData<String> start_date = new MutableLiveData<>();
    public MutableLiveData<String> start_time = new MutableLiveData<>();
    public MutableLiveData<String> price = new MutableLiveData<>();
    public MutableLiveData<String> description = new MutableLiveData<>();
    public AddShareTripInformationViewModel(@NonNull Application application) {
        super(application);
    }
}
