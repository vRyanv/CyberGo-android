package com.tech.cybercars.ui.main.fragment.trip.trip_detail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.data.models.TripManagement;
import com.tech.cybercars.ui.base.BaseViewModel;

public class TripDetailViewModel extends BaseViewModel {
    public MutableLiveData<TripManagement> trip_management = new MutableLiveData<>();
    public TripDetailViewModel(@NonNull Application application) {
        super(application);
    }
}
