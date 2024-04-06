package com.tech.cybercars.ui.main.fragment.account.driver_register;

import android.app.Application;
import android.net.Uri;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.DelayTime;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.StatusCode;
import com.tech.cybercars.data.remote.user.driver.DriverRegistrationResponse;
import com.tech.cybercars.data.repositories.UserRepository;
import com.tech.cybercars.ui.base.BaseViewModel;
import com.tech.cybercars.utils.FileUtil;
import com.tech.cybercars.utils.RealPathUtil;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class DriverRegistrationViewModel extends BaseViewModel {
    public MutableLiveData<String> vehicle_type = new MutableLiveData<>();
    public MutableLiveData<String> vehicle_name = new MutableLiveData<>();
    public MutableLiveData<String> license_plates = new MutableLiveData<>();
    public MutableLiveData<String> id_number = new MutableLiveData<>();
    public MutableLiveData<Boolean> is_lost_image_error = new MutableLiveData<>();
    public Uri front_vehicle_registration_certificate_uri, back_vehicle_registration_certificate_uri;
    public Uri front_driving_licence_uri, back_driving_licence_uri;
    public Uri front_vehicle_uri, back_vehicle_uri, right_vehicle_uri, left_vehicle_uri;
    private final UserRepository user_repository = UserRepository.GetInstance();

    public DriverRegistrationViewModel(@NonNull Application application) {
        super(application);
    }

    public void DriverRegistrationHandle() throws IOException {
        boolean is_error = false;
        Uri[] uri_arr = new Uri[]{
                front_vehicle_registration_certificate_uri,
                back_vehicle_registration_certificate_uri,
                front_driving_licence_uri,
                back_driving_licence_uri,
                front_vehicle_uri,
                back_vehicle_uri,
                right_vehicle_uri,
                left_vehicle_uri
        };

        for (Uri uri : uri_arr) {
            if (uri == null) {
                is_error = true;
                break;
            }
        }

        if(vehicle_name.getValue() == null || vehicle_name.getValue().equals("")){
            is_error = true;
        }

        if(license_plates.getValue() == null || license_plates.getValue().equals("")){
            is_error = true;
        }

        if(is_error){
            is_lost_image_error.setValue(true);
            return;
        }

        String[] img_field_arr = new String[]{
                FieldName.FRONT_VEHICLE_REGISTRATION_CERTIFICATE,
                FieldName.BACK_VEHICLE_REGISTRATION_CERTIFICATE,
                FieldName.FRONT_DRIVING_LICENSE,
                FieldName.BACK_DRIVING_LICENSE,
                FieldName.FRONT_VEHICLE,
                FieldName.BACK_VEHICLE,
                FieldName.RIGHT_VEHICLE,
                FieldName.LEFT_VEHICLE,
        };

        is_loading.postValue(true);
        RequestBody license_plates_body = RequestBody.create(MultipartBody.FORM, license_plates.getValue());
        RequestBody vehicle_name_body = RequestBody.create(MultipartBody.FORM, vehicle_name.getValue());
        RequestBody vehicle_type_body = RequestBody.create(MultipartBody.FORM, vehicle_type.getValue());

        List<MultipartBody.Part> driver_images_body = new ArrayList<>();
        for (int i = 0; i < uri_arr.length; i++) {
            File file = new File(RealPathUtil.getRealPath(getApplication(), uri_arr[i]));
            String mime_type = "image/" + FileUtil.GetFileExtension(file.getName());
            RequestBody request_body = RequestBody.create(MediaType.parse(mime_type), file);
            driver_images_body.add(MultipartBody.Part.createFormData(img_field_arr[i], file.getName(), request_body));
        }

        String user_token = SharedPreferencesUtil.GetString(getApplication(), SharedPreferencesUtil.USER_TOKEN_KEY);
        user_repository.CreateDriverRegistration(
                user_token,
                vehicle_name_body,
                vehicle_type_body,
                license_plates_body,
                driver_images_body,
                this::CallCreateDriverRegistrationSuccess,
                this::CallCreateDriverRegistrationFailed
        );
    }

    private void CallCreateDriverRegistrationSuccess(Response<DriverRegistrationResponse> response) {
        new Handler().postDelayed(()->{
            if(!response.isSuccessful() || response.body() == null){
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
                is_loading.postValue(false);
                return;
            }
            if (response.body().getCode() == StatusCode.CREATED) {
                is_success.postValue(true);
            } else if (response.body().getCode() == StatusCode.BAD_REQUEST) {
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
            }

            is_loading.postValue(false);
        }, DelayTime.CALL_API);
    }

    private void CallCreateDriverRegistrationFailed(Throwable error) {
        is_loading.postValue(false);
        error_call_server.postValue(getApplication().getString(R.string.can_not_connect_to_server));
    }
}
