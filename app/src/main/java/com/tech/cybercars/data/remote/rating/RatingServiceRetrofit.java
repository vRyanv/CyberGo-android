package com.tech.cybercars.data.remote.rating;

import com.tech.cybercars.constant.URL;
import com.tech.cybercars.data.remote.base.BaseResponse;
import com.tech.cybercars.data.remote.notification.NotificationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RatingServiceRetrofit {
    @GET(URL.RATING_LIST)
    Call<RatingListResponse> GetRatingListRequest(
            @Header("authorization") String user_token,
            @Path("user_id") String user_id
    );

    @POST(URL.CREATE_RATING)
    Call<BaseResponse> CreateRatingRequest(
            @Header("authorization") String user_token,
            @Body MakeRatingBody make_rating_body
    );
}
