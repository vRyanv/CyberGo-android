package com.tech.cybercars.data.remote.verification;

import com.tech.cybercars.data.remote.signup.SignUpResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface VerificationService {
    @PUT("/sign-up/activate-account")
    Call<VerificationResponse> VerificationRequest(@Body VerificationBody verify_body);
}
