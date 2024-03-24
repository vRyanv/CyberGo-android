package com.tech.cybercars.ui.main;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.tech.cybercars.constant.FieldName;
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
import timber.log.Timber;

public class MainViewModel extends BaseViewModel {
    private final UserRepository user_repo;

    public MainViewModel(@NonNull Application application) {
        super(application);
        user_repo = UserRepository.GetInstance();
    }

    public void HandleUpdateFirebaseToken() {
        boolean is_firebase_token_updated = SharedPreferencesUtil.GetBoolean(getApplication(), SharedPreferencesUtil.IS_FIREBASE_TOKEN_UPDATED);
        if(is_firebase_token_updated){
            return;
        }
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if(!task.isSuccessful()){
                Timber.tag(Tag.CYBER_DEBUG).w("get firebase token fail");
                return;
            }
            String firebase_token = task.getResult();
            UpdateToken(firebase_token);
        });
    }

    private void UpdateToken(String firebase_token){
        UpdateFCMBody update_firebase_token_body = new UpdateFCMBody();
        update_firebase_token_body.firebase_token = firebase_token;
        String user_token = SharedPreferencesUtil.GetString(getApplication(), SharedPreferencesUtil.USER_TOKEN_KEY);
        user_repo.UpdateFirebaseToken(
                user_token,
                update_firebase_token_body,
                response -> {
                    if (!response.isSuccessful() || response.body() == null) {
                        Toast.makeText(getApplication(), "Update firebase token fail", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (response.body().getCode() == StatusCode.UPDATED) {
                        Toast.makeText(getApplication(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        SharedPreferencesUtil.SetBoolean(
                                getApplication(),
                                SharedPreferencesUtil.IS_FIREBASE_TOKEN_UPDATED,
                                true);
                    } else {
                        Toast.makeText(getApplication(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(getApplication(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );
    }

}
