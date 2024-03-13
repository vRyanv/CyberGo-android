package com.tech.cybercars.ui.signin;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.StatusCode;
import com.tech.cybercars.data.remote.user.signin.SignInBody;
import com.tech.cybercars.data.remote.user.signin.SignInResponse;
import com.tech.cybercars.data.repositories.UserRepository;
import com.tech.cybercars.ui.base.BaseViewModel;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import retrofit2.Response;

public class SignInViewModel extends BaseViewModel {
    public MutableLiveData<String> email = new MutableLiveData<>();

    public LiveData<String> getEmailLive() {
        return email;
    }

    public MutableLiveData<String> email_error = new MutableLiveData<>();

    public LiveData<String> getEmailErrorLive() {
        return email_error;
    }

    public MutableLiveData<String> password = new MutableLiveData<>();

    public LiveData<String> getPasswordLive() {
        return password;
    }

    public MutableLiveData<String> password_error = new MutableLiveData<>();

    public LiveData<String> getPasswordErrorLive() {
        return password_error;
    }

    private final MutableLiveData<String> error_login = new MutableLiveData<>();


    public LiveData<String> getErrorLoginLive() {
        return error_login;
    }
    private final MutableLiveData<Boolean> is_verify_account = new MutableLiveData<>();
    public LiveData<Boolean> getIsVerifyAccount(){return is_verify_account;}

    private final UserRepository user_repository = UserRepository.GetInstance();

    public SignInViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void ResetViewModel() {
        email.setValue(null);
        email_error.setValue(null);
        password.setValue(null);
        password_error.setValue(null);
        this.ResetBaseViewModel();
    }

    public void SignInHandle() {
        is_loading.setValue(true);
        if (ValidateData()) {
            SignInRemote(new SignInBody(email.getValue(), password.getValue()));
        } else {
            is_loading.setValue(false);
        }
    }

    private boolean ValidateData() {
        boolean is_valid = true;

        if (email.getValue() == null || email.getValue().equals("")) {
            email_error.setValue(
                    getApplication().getString(R.string.email) + " "
                            + getApplication().getString(R.string.can_not_empty)
            );
            is_valid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getValue()).matches()) {
            email_error.setValue(getApplication().getString(R.string.invalid_email));
            is_valid = false;
        }

        if (password.getValue() == null || password.getValue().equals("")) {
            password_error.setValue(
                    getApplication().getString(R.string.password) + " "
                            + getApplication().getString(R.string.can_not_empty)
            );
            is_valid = false;
        }
        return is_valid;
    }

    private void SignInRemote(SignInBody sign_in_body) {
        user_repository.UserSignIn(
                sign_in_body,
                this::CallLoginSuccess,
                this::CallLoginFail);
    }

    private void CallLoginSuccess(Response<SignInResponse> response) {
        is_loading.postValue(false);
        if (response.body().getCode() == StatusCode.OK) {
            String token = response.body().getToken();
            SharedPreferencesUtil.SetUserToken(getApplication(), token);
            is_success.postValue(true);
        } else if (response.body().getCode() == StatusCode.BAD_REQUEST) {
            error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
        } else if (response.body().getCode() == StatusCode.NOT_FOUND) {
            error_login.postValue(getApplication().getString(R.string.wrong_email_or_password));
        } else if (response.body().getCode() == StatusCode.VERIFY) {
            is_verify_account.postValue(true);
        }
    }

    private void CallLoginFail(Throwable error) {
        is_loading.postValue(false);
        error_call_server.postValue(getApplication().getString(R.string.can_not_connect_to_server));
    }
}
