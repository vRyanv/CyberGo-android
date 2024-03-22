package com.tech.cybercars.ui.main.fragment.setting.profile.edit_id_card;

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
import com.tech.cybercars.data.remote.user.profile.UpdateIdCardResponse;
import com.tech.cybercars.data.repositories.UserRepository;
import com.tech.cybercars.ui.base.BaseViewModel;
import com.tech.cybercars.utils.FileUtil;
import com.tech.cybercars.utils.RealPathUtil;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class EditIdentityCardViewModel extends BaseViewModel {

    public Uri front_id_card_uri;
    public Uri back_id_card_uri;
    public User user_edit;
    private final UserRepository user_repo;
    private final AppDBContext app_db_context;

    public EditIdentityCardViewModel(@NonNull Application application) {
        super(application);
        user_repo = UserRepository.GetInstance();
        app_db_context = AppDBContext.GetInstance(getApplication());
    }

    public void HandleUpdateIdCard() {
        if (front_id_card_uri == null && back_id_card_uri == null) {
            Toast.makeText(getApplication(), getApplication().getString(R.string.there_are_no_changes), Toast.LENGTH_SHORT).show();
            return;
        }

        is_loading.setValue(true);
        MultipartBody.Part front_id_card_body = null;
        if (front_id_card_uri != null) {
            File file = new File(RealPathUtil.getRealPath(getApplication(), front_id_card_uri));
            String mime_type = "image/" + FileUtil.GetFileExtension(file.getName());
            RequestBody front_id_card_request_body = RequestBody.create(MediaType.parse(mime_type), file);
            front_id_card_body = MultipartBody.Part.createFormData(FieldName.FRONT_ID_CARD, file.getName(), front_id_card_request_body);
        }

        MultipartBody.Part back_id_card_body = null;
        if (back_id_card_uri != null) {
            File file = new File(RealPathUtil.getRealPath(getApplication(), back_id_card_uri));
            String mime_type = "image/" + FileUtil.GetFileExtension(file.getName());
            RequestBody back_id_card_request_body = RequestBody.create(MediaType.parse(mime_type), file);
            back_id_card_body = MultipartBody.Part.createFormData(FieldName.BACK_ID_CARD, file.getName(), back_id_card_request_body);
        }
        List<MultipartBody.Part> id_card_body = new ArrayList<>();
        id_card_body.add(front_id_card_body);
        id_card_body.add(back_id_card_body);

        String user_token = SharedPreferencesUtil.GetString(getApplication(), SharedPreferencesUtil.USER_TOKEN_KEY);
        user_repo.UpdateIdCard(
                user_token,
                id_card_body,
                this::CallUpdateIdCardSuccess,
                this::CallUpdateIdCardFailed
        );

    }

    private void CallUpdateIdCardSuccess(Response<UpdateIdCardResponse> response) {
        new Handler().postDelayed(() -> {
            if (!response.isSuccessful()) {
                error_call_server.postValue(getApplication().getString(R.string.something_went_wrong));
                return;
            }
            assert response.body() != null;
            if (response.body().getCode() == StatusCode.UPDATED) {
                if (response.body().front_id_card != null) {
                    user_edit.front_id_card = response.body().front_id_card;
                }

                if (response.body().back_id_card != null) {
                    user_edit.back_id_card = response.body().back_id_card;
                }

                ExecutorService executor_service = Executors.newSingleThreadExecutor();
                executor_service.execute(() -> {
                    app_db_context.UserDao().UpdateUser(user_edit);
                });
                executor_service.shutdown();

                is_success.postValue(true);
            } else if (response.body().getCode() == StatusCode.BAD_REQUEST) {
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
            }
            is_loading.postValue(false);
        }, DelayTime.CALL_API);
    }

    private void CallUpdateIdCardFailed(Throwable error) {
        is_loading.postValue(false);
        error_call_server.postValue(getApplication().getString(R.string.can_not_connect_to_server));
        Toast.makeText(getApplication(), error.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
