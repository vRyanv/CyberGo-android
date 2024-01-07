package com.tech.cybercars.data.remote.signin;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SignInService {
    @POST("/sign-in/handle")
    Call<SignInResponse> LoginRequest(@Body SignInBody login_body);
}
