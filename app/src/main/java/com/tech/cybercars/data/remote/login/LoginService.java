package com.tech.cybercars.data.remote.login;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {
    @POST("/sign-in/handle")
    Call<LoginResponse> LoginRequest(@Body LoginBody login_body);
}
