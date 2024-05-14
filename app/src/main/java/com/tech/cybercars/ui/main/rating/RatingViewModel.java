package com.tech.cybercars.ui.main.rating;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.DelayTime;
import com.tech.cybercars.constant.StatusCode;
import com.tech.cybercars.data.models.User;
import com.tech.cybercars.data.remote.base.BaseResponse;
import com.tech.cybercars.data.remote.rating.MakeRatingBody;
import com.tech.cybercars.data.remote.user.profile.ProfileResponse;
import com.tech.cybercars.data.repositories.RatingRepository;
import com.tech.cybercars.ui.base.BaseViewModel;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class RatingViewModel extends BaseViewModel {
    public String user_receive;
    public MutableLiveData<String> comment = new MutableLiveData<>();
    public int rating = 5;
    private final RatingRepository rating_repo;
    public RatingViewModel(@NonNull Application application) {
        super(application);
        rating_repo = RatingRepository.GetInstance();
    }
    public void HandleSendRating(){
        is_loading.setValue(true);
        String user_token = SharedPreferencesUtil.GetString(getApplication(), SharedPreferencesUtil.USER_TOKEN_KEY);
        MakeRatingBody make_rating_body = new MakeRatingBody(
                user_receive,
                rating,
                comment.getValue()
        );

        rating_repo.CreateRating(
                user_token,
                make_rating_body,
                this::CallCreateRatingSuccess,
                this::CallCreateRatingFailed
        );
    }

    private void CallCreateRatingSuccess(Response<BaseResponse> response) {
        new Handler().postDelayed(() -> {
            if(!response.isSuccessful() || response.body() == null){
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
                is_loading.postValue(false);
                return;
            }
            if (response.body().code == StatusCode.CREATED) {
                is_success.postValue(true);
            } else if (response.body().code == StatusCode.NOT_FOUND) {
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
            }
            is_loading.postValue(false);
        }, DelayTime.CALL_API);
    }


    private void CallCreateRatingFailed(Throwable error) {
        is_loading.postValue(false);
        error_call_server.postValue(getApplication().getString(R.string.can_not_connect_to_server));
    }

}
