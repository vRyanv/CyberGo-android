package com.tech.cybercars.ui.signup.verification;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.StatusCode;
import com.tech.cybercars.constant.Tag;
import com.tech.cybercars.data.remote.api.RetrofitResponse;
import com.tech.cybercars.data.remote.verification.VerificationBody;
import com.tech.cybercars.data.remote.verification.VerificationResponse;
import com.tech.cybercars.data.remote.verification.VerificationService;
import com.tech.cybercars.data.repositories.UserRepository;
import com.tech.cybercars.ui.base.BaseViewModel;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import retrofit2.Response;

public class PhoneVerificationViewModel extends BaseViewModel {
    private String email;
    public void setEmail(String email){this.email = email;}
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
    public LiveData<String> getErrorOtpCodeLive(){return error_otp_code;}
    private UserRepository user_repository = UserRepository.GetInstance();

    public PhoneVerificationViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void ResetViewModel() {

    }

    public void VerifyHandle() {
        is_loading.setValue(true);
        if (IsValidOTP()) {
            String OTP_string = otp_1.getValue() +
                    otp_2.getValue() +
                    otp_3.getValue() +
                    otp_4.getValue() +
                    otp_5.getValue();
            VerificationBody verify_body = new VerificationBody(OTP_string, email);
            VerifyRemote(verify_body);
        } else {
            is_loading.setValue(false);
            error_otp_code.setValue(getApplication().getString(R.string.please_enter_the_otp_code));
        }
    }

    private void VerifyRemote(VerificationBody verify_body) {
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

    private void CallVerifySuccess(Response<VerificationResponse> verify_response) {
        is_loading.postValue(false);
        if (verify_response.body().getCode() == StatusCode.UPDATED) {
            String user_token = verify_response.body().getUser_token();
            SharedPreferencesUtil.SetUserToken(getApplication(), user_token);
            is_success.postValue(true);
        } else if (verify_response.body().getCode() == StatusCode.NOT_FOUND) {
            error_otp_code.postValue(getApplication().getString(R.string.otp_code_is_incorrect));
        } else if (verify_response.body().getCode() == StatusCode.BAD_REQUEST) {
            error_otp_code.postValue(getApplication().getString(R.string.your_request_is_invalid));
        }
    }

    private void CallVerifyFail(Throwable error) {
        is_loading.postValue(false);
        error_call_server.postValue(getApplication().getString(R.string.can_not_connect_to_server));
    }
}
