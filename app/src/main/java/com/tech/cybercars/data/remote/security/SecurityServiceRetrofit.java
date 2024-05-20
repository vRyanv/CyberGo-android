package com.tech.cybercars.data.remote.security;

import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.data.remote.base.BaseResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface SecurityServiceRetrofit {
    @FormUrlEncoded
    @PUT(URL.RESET_PASSWORD)
    Call<BaseResponse> ResetPasswordRequest(
            @Field(FieldName.OTP_CODE) String otp_code,
            @Field(FieldName.EMAIL) String email,
            @Field(FieldName.NEW_PASSWORD) String new_pass
    );
    @FormUrlEncoded
    @POST(URL.FORGOT_PASSWORD)
    Call<BaseResponse> ForgotPasswordRequest(@Field(FieldName.EMAIL) String email);
}
