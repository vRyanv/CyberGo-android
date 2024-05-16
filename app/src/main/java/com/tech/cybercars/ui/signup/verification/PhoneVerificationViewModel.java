package com.tech.cybercars.ui.signup.verification;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.DelayTime;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.StatusCode;
import com.tech.cybercars.constant.Tag;
import com.tech.cybercars.data.remote.base.BaseResponse;
import com.tech.cybercars.data.remote.retrofit.ResFailCallback;
import com.tech.cybercars.data.remote.retrofit.ResSuccessCallback;
import com.tech.cybercars.data.remote.user.verification.VerificationBody;
import com.tech.cybercars.data.remote.user.verification.VerificationResponse;
import com.tech.cybercars.data.repositories.UserRepository;
import com.tech.cybercars.ui.base.BaseViewModel;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import retrofit2.Response;

public class PhoneVerificationViewModel extends BaseViewModel {
    public String number_prefix;
    public String phone_number;

    public MutableLiveData<String> otp_1 = new MutableLiveData<>();

    public LiveData<String> getOtp1Live() {
        return otp_1;
    }

    public MutableLiveData<String> otp_2 = new MutableLiveData<>();

    public LiveData<String> getOtp2Live() {
        return otp_2;
    }

    public MutableLiveData<String> otp_3 = new MutableLiveData<>();

    public LiveData<String> getOtp3Live() {
        return otp_3;
    }

    public MutableLiveData<String> otp_4 = new MutableLiveData<>();

    public LiveData<String> getOtp4Live() {
        return otp_4;
    }

    public MutableLiveData<String> otp_5 = new MutableLiveData<>();

    public LiveData<String> getOtp5Live() {
        return otp_5;
    }

    public MutableLiveData<String> error_otp_code = new MutableLiveData<>();

    public LiveData<String> getErrorOtpCodeLive() {
        return error_otp_code;
    }

    private final UserRepository user_repository = UserRepository.GetInstance();
    public MutableLiveData<Boolean> is_resend_success = new MutableLiveData<>();

    public PhoneVerificationViewModel(@NonNull Application application) {
        super(application);
    }
    public void ResendOTPCode(){
        String national_phone = number_prefix + phone_number;
        user_repository.ResendOTP(
                national_phone,
                response -> {
                    if(!response.isSuccessful()){
                        return;
                    }

                    is_resend_success.setValue(true);
                },
                error -> {
                    Log.e(Tag.CYBER_DEBUG, "ResendOTPCode: " + error.getMessage());
                    error_call_server.postValue(getApplication().getString(R.string.can_not_connect_to_server));
                }
        );
    }


    public void HandleVerifyPhone() {
        if (!IsValidOTP()) {
            error_otp_code.setValue(getApplication().getString(R.string.please_enter_the_otp_code));
            return;
        }

        is_loading.setValue(true);
        String OTP_string = otp_1.getValue() +
                otp_2.getValue() +
                otp_3.getValue() +
                otp_4.getValue() +
                otp_5.getValue();
        VerificationBody verify_body = new VerificationBody(OTP_string, number_prefix, phone_number);
        user_repository.ActivateUser(
                verify_body,
                this::CallVerifySuccess,
                this::CallVerifyFail);
    }

    private boolean IsValidOTP() {
        return otp_1.getValue() != null && !otp_1.getValue().equals("") &&
                otp_2.getValue() != null && !otp_2.getValue().equals("") &&
                otp_3.getValue() != null && !otp_3.getValue().equals("") &&
                otp_4.getValue() != null && !otp_4.getValue().equals("") &&
                otp_5.getValue() != null && !otp_5.getValue().equals("");
    }

    private void CallVerifySuccess(Response<BaseResponse> verify_response) {
        is_loading.postValue(false);
        new Handler().postDelayed(() -> {
            assert verify_response.body() != null;
            if (verify_response.body().code == StatusCode.UPDATED) {
                is_success.postValue(true);
            } else if (verify_response.body().code == StatusCode.NOT_FOUND) {
                error_otp_code.postValue(getApplication().getString(R.string.otp_code_is_incorrect));
            } else if (verify_response.body().code == StatusCode.BAD_REQUEST) {
                error_otp_code.postValue(getApplication().getString(R.string.your_request_is_invalid));
            }
        }, DelayTime.CALL_API);
    }

    private void CallVerifyFail(Throwable error) {
        is_loading.postValue(false);
        error_call_server.postValue(getApplication().getString(R.string.can_not_connect_to_server));
    }
}
