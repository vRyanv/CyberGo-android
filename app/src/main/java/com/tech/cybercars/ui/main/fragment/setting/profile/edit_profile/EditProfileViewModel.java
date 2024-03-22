package com.tech.cybercars.ui.main.fragment.setting.profile.edit_profile;

import android.app.Application;
import android.net.Uri;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.DelayTime;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.StatusCode;
import com.tech.cybercars.data.local.AppDBContext;
import com.tech.cybercars.data.local.user.User;
import com.tech.cybercars.data.remote.base.BaseResponse;
import com.tech.cybercars.data.remote.user.driver.DriverRegistrationResponse;
import com.tech.cybercars.data.remote.user.profile.UpdateProfileResponse;
import com.tech.cybercars.data.repositories.UserRepository;
import com.tech.cybercars.ui.base.BaseViewModel;
import com.tech.cybercars.utils.FileUtil;
import com.tech.cybercars.utils.RealPathUtil;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class EditProfileViewModel extends BaseViewModel {
    public Uri avatar_uri;
    public MutableLiveData<String> full_name = new MutableLiveData<>();
    public MutableLiveData<String> full_name_error = new MutableLiveData<>();
    public MutableLiveData<String> gender = new MutableLiveData<>();
    public MutableLiveData<String> identity_number = new MutableLiveData<>();
    public MutableLiveData<String> address = new MutableLiveData<>();
    public MutableLiveData<String> error_update_profile = new MutableLiveData<>();

    private final UserRepository user_repo = UserRepository.GetInstance();
    public User edit_user;
    private final AppDBContext app_db_context;
    public EditProfileViewModel(@NonNull Application application) {
        super(application);
        app_db_context = AppDBContext.GetInstance(getApplication());
    }


    public void HandleUpdateProfile(){
        String error_mess = "";
        if(full_name.getValue().equals("")){
            error_mess = getApplication().getString(R.string.full_name)
                    + " " + getApplication().getString(R.string.can_not_empty);
            full_name_error.setValue(error_mess);
        }

        if(!error_mess.equals("")){
            is_loading.setValue(false);
            return;
        }

        is_loading.setValue(true);
        //avatar
        MultipartBody.Part avatar_body = null;
        if(avatar_uri != null){
            File file = new File(RealPathUtil.getRealPath(getApplication(), avatar_uri));
            String mime_type = "image/" + FileUtil.GetFileExtension(file.getName());
            RequestBody avatar_request_body = RequestBody.create(MediaType.parse(mime_type), file);
            avatar_body = MultipartBody.Part.createFormData(FieldName.AVATAR, file.getName(), avatar_request_body);
        }

        //other field
        RequestBody full_name_body = RequestBody.create(MediaType.parse(FieldName.FULL_NAME), full_name.getValue());
        RequestBody gender_body = RequestBody.create(MediaType.parse(FieldName.GENDER), gender.getValue());
        RequestBody id_number_body = RequestBody.create(
                MediaType.parse(FieldName.ID_NUMBER),
                identity_number.getValue() == null ? "" : identity_number.getValue()
        );
        RequestBody address_body = RequestBody.create(
                MediaType.parse(FieldName.ADDRESS),
                address.getValue() == null ? "" : address.getValue()
        );


        String user_token = SharedPreferencesUtil.GetString(getApplication(), SharedPreferencesUtil.USER_TOKEN_KEY);
        user_repo.UpdateProfileInformation(
                user_token, avatar_body, full_name_body,
                gender_body, id_number_body, address_body,
                this::CallUpdateProfileSuccess,
                this::CallUpdateProfileFailed
        );
    }

    private void CallUpdateProfileSuccess(Response<UpdateProfileResponse> response) {
        new Handler().postDelayed(() -> {
            if(!response.isSuccessful()){
                error_call_server.postValue(getApplication().getString(R.string.something_went_wrong));
                return;
            }
            if (response.body().getCode() == StatusCode.UPDATED) {
                edit_user.full_name = full_name.getValue();
                SharedPreferencesUtil.SetString(getApplication(), FieldName.FULL_NAME, edit_user.full_name);
                edit_user.gender = gender.getValue();
                edit_user.id_number = identity_number.getValue();
                edit_user.address = address.getValue();
                if(response.body().avatar != null){
                    edit_user.avatar = response.body().avatar;
                    SharedPreferencesUtil.SetString(getApplication(), FieldName.AVATAR, edit_user.avatar);
                }
                ExecutorService executor_service = Executors.newSingleThreadExecutor();
                executor_service.execute(()-> {
                    app_db_context.UserDao().UpdateUser(edit_user);
                });
                executor_service.shutdown();
                is_success.postValue(true);
            } else if (response.body().getCode() == StatusCode.BAD_REQUEST) {
                error_update_profile.postValue(getApplication().getString(R.string.your_request_is_invalid));
            }
            is_loading.postValue(false);
        }, DelayTime.CALL_API);
    }

    private void CallUpdateProfileFailed(Throwable error) {
        is_loading.postValue(false);
        error_call_server.postValue(getApplication().getString(R.string.can_not_connect_to_server));
    }


}
