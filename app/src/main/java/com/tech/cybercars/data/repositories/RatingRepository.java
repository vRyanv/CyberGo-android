package com.tech.cybercars.data.repositories;

import com.tech.cybercars.data.remote.base.BaseResponse;
import com.tech.cybercars.data.remote.notification.NotificationResponse;
import com.tech.cybercars.data.remote.notification.NotificationServiceRetrofit;
import com.tech.cybercars.data.remote.rating.MakeRatingBody;
import com.tech.cybercars.data.remote.rating.RatingListResponse;
import com.tech.cybercars.data.remote.rating.RatingServiceRetrofit;
import com.tech.cybercars.data.remote.retrofit.ResFailCallback;
import com.tech.cybercars.data.remote.retrofit.ResSuccessCallback;
import com.tech.cybercars.data.remote.retrofit.RetrofitRequest;
import com.tech.cybercars.data.remote.retrofit.RetrofitResponse;

public class RatingRepository {
    private final RatingServiceRetrofit rating_service;
    private static RatingRepository rating_repository;
    public static RatingRepository GetInstance(){
        if(rating_repository == null){
            rating_repository = new RatingRepository();
        }
        return rating_repository;
    }

    private RatingRepository() {
        rating_service = RetrofitRequest.getInstance().create(RatingServiceRetrofit.class);
    }

    public void GetRatingList(String user_token, String user_id, ResSuccessCallback<RatingListResponse> success_callback, ResFailCallback fail_callback){
        rating_service.GetRatingListRequest(user_token, user_id)
                .enqueue(new RetrofitResponse<RatingListResponse>().GetResponse(success_callback, fail_callback));
    }

    public void CreateRating(String user_token, MakeRatingBody make_rating_body, ResSuccessCallback<BaseResponse> success_callback, ResFailCallback fail_callback){
        rating_service.CreateRatingRequest(user_token, make_rating_body)
                .enqueue(new RetrofitResponse<BaseResponse>().GetResponse(success_callback, fail_callback));
    }
}
