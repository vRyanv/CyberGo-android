package com.tech.cybercars.data.repositories;

import com.tech.cybercars.data.remote.api.ResFailCallback;
import com.tech.cybercars.data.remote.api.RetrofitRequest;
import com.tech.cybercars.data.remote.api.RetrofitResponse;
import com.tech.cybercars.data.remote.api.ResSuccessCallback;
import com.tech.cybercars.data.remote.signin.SignInBody;
import com.tech.cybercars.data.remote.signin.SignInResponse;
import com.tech.cybercars.data.remote.signin.SignInServiceRetrofit;
import com.tech.cybercars.data.remote.signup.SignUpBody;
import com.tech.cybercars.data.remote.signup.SignUpResponse;
import com.tech.cybercars.data.remote.signup.SignUpServiceRetrofit;
import com.tech.cybercars.data.remote.verification.VerificationBody;
import com.tech.cybercars.data.remote.verification.VerificationResponse;
import com.tech.cybercars.data.remote.verification.VerificationServiceRetrofit;

public class UserRepository {
    private final SignInServiceRetrofit sign_in_service;
    private final SignUpServiceRetrofit sign_up_service;
    private final VerificationServiceRetrofit verify_service;
    private static UserRepository user_repository;
    public static UserRepository GetInstance(){
        if(user_repository == null){
            user_repository = new UserRepository();
        }
        return user_repository;
    }

    private UserRepository() {
        sign_in_service = RetrofitRequest.getInstance().create(SignInServiceRetrofit.class);
        sign_up_service = RetrofitRequest.getInstance().create(SignUpServiceRetrofit.class);
        verify_service = RetrofitRequest.getInstance().create(VerificationServiceRetrofit.class);
    }

    public void UserSignIn(SignInBody sign_in_body, ResSuccessCallback<SignInResponse> success_callback, ResFailCallback fail_callback){
        sign_in_service.LoginRequest(sign_in_body).enqueue(
                new RetrofitResponse<SignInResponse>().GetResponse(success_callback, fail_callback)
        );
    }

    public void CreateUser(SignUpBody sign_up_body, ResSuccessCallback<SignUpResponse> success_callback, ResFailCallback fail_callback) {
        sign_up_service.SignUpRequest(sign_up_body).enqueue(
                new RetrofitResponse<SignUpResponse>().GetResponse(success_callback, fail_callback)
        );
    }

    public void ActivateUser(VerificationBody verify_body, ResSuccessCallback<VerificationResponse>  success_callback, ResFailCallback fail_callback){
        verify_service.VerificationRequest(verify_body).enqueue(
                new RetrofitResponse<VerificationResponse>().GetResponse(success_callback, fail_callback)
        );
    }
}
