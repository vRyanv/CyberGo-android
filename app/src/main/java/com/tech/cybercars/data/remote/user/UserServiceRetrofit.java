package com.tech.cybercars.data.remote.user;


import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.data.remote.base.BaseResponse;
import com.tech.cybercars.data.remote.user.driver.DriverRegistrationResponse;
import com.tech.cybercars.data.remote.user.fcm.UpdateFCMBody;
import com.tech.cybercars.data.remote.user.profile.ProfileResponse;
import com.tech.cybercars.data.remote.user.profile.UpdateIdCardResponse;
import com.tech.cybercars.data.remote.user.profile.UpdateProfileResponse;
import com.tech.cybercars.data.remote.user.signin.SignInBody;
import com.tech.cybercars.data.remote.user.signin.SignInResponse;
import com.tech.cybercars.data.remote.user.signup.SignUpBody;
import com.tech.cybercars.data.remote.user.signup.SignUpResponse;
import com.tech.cybercars.data.remote.user.statistic.StatisticResponse;
import com.tech.cybercars.data.remote.user.verification.VerificationBody;
import com.tech.cybercars.data.remote.user.verification.VerificationResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserServiceRetrofit {
    @FormUrlEncoded
    @PUT(URL.UPDATE_PASSWORD)
    Call<BaseResponse> UpdatePasswordRequest(
            @Header("authorization") String user_token,
            @Field(FieldName.CURRENT_PASSWORD) String current_password,
            @Field(FieldName.NEW_PASSWORD) String new_password
    );

    @GET(URL.VIEW_STATISTIC)
    Call<StatisticResponse> ViewStatisticRequest(
            @Header("authorization") String user_token,
            @Path(FieldName.START_DATE) String start_date,
            @Path(FieldName.END_DATE) String end_date
    );

    @FormUrlEncoded
    @POST(URL.RESEND_OTP_CODE)
    Call<BaseResponse> ResendOTPRequest(
            @Field(FieldName.NATIONAL_PHONE) String national_phone
    );

    @GET(URL.VIEW_USER_PROFILE)
    Call<ProfileResponse> ViewUserProfileRequest(
            @Header("authorization") String user_token,
            @Path("user_id") String user_id
    );

    @PUT(URL.UPDATE_FIREBASE_TOKEN)
    Call<BaseResponse> UpdateFirebaseTokenRequest(
            @Header("authorization") String user_token,
            @Body UpdateFCMBody update_firebase_token_body
    );

    @Multipart
    @PUT(URL.UPDATE_ID_CARD)
    Call<UpdateIdCardResponse> UpdateIdCardRequest(
            @Header("authorization") String user_token,
            @Part(FieldName.ID_NUMBER) RequestBody id_number_body,
            @Part List<MultipartBody.Part> id_card_body
    );

    @Multipart
    @PUT(URL.UPDATE_PROFILE)
    Call<UpdateProfileResponse> UpdateProfileRequest(
            @Header("authorization") String user_token,
            @Part MultipartBody.Part avatar_body,
            @Part(FieldName.FULL_NAME) RequestBody full_name_body,
            @Part(FieldName.GENDER) RequestBody gender_body,
            @Part(FieldName.BIRTHDAY) RequestBody birthday_body,
            @Part(FieldName.ID_NUMBER) RequestBody identity_number_body,
            @Part(FieldName.ADDRESS) RequestBody address_body
    );

    @Multipart
    @POST(URL.DRIVER_REGISTRATION)
    Call<DriverRegistrationResponse> DriverRegistrationRequest(
            @Header("authorization") String user_token,
            @Part(FieldName.VEHICLE_NAME) RequestBody vehicle_name_body,
            @Part(FieldName.VEHICLE_TYPE) RequestBody vehicle_type_body,
            @Part(FieldName.LICENSE_PLATES) RequestBody license_plates_body,
            @Part List<MultipartBody.Part> driver_images_body
    );

    @POST(URL.SIGN_IN)
    Call<SignInResponse> LoginRequest(@Body SignInBody login_body);

    @POST(URL.SIGN_UP)
    Call<SignUpResponse> SignUpRequest(@Body SignUpBody sign_up_body);

    @PUT(URL.ACTIVE_ACCOUNT)
    Call<BaseResponse> VerificationRequest(@Body VerificationBody verify_body);

    @GET(URL.PROFILE)
    Call<ProfileResponse> GetProfileInformationRequest(@Header("authorization") String user_token);
}
