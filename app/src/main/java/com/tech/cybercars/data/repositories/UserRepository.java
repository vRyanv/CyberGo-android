package com.tech.cybercars.data.repositories;

import com.tech.cybercars.data.remote.base.BaseResponse;
import com.tech.cybercars.data.remote.retrofit.ResFailCallback;
import com.tech.cybercars.data.remote.retrofit.ResSuccessCallback;
import com.tech.cybercars.data.remote.retrofit.RetrofitRequest;
import com.tech.cybercars.data.remote.retrofit.RetrofitResponse;
import com.tech.cybercars.data.remote.user.UserServiceRetrofit;
import com.tech.cybercars.data.remote.user.driver.DriverRegistrationResponse;
import com.tech.cybercars.data.remote.user.fcm.UpdateFCMBody;
import com.tech.cybercars.data.remote.user.profile.ProfileResponse;
import com.tech.cybercars.data.remote.user.profile.UpdateIdCardResponse;
import com.tech.cybercars.data.remote.user.profile.UpdateProfileResponse;
import com.tech.cybercars.data.remote.user.signin.SignInBody;
import com.tech.cybercars.data.remote.user.signin.SignInResponse;
import com.tech.cybercars.data.remote.user.signup.SignUpBody;
import com.tech.cybercars.data.remote.user.signup.SignUpResponse;
import com.tech.cybercars.data.remote.user.statistic.StatisticBody;
import com.tech.cybercars.data.remote.user.statistic.StatisticResponse;
import com.tech.cybercars.data.remote.user.verification.VerificationBody;
import com.tech.cybercars.data.remote.user.verification.VerificationResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UserRepository {
    private final UserServiceRetrofit user_service;
    private static UserRepository user_repository;

    public static UserRepository GetInstance() {
        if (user_repository == null) {
            user_repository = new UserRepository();
        }
        return user_repository;
    }

    private UserRepository() {
        user_service = RetrofitRequest.getInstance().create(UserServiceRetrofit.class);
    }

    public void UpdatePassword(String user_token, String current_password, String new_password, ResSuccessCallback<BaseResponse> success_callback, ResFailCallback fail_callback){
        user_service.UpdatePasswordRequest(user_token,current_password, new_password)
                .enqueue(new RetrofitResponse<BaseResponse>().GetResponse(success_callback, fail_callback));
    }

    public void ViewStatistic(String user_token, String start_date, String end_date, ResSuccessCallback<StatisticResponse> success_callback, ResFailCallback fail_callback){
        user_service.ViewStatisticRequest(user_token, start_date, end_date)
                .enqueue(new RetrofitResponse<StatisticResponse>().GetResponse(success_callback, fail_callback));
    }

    public void ResendOTP(String national_phone, ResSuccessCallback<BaseResponse> success_callback, ResFailCallback fail_callback) {
        user_service.ResendOTPRequest(national_phone)
                .enqueue(new RetrofitResponse<BaseResponse>().GetResponse(success_callback, fail_callback));
    }

    public void ViewUserProfile(String user_token, String user_id, ResSuccessCallback<ProfileResponse> success_callback, ResFailCallback fail_callback) {
        user_service.ViewUserProfileRequest(user_token, user_id)
                .enqueue(new RetrofitResponse<ProfileResponse>().GetResponse(success_callback, fail_callback));
    }

    public void UpdateFirebaseToken(String user_token, UpdateFCMBody update_firebase_token_body, ResSuccessCallback<BaseResponse> success_callback, ResFailCallback fail_callback) {
        user_service.UpdateFirebaseTokenRequest(user_token, update_firebase_token_body)
                .enqueue(new RetrofitResponse<BaseResponse>().GetResponse(success_callback, fail_callback));
    }

    public void UpdateIdCard(String user_token,
                             RequestBody id_number_body,
                             List<MultipartBody.Part> id_card_body,
                             ResSuccessCallback<UpdateIdCardResponse> success_callback, ResFailCallback fail_callback) {
        user_service.UpdateIdCardRequest(user_token, id_number_body, id_card_body)
                .enqueue(new RetrofitResponse<UpdateIdCardResponse>().GetResponse(success_callback, fail_callback));
    }

    public void UpdateProfileInformation(String user_token,
                                         MultipartBody.Part avatar_body,
                                         RequestBody full_name_body,
                                         RequestBody gender_body,
                                         RequestBody birthday_body,
                                         RequestBody identity_number,
                                         RequestBody address,
                                         ResSuccessCallback<UpdateProfileResponse> success_callback, ResFailCallback fail_callback) {
        user_service.UpdateProfileRequest(user_token, avatar_body, full_name_body, gender_body, birthday_body, identity_number, address)
                .enqueue(new RetrofitResponse<UpdateProfileResponse>().GetResponse(success_callback, fail_callback));
    }

    public void GetProfileInformation(String user_token, ResSuccessCallback<ProfileResponse> success_callback, ResFailCallback fail_callback) {
        user_service.GetProfileInformationRequest(user_token).enqueue(
                new RetrofitResponse<ProfileResponse>().GetResponse(success_callback, fail_callback)
        );
    }

    public void UserSignIn(SignInBody sign_in_body, ResSuccessCallback<SignInResponse> success_callback, ResFailCallback fail_callback) {
        user_service.LoginRequest(sign_in_body).enqueue(
                new RetrofitResponse<SignInResponse>().GetResponse(success_callback, fail_callback)
        );
    }

    public void CreateUser(SignUpBody sign_up_body, ResSuccessCallback<SignUpResponse> success_callback, ResFailCallback fail_callback) {
        user_service.SignUpRequest(sign_up_body).enqueue(
                new RetrofitResponse<SignUpResponse>().GetResponse(success_callback, fail_callback)
        );
    }

    public void ActivateUser(VerificationBody verify_body, ResSuccessCallback<BaseResponse> success_callback, ResFailCallback fail_callback) {
        user_service.VerificationRequest(verify_body).enqueue(
                new RetrofitResponse<BaseResponse>().GetResponse(success_callback, fail_callback)
        );
    }

    public void CreateDriverRegistration(String user_token, RequestBody vehicle_name_body, RequestBody vehicle_type_body, RequestBody license_plates_body, List<MultipartBody.Part> driver_images_body, ResSuccessCallback<DriverRegistrationResponse> success_callback, ResFailCallback fail_callback) {
        user_service.DriverRegistrationRequest(user_token, vehicle_name_body, vehicle_type_body, license_plates_body, driver_images_body).enqueue(
                new RetrofitResponse<DriverRegistrationResponse>().GetResponse(success_callback, fail_callback)
        );
    }
}
