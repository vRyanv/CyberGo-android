package com.tech.cybercars.ui.main.fragment.go;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.TransportType;
import com.tech.cybercars.databinding.ActivitySelectTransportBinding;
import com.tech.cybercars.ui.main.fragment.go.share_transport.ShareTransportActivity;

public class SelectTransportActivity extends AppCompatActivity {
    ActivitySelectTransportBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectTransportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        InitView();
    }

    private void InitView() {
        binding.typeCarSelect.setOnClickListener(view -> ActiveSelection(binding.typeCarSelect, TransportType.CAR));
        binding.typeMotoSelect.setOnClickListener(view ->  ActiveSelection(binding.typeMotoSelect, TransportType.MOTO));
        binding.typeTruckSelect.setOnClickListener(view -> ActiveSelection(binding.typeTruckSelect, TransportType.TRUCK));
    }

    private void ActiveSelection(ConstraintLayout selection, String transport_type) {
        binding.typeCarSelect.setBackgroundResource(R.drawable.shape_transport_type);
        binding.typeTruckSelect.setBackgroundResource(R.drawable.shape_transport_type);
        binding.typeMotoSelect.setBackgroundResource(R.drawable.shape_transport_type);
        selection.setBackgroundResource(R.drawable.shape_transport_type_selected);

        startActivity(new Intent(this, ShareTransportActivity.class).putExtra(TransportType.class.getSimpleName(), transport_type));
    }
}