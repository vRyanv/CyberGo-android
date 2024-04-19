package com.tech.cybercars.ui.main.fragment.go;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.tech.cybercars.R;
import com.tech.cybercars.databinding.FragmentGoBinding;
import com.tech.cybercars.ui.main.fragment.go.find_trip.FindTripActivity;
import com.tech.cybercars.ui.main.fragment.go.vehicle_selection.VehicleSelectionActivity;

public class GoFragment extends Fragment {
    private FragmentGoBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_go, container, false);


        InitView();

        return binding.getRoot();
    }

    private void InitView(){
        binding.cardFindTrip.setOnClickListener(view->{
            StartFindTripActivity();
        });
        binding.btnFindTrip.setOnClickListener(view->{
            StartFindTripActivity();
        });

        binding.cardShareTrip.setOnClickListener(view->{
            StartShareTripActivity();
        });
        binding.btnShareTrip.setOnClickListener(view->{
            StartShareTripActivity();
        });


    }

    private void StartFindTripActivity(){
        startActivity(new Intent(requireContext(), FindTripActivity.class));
    }

    private void StartShareTripActivity(){
        startActivity(new Intent(requireContext(), VehicleSelectionActivity.class));
    }
}