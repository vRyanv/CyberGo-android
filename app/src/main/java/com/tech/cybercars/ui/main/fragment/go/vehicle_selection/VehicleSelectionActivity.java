package com.tech.cybercars.ui.main.fragment.go.vehicle_selection;

import android.app.Dialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tech.cybercars.R;
import com.tech.cybercars.adapter.vehicles.VehicleAdapter;
import com.tech.cybercars.constant.DestinationType;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.data.models.Vehicle;
import com.tech.cybercars.databinding.ActivityVehicleSelectionBinding;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.component.dialog.NotificationDialog;
import com.tech.cybercars.ui.main.fragment.go.share_trip.multiple_destination.MultipleShareTripActivity;
import com.tech.cybercars.ui.main.fragment.go.share_trip.single_destination.SingleShareTripActivity;

import java.util.ArrayList;

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
//            DestinationTypeSelectionDialog type_selection_dialog = new DestinationTypeSelectionDialog(this);
//            type_selection_dialog.SetTypeSelectionCallback((dialog, destination_type) -> {
//                StartShareTripActivity(destination_type, vehicle);
//
//                dialog.dismiss();
//            });
//            type_selection_dialog.show();
            Intent share_trip_intent = new Intent(this, SingleShareTripActivity.class);
            share_trip_intent.putExtra(FieldName.VEHICLE, vehicle);
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
            if (is_loading) {
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

    private void StartShareTripActivity(String destination_type, Vehicle vehicle) {
        Intent share_trip_intent;
        if (destination_type.equals(DestinationType.MULTIPLE)) {
            share_trip_intent = new Intent(this, MultipleShareTripActivity.class);
        } else {
            share_trip_intent = new Intent(this, SingleShareTripActivity.class);
        }
        share_trip_intent.putExtra(FieldName.VEHICLE, vehicle);
        startActivity(share_trip_intent);
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