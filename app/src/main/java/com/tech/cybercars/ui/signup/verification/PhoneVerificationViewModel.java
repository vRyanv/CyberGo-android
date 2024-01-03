package com.tech.cybercars.ui.signup.verification;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.ui.base.BaseViewModel;

public class PhoneVerificationViewModel extends BaseViewModel {
    public MutableLiveData<String> otp_1 = new MutableLiveData<>();
    public MutableLiveData<String> otp_2 = new MutableLiveData<>();
    public MutableLiveData<String> otp_3 = new MutableLiveData<>();
    public MutableLiveData<String> otp_4 = new MutableLiveData<>();
    public MutableLiveData<String> otp_5 = new MutableLiveData<>();
    public MutableLiveData<String> error = new MutableLiveData<>();
    public PhoneVerificationViewModel(@NonNull Application application) {
        super(application);
    }

    public void VerifyHandle(){
        is_success.setValue(true);
        if(otp_1.getValue() == null || otp_1.getValue().equals("")){

        }
    }
}
