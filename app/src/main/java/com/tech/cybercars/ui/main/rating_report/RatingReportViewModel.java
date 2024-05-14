package com.tech.cybercars.ui.main.rating_report;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.DelayTime;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.StatusCode;
import com.tech.cybercars.constant.Tag;
import com.tech.cybercars.data.models.Rating;
import com.tech.cybercars.data.remote.notification.NotificationResponse;
import com.tech.cybercars.data.remote.rating.RatingListResponse;
import com.tech.cybercars.data.repositories.RatingRepository;
import com.tech.cybercars.ui.base.BaseViewModel;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class RatingReportViewModel extends BaseViewModel {
    public String user_receive_id;
    public MutableLiveData<List<Rating>> rating_list = new MutableLiveData<>();

    public RatingReportViewModel(@NonNull Application application) {
        super(application);
    }

    public void HandleLoadDataFromServer() {
        is_loading.setValue(true);
        String user_token = SharedPreferencesUtil.GetString(getApplication(), SharedPreferencesUtil.USER_TOKEN_KEY);
        RatingRepository.GetInstance().GetRatingList(
                user_token,
                user_receive_id,
                this::CallGetRatingListSuccess,
                this::CallGetRatingListFail
        );
    }

    private void CallGetRatingListSuccess(Response<RatingListResponse> response) {
        new Handler().postDelayed(() -> {
            if (!response.isSuccessful() || response.body() == null) {
                Log.e(Tag.CYBER_DEBUG, "!response.isSuccessful(): " + response.message());
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
                is_loading.postValue(false);
                return;
            }
            if (response.body().code == StatusCode.OK) {
                rating_list.postValue(response.body().rating_list);
            } else {
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
            }

            is_loading.postValue(false);
        }, DelayTime.CALL_API);
    }

    private void CallGetRatingListFail(Throwable throwable) {
        Log.e(Tag.CYBER_DEBUG, "CallGetRatingListFail: " + throwable.getMessage());
        new Handler().postDelayed(() -> {
            is_loading.postValue(false);
            error_call_server.postValue(getApplication().getString(R.string.can_not_connect_to_server));
        }, DelayTime.CALL_API);
    }
}
