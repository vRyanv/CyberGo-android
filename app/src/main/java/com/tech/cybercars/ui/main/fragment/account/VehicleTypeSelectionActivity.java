package com.tech.cybercars.ui.main.fragment.account;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.VehicleType;
import com.tech.cybercars.databinding.ActivitySelectVehicleTypeBinding;
import com.tech.cybercars.ui.main.fragment.account.driver_register.DriverRegistrationActivity;

public class VehicleTypeSelectionActivity extends AppCompatActivity {
    ActivitySelectVehicleTypeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectVehicleTypeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        InitView();
    }

    private void InitView() {
        binding.typeCarSelect.setOnClickListener(view -> ActiveSelection(binding.typeCarSelect, VehicleType.CAR));
        binding.typeMotoSelect.setOnClickListener(view ->  ActiveSelection(binding.typeMotoSelect, VehicleType.MOTO));
        binding.typeTruckSelect.setOnClickListener(view -> ActiveSelection(binding.typeTruckSelect, VehicleType.TRUCK));
    }

    private void ActiveSelection(ConstraintLayout selection, String transport_type) {
        binding.typeCarSelect.setBackgroundResource(R.drawable.shape_transport_type);
        binding.typeTruckSelect.setBackgroundResource(R.drawable.shape_transport_type);
        binding.typeMotoSelect.setBackgroundResource(R.drawable.shape_transport_type);
        selection.setBackgroundResource(R.drawable.shape_transport_type_selected);

        startActivity(new Intent(this, DriverRegistrationActivity.class).putExtra(FieldName.VEHICLE_TYPE, transport_type));
        finish();
    }
}