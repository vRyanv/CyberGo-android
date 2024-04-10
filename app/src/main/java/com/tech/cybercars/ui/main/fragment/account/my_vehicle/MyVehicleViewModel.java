package com.tech.cybercars.ui.main.fragment.account.my_vehicle;

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
import com.tech.cybercars.data.remote.vehicle.ManyVehicleResponse;
import com.tech.cybercars.data.repositories.VehicleRepository;
import com.tech.cybercars.ui.base.BaseViewModel;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class MyVehicleViewModel extends BaseViewModel {
    public MutableLiveData<List<Vehicle>> vehicle_list = new MutableLiveData<>();
    private final VehicleRepository vehicle_repo;
    private final AppDBContext app_db_context;

    public MyVehicleViewModel(@NonNull Application application) {
        super(application);
        vehicle_repo = VehicleRepository.GetInstance();
        app_db_context = AppDBContext.GetInstance(application);
    }

    public void HandleLoadMyVehicle() {
        is_loading.setValue(true);
        List<Vehicle> vehicles = AppDBContext.GetInstance(getApplication()).VehicleDAO().GetVehicleList();
        if (!vehicles.isEmpty()) {
            vehicle_list.setValue(vehicles);
            is_loading.setValue(false);
            return;
        }

        HandleLoadDataFromServer();
    }

    private void CallLoadVehicleSuccess(Response<ManyVehicleResponse> response) {
        new Handler().postDelayed(() -> {
            if (!response.isSuccessful() || response.body() == null) {
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
                is_loading.postValue(false);
                return;
            }
            if (response.body().getCode() == StatusCode.OK) {
                vehicle_list.postValue(response.body().vehicle_list);
                ExecutorService executor_service = Executors.newSingleThreadExecutor();
                executor_service.execute(() -> {
                    app_db_context.VehicleDAO().ClearTable();
                    app_db_context.VehicleDAO().InsertManyVehicle(response.body().vehicle_list);
                });
                executor_service.shutdown();
            }
            is_loading.postValue(false);
        }, DelayTime.CALL_API);
    }

    private void CallLoadVehicleFail(Throwable throwable) {
        new Handler().postDelayed(() -> {
            is_loading.postValue(false);
            error_call_server.postValue(getApplication().getString(R.string.can_not_connect_to_server));
            Log.e(Tag.CYBER_DEBUG, String.valueOf(throwable.getMessage()));
        }, DelayTime.CALL_API);
    }

    public void HandleLoadDataFromServer() {
        String token = SharedPreferencesUtil.GetString(getApplication(), SharedPreferencesUtil.USER_TOKEN_KEY);
        vehicle_repo.GetVehicleList(
                token,
                this::CallLoadVehicleSuccess,
                this::CallLoadVehicleFail
        );
    }

}
