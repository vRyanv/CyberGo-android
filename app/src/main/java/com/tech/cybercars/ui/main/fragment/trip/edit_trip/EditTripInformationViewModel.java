package com.tech.cybercars.ui.main.fragment.trip.edit_trip;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.ui.base.BaseViewModel;

public class EditTripInformationViewModel extends BaseViewModel {
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
    public EditTripInformationViewModel(@NonNull Application application) {
        super(application);
    }
}
