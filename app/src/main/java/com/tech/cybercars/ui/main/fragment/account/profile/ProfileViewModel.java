package com.tech.cybercars.ui.main.fragment.account.profile;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.DelayTime;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.StatusCode;
import com.tech.cybercars.data.local.AppDBContext;
import com.tech.cybercars.data.models.User;
import com.tech.cybercars.data.remote.user.profile.ProfileResponse;
import com.tech.cybercars.data.repositories.UserRepository;
import com.tech.cybercars.ui.base.BaseViewModel;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class ProfileViewModel extends BaseViewModel {
    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> gender = new MutableLiveData<>();
    public MutableLiveData<String> full_name = new MutableLiveData<>();
    public MutableLiveData<String> phone_number = new MutableLiveData<>();
    public MutableLiveData<String> avatar = new MutableLiveData<>();
    public MutableLiveData<String> address = new MutableLiveData<>();
    public MutableLiveData<String> identity_number = new MutableLiveData<>();
    public MutableLiveData<String> birthday = new MutableLiveData<>();
    private final UserRepository user_repo;
    private final AppDBContext app_db_context;
    public User user_profile;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        user_repo = UserRepository.GetInstance();
        app_db_context = AppDBContext.GetInstance(getApplication());
    }

    public void LoadProfileInformation() {
        String user_id = SharedPreferencesUtil.GetString(getApplication(), FieldName.USER_ID);
        if(!user_id.equals("")){
            user_profile = app_db_context.UserDao().FindUserById(user_id);
            if(user_profile != null){
                BindData(user_profile);
                return;
            }
        }

        LoadProfileFromServer();
    }
    public void LoadProfileFromServer(){
        is_loading.setValue(true);
        String user_token = SharedPreferencesUtil.GetString(getApplication(), SharedPreferencesUtil.USER_TOKEN_KEY);
        user_repo.GetProfileInformation(
                user_token,
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
                user_profile = new User( response.body().id, response.body().role, response.body().email,
                        response.body().full_name, response.body().gender, response.body().birthday, response.body().avatar, response.body().id_number,
                        response.body().address, response.body().phone_number,  response.body().country.prefix,
                        response.body().country.code, response.body().front_id_card,  response.body().back_id_card
                );

                ExecutorService executor_service = Executors.newSingleThreadExecutor();
                executor_service.execute(()-> {
                    User user = app_db_context.UserDao().FindUserById(user_profile.user_id);
                    if(user == null){
                        app_db_context.UserDao().InsertUser(user_profile);
                    } else {
                        app_db_context.UserDao().UpdateUser(user_profile);
                    }
                });
                executor_service.shutdown();

                BindData(user_profile);
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

    private void BindData(User user){
        String not_update = getApplication().getString(R.string.not_update);
        avatar.postValue(user.avatar);
        full_name.postValue(user.full_name);
        email.postValue(user.email);
        String phone = user.country_prefix + user.phone_number;
        phone_number.postValue(phone);
        gender.postValue(user.gender);
        address.postValue(user.address != null ? user.address : not_update);
        identity_number.postValue(user.id_number != null ? user.id_number : not_update);
        birthday.postValue(user.birthday != null ? user.birthday : not_update);
    }

}
