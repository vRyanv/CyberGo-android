package com.tech.cybercars.ui.main.fragment.go.add_share_trip_information;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.ui.base.BaseViewModel;

public class AddShareTripInformationViewModel extends BaseViewModel {
    public MutableLiveData<String> origin = new MutableLiveData<>();
    public MutableLiveData<String> destination = new MutableLiveData<>();
    public MutableLiveData<String> time_estimate = new MutableLiveData<>();
    public MutableLiveData<String> distance = new MutableLiveData<>();
    public AddShareTripInformationViewModel(@NonNull Application application) {
        super(application);

    }

    @Override
    public void ResetViewModel() {

    }
}
