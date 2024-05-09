package com.tech.cybercars.ui.main.notification;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.DelayTime;
import com.tech.cybercars.constant.StatusCode;
import com.tech.cybercars.data.local.AppDBContext;
import com.tech.cybercars.data.local.notification.NotificationDAO;
import com.tech.cybercars.data.models.Notification;
import com.tech.cybercars.data.remote.notification.NotificationResponse;
import com.tech.cybercars.data.repositories.NotificationRepository;
import com.tech.cybercars.ui.base.BaseViewModel;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;


public class NotificationViewModel extends BaseViewModel {
    public MutableLiveData<List<Notification>> notification_list = new MutableLiveData<>();
    private final NotificationRepository notification_repo;
    private final NotificationDAO notification_dao;

    public NotificationViewModel(@NonNull Application application) {
        super(application);
        notification_repo = NotificationRepository.GetInstance();
        notification_dao = AppDBContext.GetInstance(application).NotificationDAO();
    }

    public void HandleLoadNotification() {
        List<Notification> notifications = AppDBContext.GetInstance(getApplication()).NotificationDAO().GetNotificationList();
        if(!notifications.isEmpty()){
            notification_list.setValue(notifications);
            return;
        }

        LoadDataFromServer();
    }

    public void LoadDataFromServer(){
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

                ExecutorService executor_service = Executors.newSingleThreadExecutor();
                executor_service.execute(()-> {
                    notification_dao.ClearTable();
                    notification_dao.InsertNotification(response.body().notification_list);
                });
                executor_service.shutdown();
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
