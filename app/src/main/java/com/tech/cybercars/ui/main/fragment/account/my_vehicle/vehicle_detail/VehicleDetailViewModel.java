package com.tech.cybercars.ui.main.fragment.account.my_vehicle.vehicle_detail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.data.models.Vehicle;
import com.tech.cybercars.ui.base.BaseViewModel;

public class VehicleDetailViewModel extends BaseViewModel {
    public MutableLiveData<Vehicle> vehicle = new MutableLiveData<>();
    public VehicleDetailViewModel(@NonNull Application application) {
        super(application);
    }
}
