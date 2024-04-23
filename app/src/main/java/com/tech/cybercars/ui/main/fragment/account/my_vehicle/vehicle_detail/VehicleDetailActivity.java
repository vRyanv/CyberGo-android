package com.tech.cybercars.ui.main.fragment.account.my_vehicle.vehicle_detail;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayoutMediator;
import com.tech.cybercars.R;
import com.tech.cybercars.adapter.paper.VehicleDetailAdapter;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.VehicleStatus;
import com.tech.cybercars.data.models.Vehicle;
import com.tech.cybercars.databinding.ActivityVehicleDetailBinding;
import com.tech.cybercars.ui.base.BaseActivity;

public class VehicleDetailActivity extends BaseActivity<ActivityVehicleDetailBinding, VehicleDetailViewModel> {

    @NonNull
    @Override
    protected VehicleDetailViewModel InitViewModel() {
        return new ViewModelProvider(this).get(VehicleDetailViewModel.class);
    }

    @Override
    protected ActivityVehicleDetailBinding InitBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vehicle_detail);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {
        binding.btnOutScreen.setOnClickListener(view -> {
            OnBackPress();
        });
    }

    @Override
    protected void InitObserve() {
        view_model.vehicle.observe(this, vehicle -> {
            switch (vehicle.status){
                case VehicleStatus.ACCEPTED:
                    binding.imgVehicleStatus.setImageResource(R.drawable.ic_success);
                    binding.btnOpenReasonRefuseModal.setVisibility(View.GONE);
                    break;
                case VehicleStatus.REFUSED:
                    binding.imgVehicleStatus.setImageResource(R.drawable.ic_refuse);
                    break;
                default:
                    binding.imgVehicleStatus.setImageResource(R.drawable.ic_queue);
                    binding.btnOpenReasonRefuseModal.setVisibility(View.GONE);
                    break;
            }
        });
    }

    @Override
    protected void InitCommon() {
        VehicleDetailAdapter vehicle_detail_adapter = new VehicleDetailAdapter(getSupportFragmentManager(), this.getLifecycle());
        binding.paperVehicleDetail.setAdapter(vehicle_detail_adapter);
        String[] tab_name = new String[]{
                getString(R.string.vehicle_registration_certificate),
                getString(R.string.driving_license),
                getString(R.string.my_vehicle)};
        new TabLayoutMediator(binding.tabVehicleDetail, binding.paperVehicleDetail, (tab, position) -> {
            tab.setText(tab_name[position]);
        }).attach();

        Vehicle vehicle = (Vehicle) getIntent().getSerializableExtra(FieldName.VEHICLE);
        view_model.vehicle.setValue(vehicle);
    }

    @Override
    protected void OnBackPress() {
        finish();
    }
}