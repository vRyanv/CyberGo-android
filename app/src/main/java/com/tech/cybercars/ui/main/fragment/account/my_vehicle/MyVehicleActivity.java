package com.tech.cybercars.ui.main.fragment.account.my_vehicle;

import android.app.Dialog;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tech.cybercars.R;
import com.tech.cybercars.adapter.vehicles.VehicleAdapter;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.Tag;
import com.tech.cybercars.databinding.ActivityMyVehicleBinding;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.component.dialog.NotificationDialog;
import com.tech.cybercars.ui.main.fragment.account.my_vehicle.vehicle_detail.VehicleDetailActivity;

import java.util.ArrayList;

public class MyVehicleActivity extends BaseActivity<ActivityMyVehicleBinding, MyVehicleViewModel> {
    private VehicleAdapter vehicle_adapter;
    @NonNull
    @Override
    protected MyVehicleViewModel InitViewModel() {
        return new ViewModelProvider(this).get(MyVehicleViewModel.class);
    }

    @Override
    protected ActivityMyVehicleBinding InitBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_vehicle);
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
        binding.rcvMyVehicle.setLayoutManager(layoutManager);
        binding.rcvMyVehicle.setAdapter(vehicle_adapter);
        vehicle_adapter.SetOnClickListener(vehicle -> {
            Intent vehicle_detail_intent = new Intent(this, VehicleDetailActivity.class);
            vehicle_detail_intent.putExtra(FieldName.VEHICLE, vehicle);
            startActivity(vehicle_detail_intent);
        });

        binding.swipeRefresh.setOnRefreshListener(() -> {
            view_model.HandleLoadDataFromServer();
        });

        binding.headerPrimary.btnOutScreen.setOnClickListener(view -> {
            OnBackPress();
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
        view_model.HandleLoadMyVehicle();
    }

    @Override
    protected void OnBackPress() {
        finish();
    }

    public void ShowErrorCallServer(String error_call_server) {
        NotificationDialog.Builder(this)
                .SetIcon(R.drawable.ic_error)
                .SetTitle(getResources().getString(R.string.something_went_wrong))
                .SetSubtitle(error_call_server)
                .SetTextMainButton(getResources().getString(R.string.close))
                .SetOnMainButtonClicked(Dialog::dismiss).show();
    }
}