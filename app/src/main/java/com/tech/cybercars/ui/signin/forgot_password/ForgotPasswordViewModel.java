package com.tech.cybercars.ui.signin.forgot_password;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.ui.base.BaseViewModel;

public class ForgotPasswordViewModel extends BaseViewModel {
    public int ENTER_MAIL_STEP = 0;
    public int ENTER_OTP_STEP = 1;
    public MutableLiveData<Integer> current_step = new MutableLiveData<>();
    public ForgotPasswordViewModel(@NonNull Application application) {
        super(application);
    }
}
