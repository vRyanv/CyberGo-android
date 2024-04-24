package com.tech.cybercars.ui.main.fragment.trip.trip_detail.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.tech.cybercars.R;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.TripStatus;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.constant.VehicleType;
import com.tech.cybercars.data.models.TripManagement;
import com.tech.cybercars.databinding.FragmentInformationTripDetailBinding;
import com.tech.cybercars.ui.base.BaseFragment;
import com.tech.cybercars.ui.main.fragment.go.share_trip.add_share_trip_information.AddShareTripInformationViewModel;
import com.tech.cybercars.ui.main.fragment.trip.trip_detail.TripDetailViewModel;
import com.tech.cybercars.ui.main.view_vehicle.ViewVehicleActivity;

public class InformationTripDetailFragment extends BaseFragment<FragmentInformationTripDetailBinding, TripDetailViewModel> {
    @NonNull
    @Override
    protected TripDetailViewModel InitViewModel() {
        return new ViewModelProvider(requireActivity()).get(TripDetailViewModel.class);
    }

    @Override
    protected FragmentInformationTripDetailBinding InitBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_information_trip_detail, container, false);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {

    }

    @Override
    protected void InitObserve() {
        view_model.trip_management.observe(requireActivity(), this::BindDataToUI);
    }

    @Override
    protected void InitCommon() {

    }

    private void BindDataToUI(TripManagement trip_management){
        String avatar_full_path = URL.BASE_URL + URL.AVATAR_RES_PATH + trip_management.trip_owner.avatar;
        Glide.with(requireContext()).load(avatar_full_path).placeholder(R.drawable.loading_placeholder).into(binding.imgAvatar);

        binding.txtFullName.setText(trip_management.trip_owner.full_name);
        binding.ratingBar.setRating(trip_management.trip_owner.rating);

        binding.btnOpenViewVehicle.setOnClickListener(view -> {
            Intent view_vehicle_intent = new Intent(requireContext(), ViewVehicleActivity.class);
            view_vehicle_intent.putExtra(FieldName.VEHICLE, trip_management.vehicle);
            view_vehicle_intent.putExtra(FieldName.FULL_NAME, trip_management.trip_owner.full_name);
            startActivity(view_vehicle_intent);
        });

        switch (trip_management.vehicle.type){
            case VehicleType.MOTO:
                binding.txtVehicleType.setText(getString(R.string.motorbike));
                binding.imgVehicleType.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_motorcycle));
                break;
            case VehicleType.CAR:
                binding.txtVehicleType.setText(getString(R.string.car));
                binding.imgVehicleType.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_car));
                break;
            default:
                binding.txtVehicleType.setText(getString(R.string.truck));
                binding.imgVehicleType.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_truck));
                break;
        }

        binding.txtTripName.setText(trip_management.trip_name);
        binding.txtStartDate.setText(trip_management.start_date);
        binding.txtStartTime.setText(trip_management.start_time);

        if(trip_management.price > 0){
            binding.txtPrice.setText(String.valueOf(trip_management.price));
        } else {
            binding.txtPrice.setText(requireContext().getString(R.string.free));
            binding.txtPrice.setTextColor(requireContext().getColor(R.color.green));
        }


        switch (trip_management.trip_status){
            case TripStatus.OPENING:
                binding.txtStatus.setText(getString(R.string.opening));
                break;
            case TripStatus.CLOSED:
                binding.txtStatus.setText(getString(R.string.closed));
                break;
            default:
                binding.txtStatus.setText(getString(R.string.finish));
                break;
        }


        if(trip_management.description == null || trip_management.description.equals("")){
            binding.txtDescription.setText(getString(R.string.no_description_available));
        } else {
            binding.txtDescription.setText(trip_management.description);
        }
    }

}