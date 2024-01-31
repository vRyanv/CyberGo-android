package com.tech.cybercars.data.remote.signup;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SignUpServiceRetrofit {
    @POST("/sign-up/handle")
    Call<SignUpResponse> SignUpRequest(@Body SignUpBody sign_up_body);
}
