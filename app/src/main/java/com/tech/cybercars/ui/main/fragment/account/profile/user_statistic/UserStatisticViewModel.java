package com.tech.cybercars.ui.main.fragment.account.profile.user_statistic;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;
import com.tech.cybercars.R;
import com.tech.cybercars.constant.DelayTime;
import com.tech.cybercars.constant.StatusCode;
import com.tech.cybercars.data.models.User;
import com.tech.cybercars.data.remote.user.profile.ProfileResponse;
import com.tech.cybercars.data.remote.user.statistic.StatisticResponse;
import com.tech.cybercars.data.repositories.UserRepository;
import com.tech.cybercars.ui.base.BaseViewModel;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class UserStatisticViewModel extends BaseViewModel {
    public MutableLiveData<ArrayList<PieEntry>> revenue_of_vehicle = new MutableLiveData<>();
    public MutableLiveData<ArrayList<BarEntry>> trip_quantity_of_vehicle = new MutableLiveData<>();
    public MutableLiveData<String> start_date = new MutableLiveData<>();
    public MutableLiveData<String> end_date = new MutableLiveData<>();

    private final UserRepository user_repo;
    public UserStatisticViewModel(@NonNull Application application) {
        super(application);
        user_repo = UserRepository.GetInstance();
    }

    public void HandleGetStatistic(){
        is_loading.setValue(true);
        String user_token = SharedPreferencesUtil.GetString(getApplication(), SharedPreferencesUtil.USER_TOKEN_KEY);

        user_repo.ViewStatistic(
                user_token,
                start_date.getValue(),
                end_date.getValue(),
                this::CallViewStatisticSuccess,
                this::CallViewStatisticFailed
        );
    }

    private void CallViewStatisticSuccess(Response<StatisticResponse> response) {
        new Handler().postDelayed(() -> {
            if(!response.isSuccessful() || response.body() == null){
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
                is_loading.postValue(false);
                return;
            }
            if (response.body().code != StatusCode.OK) {
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
                is_loading.postValue(false);
                return;
            }

            ExecutorService executor_service = Executors.newSingleThreadExecutor();
            executor_service.execute(()-> {
                   trip_quantity_of_vehicle.postValue(CreateTripQuantityData(response.body().trip_quantity_of_vehicle));
                   revenue_of_vehicle.postValue(CreateRevenueVehicleData(response.body().revenue_of_vehicle));
                   is_loading.postValue(false);
            });
            executor_service.shutdown();
        }, DelayTime.CALL_API);
    }

    private ArrayList<BarEntry> CreateTripQuantityData(StatisticResponse.Quantity trip_quantity){
        ArrayList<BarEntry> data = new ArrayList<>();
        data.add(new BarEntry(0,trip_quantity.moto));
        data.add(new BarEntry(1,trip_quantity.car));
        data.add(new BarEntry(2,trip_quantity.truck));
        return data;
    }

    private ArrayList<PieEntry> CreateRevenueVehicleData(StatisticResponse.Quantity revenue){
        ArrayList<PieEntry> data = new ArrayList<>();
        data.add(new PieEntry(revenue.moto,"Moto"));
        data.add(new PieEntry(revenue.car,"Car"));
        data.add(new PieEntry(revenue.truck,"Truck"));
        return data;
    }


    private void CallViewStatisticFailed(Throwable error) {
        is_loading.postValue(false);
        error_call_server.postValue(getApplication().getString(R.string.can_not_connect_to_server));
    }
}
