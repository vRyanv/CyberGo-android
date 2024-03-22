package com.tech.cybercars.ui.main.fragment.setting.profile.edit_phone;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.ui.base.BaseViewModel;

public class EditPhoneViewModel extends BaseViewModel {
    public MutableLiveData<String> phone_number = new MutableLiveData<>();
    public MutableLiveData<String> phone_number_error = new MutableLiveData<>();
    public MutableLiveData<String> country_name_code = new MutableLiveData<>();
    public EditPhoneViewModel(@NonNull Application application) {
        super(application);
    }
}
