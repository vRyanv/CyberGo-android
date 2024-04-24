package com.tech.cybercars.ui.main.fragment.trip.trip_detail.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.tech.cybercars.R;
import com.tech.cybercars.adapter.destination.DestinationAdapter;
import com.tech.cybercars.constant.DestinationType;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.TripStatus;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.constant.VehicleType;
import com.tech.cybercars.data.models.TripManagement;
import com.tech.cybercars.data.models.trip.Destination;
import com.tech.cybercars.databinding.FragmentLocationTripDetailBinding;
import com.tech.cybercars.ui.base.BaseFragment;
import com.tech.cybercars.ui.main.fragment.trip.trip_detail.TripDetailViewModel;
import com.tech.cybercars.ui.main.view_vehicle.ViewVehicleActivity;
import com.tech.cybercars.utils.DateUtil;
import com.tech.cybercars.utils.Helper;

import java.util.ArrayList;

public class LocationTripDetailFragment extends BaseFragment<FragmentLocationTripDetailBinding, TripDetailViewModel> {
    private DestinationAdapter destination_adapter;
    @NonNull
    @Override
    protected TripDetailViewModel InitViewModel() {
        return new ViewModelProvider(requireActivity()).get(TripDetailViewModel.class);
    }

    @Override
    protected FragmentLocationTripDetailBinding InitBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_location_trip_detail, container, false);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {
        destination_adapter = new DestinationAdapter(new ArrayList<>());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.rcvDestinations.setLayoutManager(layoutManager);
        binding.rcvDestinations.setAdapter(destination_adapter);
    }

    @Override
    protected void InitObserve() {
        view_model.trip_management.observe(requireActivity(), this::BindDataToUI);
    }

    @Override
    protected void InitCommon() {

    }

    private void BindDataToUI(TripManagement trip_management) {
        binding.txtOriginAddress.setText(trip_management.origin_address);
        if(trip_management.destination_type.equals(DestinationType.SINGLE)){
            binding.wrapperTime.setVisibility(View.GONE);
            binding.wrapperDistance.setVisibility(View.GONE);
        }

        destination_adapter.UpdateAdapter(trip_management.destinations);
        double distance = 0;
        double time = 0;
        for (Destination destination : trip_management.destinations) {
            distance += destination.distance;
            time += destination.time;
        }
        String total_distance = getString(R.string.total_distance) + ": " + Helper.ConvertMeterToKiloMeterString(distance) + " Km";
        binding.txtTotalDistance.setText(total_distance);

        String total_time = getString(R.string.total_time) + ": " + DateUtil.ConvertSecondToHour(time);
        binding.txtTotalTime.setText(total_time);
    }
}