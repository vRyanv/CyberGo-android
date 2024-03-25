package com.tech.cybercars.ui.main.fragment.account.profile.edit_phone.update_phone_verification;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.ui.base.BaseViewModel;

public class UpdatePhoneVerificationViewModel extends BaseViewModel {
    public MutableLiveData<String> otp_1 = new MutableLiveData<>();
    public MutableLiveData<String> otp_2 = new MutableLiveData<>();
    public MutableLiveData<String> otp_3 = new MutableLiveData<>();
    public MutableLiveData<String> otp_4 = new MutableLiveData<>();
    public MutableLiveData<String> otp_5 = new MutableLiveData<>();
    public MutableLiveData<String> error_otp_code = new MutableLiveData<>();

    public UpdatePhoneVerificationViewModel(@NonNull Application application) {
        super(application);
    }
}
