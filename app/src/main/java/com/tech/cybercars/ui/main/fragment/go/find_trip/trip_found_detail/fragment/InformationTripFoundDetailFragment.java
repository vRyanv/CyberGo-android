package com.tech.cybercars.ui.main.fragment.go.find_trip.trip_found_detail.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.tech.cybercars.R;
import com.tech.cybercars.constant.DestinationType;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.constant.VehicleType;
import com.tech.cybercars.data.models.TripFound;
import com.tech.cybercars.data.models.Vehicle;
import com.tech.cybercars.databinding.FragmentInformationTripFoundDetailBinding;
import com.tech.cybercars.ui.base.BaseFragment;
import com.tech.cybercars.ui.main.fragment.account.profile.ProfileActivity;
import com.tech.cybercars.ui.main.fragment.go.find_trip.trip_found_detail.TripFoundDetailViewModel;
import com.tech.cybercars.ui.main.user_profile.UserProfileActivity;
import com.tech.cybercars.ui.main.view_vehicle.ViewVehicleActivity;
import com.tech.cybercars.utils.SharedPreferencesUtil;

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
        binding.btnOpenViewVehicle.setOnClickListener(view -> {
            Intent view_vehicle_intent = new Intent(requireContext(), ViewVehicleActivity.class);
            Vehicle vehicle = new Vehicle();
            vehicle.license_plates = view_model.trip_found.getValue().vehicle.license_plates;
            vehicle.front_vehicle = view_model.trip_found.getValue().vehicle.front_vehicle;
            vehicle.back_vehicle = view_model.trip_found.getValue().vehicle.back_vehicle;
            vehicle.left_vehicle = view_model.trip_found.getValue().vehicle.left_vehicle;
            vehicle.right_vehicle = view_model.trip_found.getValue().vehicle.right_vehicle;

            view_vehicle_intent.putExtra(FieldName.VEHICLE, vehicle);
            view_vehicle_intent.putExtra(FieldName.FULL_NAME, view_model.trip_found.getValue().owner.full_name);
            startActivity(view_vehicle_intent);
        });

        binding.btnOpenOwnerProfile.setOnClickListener(view -> {
            OpenUserProfile();
        });
    }

    private void OpenUserProfile() {
        String user_id = view_model.trip_found.getValue().owner.user_id;
        String current_user_id = SharedPreferencesUtil.GetString(requireContext(), FieldName.USER_ID);
        if(current_user_id.equals(user_id)){
            startActivity(new Intent(requireContext(), ProfileActivity.class));
        } else {
            Intent user_profile_intent = new Intent(requireContext(), UserProfileActivity.class);
            user_profile_intent.putExtra(FieldName.USER_ID, user_id);
            startActivity(user_profile_intent);
        }
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
        binding.ratingBar.setRating(trip_found.owner.rating);
        binding.txtTripName.setText(trip_found.trip_name);

        switch (trip_found.vehicle.type){
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