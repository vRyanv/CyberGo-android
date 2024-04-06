package com.tech.cybercars.ui.main.fragment.go.vehicle_selection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tech.cybercars.R;
import com.tech.cybercars.adapter.notification.NotificationAdapter;
import com.tech.cybercars.adapter.vehicles.VehicleAdapter;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.VehicleType;
import com.tech.cybercars.data.models.Notification;
import com.tech.cybercars.data.models.Vehicle;
import com.tech.cybercars.databinding.ActivityVehicleSelectionBinding;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.component.dialog.NotificationDialog;
import com.tech.cybercars.ui.main.MainViewModel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class VehicleSelectionActivity extends BaseActivity<ActivityVehicleSelectionBinding, VehicleSelectionViewModel> {
    private VehicleAdapter vehicle_adapter;

    @NonNull
    @Override
    protected VehicleSelectionViewModel InitViewModel() {
        return new ViewModelProvider(this).get(VehicleSelectionViewModel.class);
    }

    @Override
    protected ActivityVehicleSelectionBinding InitBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vehicle_selection);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {
        vehicle_adapter = new VehicleAdapter(this, new ArrayList<>());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rcvVehicle.setLayoutManager(layoutManager);
        binding.rcvVehicle.setAdapter(vehicle_adapter);
        vehicle_adapter.SetOnClickListener(vehicle -> {
            Intent share_trip_intent = new Intent();
            share_trip_intent.putExtra(FieldName.VEHICLE_TYPE, vehicle.vehicle_type);
            startActivity(share_trip_intent);
        });

        binding.headerPrimary.btnOutScreen.setOnClickListener(view -> {
            OnBackPress();
        });

        binding.swipeRefresh.setOnRefreshListener(() -> {
            view_model.LoadDataFromServer();
        });
    }

    @Override
    protected void InitObserve() {
        view_model.vehicle_list.observe(this, vehicle_list -> {
            vehicle_adapter.UpdateData(vehicle_list);
        });

        view_model.is_loading.observe(this, is_loading -> {
            if(is_loading){
                binding.skeletonLoading.startShimmerAnimation();
            } else {
                binding.swipeRefresh.setRefreshing(false);
                binding.skeletonLoading.stopShimmerAnimation();
            }
        });

        view_model.error_call_server.observe(this, this::ShowErrorCallServer);
    }

    @Override
    protected void InitCommon() {
        view_model.HandleLoadVehicle();
    }

    @Override
    protected void OnBackPress() {
        finish();
    }

    private void ShowErrorCallServer(String error_call_server) {
        NotificationDialog.Builder(this)
                .SetIcon(R.drawable.ic_error)
                .SetTitle(getResources().getString(R.string.something_went_wrong))
                .SetSubtitle(error_call_server)
                .SetTextMainButton(getResources().getString(R.string.close))
                .SetOnMainButtonClicked(Dialog::dismiss).show();
    }
}