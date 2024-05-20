package com.tech.cybercars.ui.main.fragment.account;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.StatusCode;
import com.tech.cybercars.constant.Tag;
import com.tech.cybercars.data.remote.base.BaseResponse;
import com.tech.cybercars.data.remote.retrofit.ResFailCallback;
import com.tech.cybercars.data.remote.retrofit.ResSuccessCallback;
import com.tech.cybercars.data.remote.user.fcm.UpdateFCMBody;
import com.tech.cybercars.data.repositories.UserRepository;
import com.tech.cybercars.ui.base.BaseViewModel;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import retrofit2.Response;

public class AccountViewModel extends BaseViewModel {
    public MutableLiveData<Boolean> is_update_success_password = new MutableLiveData<>();
    public MutableLiveData<String> wrong_current_password = new MutableLiveData<>();
    private final UserRepository user_repo;

    public AccountViewModel(@NonNull Application application) {
        super(application);
        user_repo = UserRepository.GetInstance();
    }

    public void HandleUpdatePassword(String current_password, String new_password) {
        is_loading.setValue(true);
        String user_token = SharedPreferencesUtil.GetString(getApplication(), SharedPreferencesUtil.USER_TOKEN_KEY);
        user_repo.UpdatePassword(
                user_token,
                current_password,
                new_password,
                response -> {
                    if (!response.isSuccessful() || response.body() == null) {
                        is_loading.setValue(false);
                        error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
                    }

                    if(response.body().code != StatusCode.UPDATED){
                        is_loading.setValue(false);
                        wrong_current_password.postValue(getApplication().getString(R.string.invalid_current_password));
                        return;
                    }

                    is_update_success_password.postValue(true);
                    is_loading.setValue(false);
                },
                error -> {
                    is_loading.setValue(false);
                    error_call_server.postValue(getApplication().getString(R.string.something_went_wrong));
                }
        );
    }
}