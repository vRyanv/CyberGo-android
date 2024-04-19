package com.tech.cybercars.ui.main.fragment.go.find_trip.trip_found_detail.fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.tech.cybercars.R;
import com.tech.cybercars.constant.DestinationType;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.constant.VehicleType;
import com.tech.cybercars.data.models.TripFound;
import com.tech.cybercars.data.models.Vehicle;
import com.tech.cybercars.databinding.FragmentInformationTripFoundDetailBinding;
import com.tech.cybercars.ui.base.BaseFragment;
import com.tech.cybercars.ui.main.fragment.go.find_trip.trip_found_detail.TripFoundDetailViewModel;

public class InformationTripFoundDetailFragment extends BaseFragment<FragmentInformationTripFoundDetailBinding, TripFoundDetailViewModel> {

    @NonNull
    @Override
    protected TripFoundDetailViewModel InitViewModel() {
        return new ViewModelProvider(requireActivity()).get(TripFoundDetailViewModel.class);
    }

    @Override
    protected FragmentInformationTripFoundDetailBinding InitBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_information_trip_found_detail, container, false);
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
        view_model.trip_found.observe(this, this::BindDataToUI);
    }

    @Override
    protected void InitCommon() {

    }

    private void BindDataToUI(TripFound trip_found){
        String avatar_full_path = URL.BASE_URL + URL.AVATAR_RES_PATH + trip_found.owner.avatar;
        Glide.with(requireContext())
                .load(avatar_full_path)
                .placeholder(R.drawable.loading_placeholder)
                .into(binding.imgAvatar);

        binding.txtFullName.setText(trip_found.owner.full_name);
        binding.ratingBar.setRating(trip_found.rating);
        binding.txtTripName.setText(trip_found.trip_name);

        switch (trip_found.vehicle_type){
            case VehicleType.CAR:
                binding.txtVehicleType.setText(getString(R.string.car));
                binding.imgVehicleType.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_car));
                break;
            case VehicleType.MOTO:
                binding.txtVehicleType.setText(getString(R.string.motorbike));
                binding.imgVehicleType.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_motorcycle));
                break;
            default:
                binding.txtVehicleType.setText(getString(R.string.truck));
                binding.imgVehicleType.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_truck));
                break;
        }

        binding.txtStartDate.setText(trip_found.start_date);
        binding.txtStartTime.setText(trip_found.start_time);

        String destination_type = "";
        if(trip_found.destination_type.equals(DestinationType.SINGLE)){
            destination_type = getString(R.string.single_destination);
        } else {
            destination_type =  getString(R.string.multiple_destination);
        }
        binding.txtDestinationType.setText(destination_type);

        if(trip_found.price > 0){
            binding.txtPrice.setText(String.valueOf(trip_found.price));
        } else {
            binding.txtPrice.setText(getString(R.string.free));
            binding.txtPrice.setTextColor(requireContext().getColor(R.color.green));
        }

        String description = trip_found.description == null ? getString(R.string.no_description_available) : trip_found.description;
        binding.txtDescription.setText(description);

    }
}