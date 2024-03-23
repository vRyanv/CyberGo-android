package com.tech.cybercars.data.remote.user;


import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.data.remote.base.BaseResponse;
import com.tech.cybercars.data.remote.user.driver.DriverRegistrationResponse;
import com.tech.cybercars.data.remote.user.profile.ProfileResponse;
import com.tech.cybercars.data.remote.user.profile.UpdateIdCardResponse;
import com.tech.cybercars.data.remote.user.profile.UpdateProfileResponse;
import com.tech.cybercars.data.remote.user.signin.SignInBody;
import com.tech.cybercars.data.remote.user.signin.SignInResponse;
import com.tech.cybercars.data.remote.user.signup.SignUpBody;
import com.tech.cybercars.data.remote.user.signup.SignUpResponse;
import com.tech.cybercars.data.remote.user.verification.VerificationBody;
import com.tech.cybercars.data.remote.user.verification.VerificationResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface UserServiceRetrofit {
    @Multipart
    @PUT(URL.UPDATE_ID_CARD)
    Call<UpdateIdCardResponse> UpdateIdCardRequest(
            @Header("authorization") String user_token,
            @Part List<MultipartBody.Part> id_card_body
    );
    @Multipart
    @PUT(URL.UPDATE_PROFILE)
    Call<UpdateProfileResponse> UpdateProfileRequest(
            @Header("authorization") String user_token,
            @Part MultipartBody.Part avatar_body,
            @Part(FieldName.FULL_NAME) RequestBody full_name_body,
            @Part(FieldName.GENDER) RequestBody gender_body,
            @Part(FieldName.ID_NUMBER) RequestBody identity_number_body,
            @Part(FieldName.ADDRESS) RequestBody address_body
    );

    @Multipart
    @POST(URL.DRIVER_REGISTRATION)
    Call<DriverRegistrationResponse> DriverRegistrationRequest(
            @Header("authorization") String user_token,
            @Part(FieldName.VEHICLE_TYPE) RequestBody vehicle_type_body,
            @Part(FieldName.LICENSE_PLATES) RequestBody license_plates_body,
            @Part List<MultipartBody.Part> driver_images_body
    );

    @POST(URL.SIGN_IN)
    Call<SignInResponse> LoginRequest(@Body SignInBody login_body);

    @POST(URL.SIGN_UP)
    Call<SignUpResponse> SignUpRequest(@Body SignUpBody sign_up_body);

    @PUT(URL.ACTIVE_ACCOUNT)
    Call<VerificationResponse> VerificationRequest(@Body VerificationBody verify_body);

    @GET(URL.PROFILE)
    Call<ProfileResponse> GetProfileInformationRequest(@Header("authorization") String user_token);
}
