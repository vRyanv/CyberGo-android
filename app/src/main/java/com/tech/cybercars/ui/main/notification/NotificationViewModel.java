package com.tech.cybercars.ui.main.notification;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.DelayTime;
import com.tech.cybercars.constant.StatusCode;
import com.tech.cybercars.data.local.AppDBContext;
import com.tech.cybercars.data.models.Notification;
import com.tech.cybercars.data.remote.notification.NotificationResponse;
import com.tech.cybercars.data.repositories.NotificationRepository;
import com.tech.cybercars.ui.base.BaseViewModel;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import java.util.List;

import retrofit2.Response;


public class NotificationViewModel extends BaseViewModel {
    public MutableLiveData<List<Notification>> notification_list = new MutableLiveData<>();
    private final NotificationRepository notification_repo;

    public NotificationViewModel(@NonNull Application application) {
        super(application);
        notification_repo = NotificationRepository.GetInstance();
    }

    public void HandleLoadNotification() {
        List<Notification> notifications = AppDBContext.GetInstance(getApplication()).NotificationDAO().GetNotificationList();
        Notification notification = new Notification(
                "dasd",
                "request join trip",
                "1714275843198IMG_1714221231203_1714221496668.jpg",
                Long.parseLong("1714298090443"),
                "Le Truc has accepted your request to join"
        );
        notifications.add(0, notification);
        if(!notifications.isEmpty()){
            notification_list.setValue(notifications);
            return;
        }

        is_loading.setValue(true);
        String token = SharedPreferencesUtil.GetString(getApplication(), SharedPreferencesUtil.USER_TOKEN_KEY);
        notification_repo.GetNotificationList(
                token,
                this::CallGetNotificationSuccess,
                this::CallGetNotificationFail
        );
    }

    private void CallGetNotificationSuccess(Response<NotificationResponse> response) {
        new Handler().postDelayed(() -> {
            if (!response.isSuccessful() || response.body() == null) {
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
                is_loading.postValue(false);
                return;
            }

            if (response.body().code == StatusCode.OK) {
                notification_list.postValue(response.body().notification_list);
            }

            is_loading.postValue(false);
        }, DelayTime.CALL_API);
    }

    private void CallGetNotificationFail(Throwable throwable) {
        new Handler().postDelayed(() -> {
            is_loading.postValue(false);
            error_call_server.postValue(getApplication().getString(R.string.can_not_connect_to_server));
        }, DelayTime.CALL_API);
    }
}
