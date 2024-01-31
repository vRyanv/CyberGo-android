package com.tech.cybercars.data.remote.verification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

public interface VerificationServiceRetrofit {
    @PUT("/sign-up/activate-account")
    Call<VerificationResponse> VerificationRequest(@Body VerificationBody verify_body);
}
