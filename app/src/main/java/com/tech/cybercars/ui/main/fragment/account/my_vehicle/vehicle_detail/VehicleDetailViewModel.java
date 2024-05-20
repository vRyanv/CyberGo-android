package com.tech.cybercars.ui.main.fragment.account.my_vehicle.vehicle_detail;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.DelayTime;
import com.tech.cybercars.constant.StatusCode;
import com.tech.cybercars.constant.Tag;
import com.tech.cybercars.data.local.AppDBContext;
import com.tech.cybercars.data.models.Vehicle;
import com.tech.cybercars.data.remote.retrofit.ResFailCallback;
import com.tech.cybercars.data.remote.retrofit.ResSuccessCallback;
import com.tech.cybercars.data.remote.vehicle.DeleteVehicleResponse;
import com.tech.cybercars.data.repositories.VehicleRepository;
import com.tech.cybercars.ui.base.BaseViewModel;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class VehicleDetailViewModel extends BaseViewModel {
    public MutableLiveData<Vehicle> vehicle = new MutableLiveData<>();
    private VehicleRepository vehicle_repo;
    public VehicleDetailViewModel(@NonNull Application application) {
        super(application);
        vehicle_repo = VehicleRepository.GetInstance();
    }

    public void HandleDeleteVehicle(){
        is_loading.setValue(true);
        String user_token = SharedPreferencesUtil.GetString(getApplication(), SharedPreferencesUtil.USER_TOKEN_KEY);

        vehicle_repo.DeleteVehicle(
                user_token,
                vehicle.getValue().vehicle_id,
                response -> {
                    new Handler().postDelayed(() -> {
                        is_loading.postValue(false);
                        if(!response.isSuccessful() || response.body() == null){
                            Log.e(Tag.CYBER_DEBUG, "DeleteVehicle: " + response.message() );
                            error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
                            return;
                        }

                        if(response.body().code != StatusCode.DELETED){
                            error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
                            return;
                        }

                        is_success.postValue(true);
                        ExecutorService executor_service = Executors.newSingleThreadExecutor();
                        executor_service.execute(()-> {
                            AppDBContext.GetInstance(getApplication()).VehicleDAO().Delete(response.body().vehicle_id);
                        });
                        executor_service.shutdown();
                    }, DelayTime.CALL_API);
                },
                error -> {
                    is_loading.setValue(false);
                    Log.e(Tag.CYBER_DEBUG, "DeleteVehicle: " + error.getMessage());
                    error_call_server.postValue(getApplication().getString(R.string.can_not_connect_to_server));
                }
        );
    }
}
