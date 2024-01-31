package com.tech.cybercars.data.remote.signin;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SignInServiceRetrofit {
    @POST("/sign-in/handle")
    Call<SignInResponse> LoginRequest(@Body SignInBody login_body);
}
