package com.tech.cybercars.ui.main.fragment.setting.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.ui.base.BaseViewModel;

public class ProfileViewModel extends BaseViewModel {
    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> gender = new MutableLiveData<>();
    public MutableLiveData<String> full_name = new MutableLiveData<>();
    public MutableLiveData<String> phone_number = new MutableLiveData<>();
    public MutableLiveData<String> country_name = new MutableLiveData<>();
    public MutableLiveData<String> avatar = new MutableLiveData<>();

    public ProfileViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void ResetViewModel() {

    }
}
