package com.tech.cybercars.data.repositories;

import com.tech.cybercars.data.remote.base.BaseResponse;
import com.tech.cybercars.data.remote.retrofit.ResFailCallback;
import com.tech.cybercars.data.remote.retrofit.ResSuccessCallback;
import com.tech.cybercars.data.remote.retrofit.RetrofitRequest;
import com.tech.cybercars.data.remote.retrofit.RetrofitResponse;
import com.tech.cybercars.data.remote.security.SecurityServiceRetrofit;
import com.tech.cybercars.data.remote.user.UserServiceRetrofit;

public class SecurityRepository {
    private final SecurityServiceRetrofit security_service;
    private static SecurityRepository security_repository;

    public static SecurityRepository GetInstance() {
        if (security_repository == null) {
            security_repository = new SecurityRepository();
        }
        return security_repository;
    }

    private SecurityRepository() {
        security_service = RetrofitRequest.getInstance().create(SecurityServiceRetrofit.class);
    }

    public void ResetPassword(String otp_code, String email, String new_pass,ResSuccessCallback<BaseResponse> success_callback, ResFailCallback fail_callback){
        security_service.ResetPasswordRequest(otp_code, email, new_pass)
                .enqueue(new RetrofitResponse<BaseResponse>().GetResponse(success_callback, fail_callback));
    }
    public void ForgotPassword(String email, ResSuccessCallback<BaseResponse> success_callback, ResFailCallback fail_callback){
        security_service.ForgotPasswordRequest(email)
                .enqueue(new RetrofitResponse<BaseResponse>().GetResponse(success_callback, fail_callback));
    }

}
