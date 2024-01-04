package com.tech.cybercars.ui.signin;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.constant.StatusCode;
import com.tech.cybercars.data.models.User;
import com.tech.cybercars.data.remote.api.RetrofitRequest;
import com.tech.cybercars.data.remote.login.LoginBody;
import com.tech.cybercars.data.remote.login.LoginResponse;
import com.tech.cybercars.data.remote.login.LoginService;
import com.tech.cybercars.ui.base.BaseViewModel;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignInViewModel extends BaseViewModel {
    public MutableLiveData<String> email = new MutableLiveData<>("khangok1610@gmail.com");
    public LiveData<String> getEmail(){return email;}
    public MutableLiveData<String> email_error = new MutableLiveData<>();
    public LiveData<String> getEmailError(){return email_error;}
    public MutableLiveData<String> password = new MutableLiveData<>("123");
    public LiveData<String> getPassword(){return password;}
    public MutableLiveData<String> password_error = new MutableLiveData<>();
    public LiveData<String> getPasswordError(){return password;}
    private final MutableLiveData<User> user = new MutableLiveData<>();
    private LiveData<User> getUser(){return user;}
    public SignInViewModel(@NonNull Application application) {
        super(application);
    }
    public void LoginHandle(){
        LoginRemote(new LoginBody(email.getValue(), password.getValue()));
    }
    private void LoginRemote(LoginBody login_body){
        LoginService login_service = RetrofitRequest.getInstance().create(LoginService.class);
        Call<LoginResponse> login_request = login_service.LoginRequest(login_body);
        login_request.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().getCode() == StatusCode.OK){
                        LoginSuccess(response.body());
                    } else if(response.body().getCode() == StatusCode.NOT_FOUND){
                        LoginFail();
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {

            }
        });
    }

    private void LoginSuccess(LoginResponse response){
        String token = response.getToken();
        SharedPreferencesUtil.SetUserToken(getApplication(), token);
        is_success.setValue(true);
    }

    private void LoginFail(){
        is_success.setValue(false);
    }
}
