package com.tech.cybercars.ui.main.user_profile;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.DelayTime;
import com.tech.cybercars.constant.StatusCode;
import com.tech.cybercars.data.local.AppDBContext;
import com.tech.cybercars.data.local.user.UserDAO;
import com.tech.cybercars.data.models.User;
import com.tech.cybercars.data.remote.user.profile.ProfileResponse;
import com.tech.cybercars.data.repositories.UserRepository;
import com.tech.cybercars.ui.base.BaseViewModel;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class UserProfileViewModel extends BaseViewModel {
    public String user_id;
    public MutableLiveData<User> user_profile = new MutableLiveData<>();
    private final UserDAO user_dao;
    private final UserRepository user_repo;
    public UserProfileViewModel(@NonNull Application application) {
        super(application);
        user_dao = AppDBContext.GetInstance(application).UserDao();
        user_repo = UserRepository.GetInstance();
    }

    public void HandleLoadUserProfile(){
        User user = user_dao.FindUserById(user_id);
        if(user != null){
            user_profile.setValue(user);
            return;
        }

        LoadDataFromServer();
    }

    public void LoadDataFromServer(){
        is_loading.setValue(true);
        String user_token = SharedPreferencesUtil.GetString(getApplication(), SharedPreferencesUtil.USER_TOKEN_KEY);
        user_repo.ViewUserProfile(
                user_token,
                user_id,
                this::CallProfileInfoSuccess,
                this::CallProfileInfoFailed
        );
    }

    private void CallProfileInfoSuccess(Response<ProfileResponse> response) {
        new Handler().postDelayed(() -> {
            if(!response.isSuccessful() || response.body() == null){
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
                is_loading.postValue(false);
                return;
            }
            if (response.body().code == StatusCode.OK) {
                User user = new User();
                user.user_id = response.body().user_id;
                user.rating = response.body().rating;
                user.email = response.body().email;
                user.full_name = response.body().full_name;
                user.phone_number = response.body().phone_number;
                user.gender = response.body().gender;
                user.birthday = response.body().birthday;
                user.address = response.body().address;
                user.avatar = response.body().avatar;
                user_profile.postValue(user);

                ExecutorService executor_service = Executors.newSingleThreadExecutor();
                executor_service.execute(()-> {
                    user_dao.DeleteById(user.user_id);
                    user_dao.InsertUser(user);
                });
                executor_service.shutdown();
            } else if (response.body().code == StatusCode.NOT_FOUND) {
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
            }
            is_loading.postValue(false);
        }, DelayTime.CALL_API);
    }


    private void CallProfileInfoFailed(Throwable error) {
        is_loading.postValue(false);
        error_call_server.postValue(getApplication().getString(R.string.can_not_connect_to_server));
    }
}
