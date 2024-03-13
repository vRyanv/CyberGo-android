package com.tech.cybercars.ui.main.fragment.setting.driver_register;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.StatusCode;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.data.remote.user.driver.DriverRegistrationResponse;
import com.tech.cybercars.data.repositories.UserRepository;
import com.tech.cybercars.ui.base.BaseViewModel;
import com.tech.cybercars.utils.FileUtil;
import com.tech.cybercars.utils.RealPathUtil;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import timber.log.Timber;

public class DriverRegistrationViewModel extends BaseViewModel {
    public MutableLiveData<String> license_plates = new MutableLiveData<>();
    public MutableLiveData<String> id_number = new MutableLiveData<>();
    public MutableLiveData<Boolean> is_lost_image_error = new MutableLiveData<>();
    public Uri front_id_card_uri, back_id_card_uri, driver_avatar_uri, curriculum_vitae_uri;
    public Uri front_driving_license_uri, back_driving_license_uri;
    public Uri front_transport_uri, back_transport_uri, right_transport_uri, left_transport_uri;
    private final UserRepository user_repository = UserRepository.GetInstance();

    public DriverRegistrationViewModel(@NonNull Application application) {
        super(application);
    }

    public void DriverRegistrationHandle() throws IOException {
        Uri[] uri_arr = new Uri[]{
                front_id_card_uri,
                back_id_card_uri,
                driver_avatar_uri,
                curriculum_vitae_uri,
                front_driving_license_uri,
                back_driving_license_uri,
                front_transport_uri,
                back_transport_uri,
                right_transport_uri,
                left_transport_uri
        };

        for (Uri uri : uri_arr) {
            if (uri == null) {
                is_lost_image_error.setValue(true);
                return;
            }
        }

        String[] field_arr = new String[]{
                FieldName.FRONT_ID_CARD,
                FieldName.BACK_ID_CARD,
                FieldName.DRIVER_AVATAR,
                FieldName.CURRICULUM_VITAE,
                FieldName.FRONT_DRIVING_LICENSE,
                FieldName.BACK_DRIVING_LICENSE,
                FieldName.FRONT_TRANSPORT,
                FieldName.BACK_TRANSPORT,
                FieldName.RIGHT_TRANSPORT,
                FieldName.LEFT_TRANSPORT,
        };

        is_loading.postValue(true);
        List<MultipartBody.Part> driver_images_body = new ArrayList<>();
        RequestBody id_number_body = RequestBody.create(MultipartBody.FORM, id_number.getValue());
        RequestBody license_plates_body = RequestBody.create(MultipartBody.FORM, license_plates.getValue());
        for (int i = 0; i < uri_arr.length; i++) {
            File file = new File(RealPathUtil.getRealPath(getApplication(), uri_arr[i]));
            String mime_type = "image/" + FileUtil.GetFileExtension(file.getName());
            RequestBody request_body = RequestBody.create(MediaType.parse(mime_type), file);
            driver_images_body.add(MultipartBody.Part.createFormData(field_arr[i], file.getName(), request_body));
        }

        String user_token = SharedPreferencesUtil.GetUserToken(getApplication());
        user_repository.CreateDriverRegistration(
                user_token,
                id_number_body,
                license_plates_body,
                driver_images_body,
                this::CallCreateDriverRegistrationSuccess,
                this::CallCreateDriverRegistrationFailed
        );
    }

    private void CallCreateDriverRegistrationSuccess(Response<DriverRegistrationResponse> response) {
        is_loading.postValue(false);
        if (response.body().getCode() == StatusCode.CREATED) {
            is_success.postValue(true);
        } else if (response.body().getCode() == StatusCode.BAD_REQUEST) {
            error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
        }
    }

    private void CallCreateDriverRegistrationFailed(Throwable error) {
        is_loading.postValue(false);
        error_call_server.postValue(getApplication().getString(R.string.can_not_connect_to_server));
    }


    @Override
    public void ResetViewModel() {

    }
}
