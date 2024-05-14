package com.tech.cybercars.ui.main.fragment.account.my_vehicle.vehicle_detail;

import android.app.Dialog;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayoutMediator;
import com.tech.cybercars.R;
import com.tech.cybercars.adapter.paper.VehicleDetailPageAdapter;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.VehicleStatus;
import com.tech.cybercars.data.models.Vehicle;
import com.tech.cybercars.databinding.ActivityVehicleDetailBinding;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.component.dialog.DeleteDialog;

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

        binding.btnDeleteVehicle.setOnClickListener(view -> {
            String title_dialog = getString(R.string.delete_this_vehicle);
            new DeleteDialog(this, title_dialog)
                    .SetButtonSelectedCallback(new DeleteDialog.SelectButtonCallback() {
                        @Override
                        public void OnDelete(Dialog dialog) {
                            dialog.dismiss();
                        }

                        @Override
                        public void OnCancel(Dialog dialog) {
                            dialog.dismiss();
                        }
                    }).show();
        });

        binding.btnOpenReasonRefuseModal.setOnClickListener(v -> {
            String title_dialog = getString(R.string.delete_this_vehicle);
            new DeleteDialog(this, title_dialog)
                    .SetButtonSelectedCallback(new DeleteDialog.SelectButtonCallback() {
                        @Override
                        public void OnDelete(Dialog dialog) {
                            dialog.dismiss();
                        }

                        @Override
                        public void OnCancel(Dialog dialog) {
                            dialog.dismiss();
                        }
                    }).show();
        });
    }

    @Override
    protected void InitObserve() {
        view_model.vehicle.observe(this, vehicle -> {
            switch (vehicle.status){
                case VehicleStatus.ACCEPTED:
                    binding.imgVehicleStatus.setImageResource(R.drawable.ic_success);
                    binding.btnOpenReasonRefuseModal.setVisibility(View.GONE);
                    binding.btnDeleteVehicle.setVisibility(View.VISIBLE);
                    break;
                case VehicleStatus.REFUSED:
                    binding.imgVehicleStatus.setImageResource(R.drawable.ic_refuse);
                    binding.btnOpenReasonRefuseModal.setVisibility(View.VISIBLE);
                    break;
                default:
                    binding.imgVehicleStatus.setImageResource(R.drawable.ic_queue);
                    break;
            }
        });
    }

    @Override
    protected void InitCommon() {
        VehicleDetailPageAdapter vehicle_detail_adapter = new VehicleDetailPageAdapter(getSupportFragmentManager(), this.getLifecycle());
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