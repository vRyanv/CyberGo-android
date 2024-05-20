package com.tech.cybercars.ui.signin.forgot_password;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.DelayTime;
import com.tech.cybercars.constant.StatusCode;
import com.tech.cybercars.constant.Tag;
import com.tech.cybercars.data.remote.base.BaseResponse;
import com.tech.cybercars.data.remote.retrofit.ResFailCallback;
import com.tech.cybercars.data.remote.retrofit.ResSuccessCallback;
import com.tech.cybercars.data.repositories.SecurityRepository;
import com.tech.cybercars.ui.base.BaseViewModel;

import retrofit2.Response;

public class ForgotPasswordViewModel extends BaseViewModel {
    public int ENTER_MAIL_STEP = 0;
    public int ENTER_OTP_STEP = 1;
    public MutableLiveData<Integer> current_step = new MutableLiveData<>();
    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> email_error = new MutableLiveData<>();
    public MutableLiveData<Boolean> is_send_mail_success = new MutableLiveData<>();
    public MutableLiveData<String> error_forgot_pass = new MutableLiveData<>();

    public MutableLiveData<String> otp_code = new MutableLiveData<>();
    public MutableLiveData<String> otp_code_error = new MutableLiveData<>();
    public MutableLiveData<String> new_pass = new MutableLiveData<>();
    public MutableLiveData<String> new_pass_error = new MutableLiveData<>();
    public MutableLiveData<String> confirm_pass = new MutableLiveData<>();
    public MutableLiveData<String> confirm_pass_error = new MutableLiveData<>();

    private final SecurityRepository security_repo;

    public ForgotPasswordViewModel(@NonNull Application application) {
        super(application);
        security_repo = SecurityRepository.GetInstance();
    }

    public void HandleForgotPassword() {
        if (email.getValue() == null || email.getValue().equals("")) {
            String error = getApplication().getString(R.string.email) + " " + getApplication().getString(R.string.can_not_empty);
            email_error.setValue(error);
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getValue()).matches()) {
            email_error.setValue(getApplication().getString(R.string.invalid_email));
            return;
        }
        is_loading.setValue(true);
        security_repo.ForgotPassword(
                email.getValue(),
                response -> {
                    new Handler().postDelayed(() -> {
                        is_loading.setValue(false);
                        if (!response.isSuccessful() || response.body() == null) {
                            Log.e(Tag.CYBER_DEBUG, "HandleForgotPassword: " + response.message() );
                            error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
                            return;
                        }

                        assert response.body() != null;
                        switch (response.body().code) {
                            case StatusCode.NOT_FOUND:
                                error_forgot_pass.setValue(getApplication().getString(R.string.email_not_found));
                                break;
                            case StatusCode.ACCOUNT_BANNED:
                                error_forgot_pass.setValue(getApplication().getString(R.string.your_account_has_been_locked));
                                break;
                            case StatusCode.VERIFY:
                                error_forgot_pass.setValue(getApplication().getString(R.string.the_account_needs_to_verify_the_phone_number));
                                break;
                            default:
                                is_send_mail_success.postValue(true);
                        }
                    }, DelayTime.CALL_API);
                },
                error -> {
                    is_loading.setValue(false);
                    Log.e(Tag.CYBER_DEBUG, "HandleForgotPassword: " + error.getMessage());
                    error_call_server.postValue(getApplication().getString(R.string.can_not_connect_to_server));
                }
        );
    }


    public void HandleResetPassword(){
        if(!IsValidData()){
            return;
        }

        is_loading.setValue(true);
        security_repo.ResetPassword(
                otp_code.getValue(),
                email.getValue(),
                new_pass.getValue(),
                response -> {
                    new Handler().postDelayed(() -> {
                        is_loading.setValue(false);
                        if (!response.isSuccessful() || response.body() == null) {
                            Log.e(Tag.CYBER_DEBUG, "HandleForgotPassword: " + response.message() );
                            error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
                            return;
                        }

                        assert response.body() != null;
                        switch (response.body().code) {
                            case StatusCode.NOT_FOUND:
                                error_forgot_pass.setValue(getApplication().getString(R.string.email_not_found));
                                break;
                            case StatusCode.ACCOUNT_BANNED:
                                error_forgot_pass.setValue(getApplication().getString(R.string.your_account_has_been_locked));
                                break;
                            case StatusCode.VERIFY:
                                error_forgot_pass.setValue(getApplication().getString(R.string.the_account_needs_to_verify_the_phone_number));
                                break;
                            case StatusCode.BAD_REQUEST:
                                error_forgot_pass.setValue(getApplication().getString(R.string.otp_code_is_incorrect_please_check_again));
                                break;
                            default:
                                is_success.postValue(true);
                        }
                    }, DelayTime.CALL_API);
                },
                error -> {
                    is_loading.setValue(false);
                    Log.e(Tag.CYBER_DEBUG, "HandleForgotPassword: " + error.getMessage());
                    error_call_server.postValue(getApplication().getString(R.string.can_not_connect_to_server));
                }
        );
    }

    private boolean IsValidData(){
        boolean is_valid = true;
        String cannot_empty = getApplication().getString(R.string.can_not_empty);
        if(otp_code.getValue() == null || otp_code.getValue().equals("")){
            String error = getApplication().getString(R.string.otp_code) +  cannot_empty;
            otp_code_error.setValue(error);
            is_valid = false;
        }

        if(new_pass.getValue() == null || new_pass.getValue().equals("")){
            String error = getApplication().getString(R.string.new_password) +  cannot_empty;
            new_pass_error.setValue(error);
            is_valid = false;
        }

        if(confirm_pass.getValue() == null || confirm_pass.getValue().equals("")){
            String error = getApplication().getString(R.string.confirm_password) +  cannot_empty;
            confirm_pass_error.setValue(error);
            is_valid = false;
        }

        if(!confirm_pass.getValue().equals(new_pass.getValue())){
            String error = getApplication().getString(R.string.new_password) +
                    getApplication().getString(R.string.and) +
                    getApplication().getString(R.string.confirm_password) +
                    getApplication().getString(R.string.does_not_match);
            confirm_pass_error.setValue(error);
            is_valid = false;
        }

        return is_valid;
    }
}
