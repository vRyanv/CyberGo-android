package com.tech.cybercars.ui.main.fragment.trip.trip_detail.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tech.cybercars.R;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.TripStatus;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.constant.VehicleType;
import com.tech.cybercars.data.models.TripManagement;
import com.tech.cybercars.databinding.FragmentInformationTripDetailBinding;
import com.tech.cybercars.ui.base.BaseFragment;
import com.tech.cybercars.ui.main.feedback.FeedbackActivity;
import com.tech.cybercars.ui.main.fragment.account.profile.ProfileActivity;
import com.tech.cybercars.ui.main.fragment.go.share_trip.add_share_trip_information.AddShareTripInformationViewModel;
import com.tech.cybercars.ui.main.fragment.trip.edit_trip.EditTripInformationActivity;
import com.tech.cybercars.ui.main.fragment.trip.trip_detail.TripDetailViewModel;
import com.tech.cybercars.ui.main.user_profile.UserProfileActivity;
import com.tech.cybercars.ui.main.view_vehicle.ViewVehicleActivity;
import com.tech.cybercars.utils.SharedPreferencesUtil;

public class InformationTripDetailFragment extends BaseFragment<FragmentInformationTripDetailBinding, TripDetailViewModel> {
    @NonNull
    @Override
    protected TripDetailViewModel InitViewModel() {
        return new ViewModelProvider(requireActivity()).get(TripDetailViewModel.class);
    }

    @Override
    protected FragmentInformationTripDetailBinding InitBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_information_trip_detail, container, false);
        binding.setViewModel(view_model);
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

        view_model.can_perform_trip.observe(this, can_perform_trip-> {
            if(can_perform_trip){
                binding.btnUpdateTripInformation.setVisibility(View.VISIBLE);
            } else {
                binding.btnUpdateTripInformation.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void InitCommon() {

    }

    private void BindDataToUI(TripManagement trip_management){
        if(trip_management == null){
            return;
        }

        String avatar_full_path = URL.BASE_URL + URL.AVATAR_RES_PATH + trip_management.trip_owner.avatar;
        Glide.with(requireContext()).load(avatar_full_path).placeholder(R.drawable.loading_placeholder).into(binding.imgAvatar);

        binding.btnOpenOwnerProfile.setOnClickListener(view -> {
            OpenUserProfile(trip_management.trip_owner.user_id);
        });

        binding.txtFullName.setText(trip_management.trip_owner.full_name);
        binding.ratingBar.setRating(trip_management.trip_owner.rating);

        if(!trip_management.trip_status.equals(TripStatus.FINISH)){
            binding.btnUpdateTripInformation.setOnClickListener(view -> {
                Intent edit_trip_intent = new Intent(requireContext(), EditTripInformationActivity.class);
                edit_trip_intent.putExtra(FieldName.TRIP, trip_management);
                edit_trip_launcher.launch(edit_trip_intent);
            });
        }

        if(trip_management.trip_status.equals(TripStatus.FINISH)){
            binding.btnMakeRatingOwner.setVisibility(View.VISIBLE);
            binding.btnMakeRatingOwner.setOnClickListener(view -> {
                Intent feedback_intent = new Intent(requireContext(), FeedbackActivity.class);
                feedback_intent.putExtra(FieldName.AVATAR, trip_management.trip_owner.avatar);
                feedback_intent.putExtra(FieldName.FULL_NAME, trip_management.trip_owner.full_name);
                startActivity(feedback_intent);
            });
        }

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
                binding.txtStatus.setBackgroundColor(requireContext().getColor(R.color.opening_color));
                binding.txtStatus.setText(getString(R.string.opening));
                break;
            case TripStatus.CLOSED:
                binding.txtStatus.setBackgroundColor(requireContext().getColor(R.color.closed_color));
                binding.txtStatus.setText(getString(R.string.closed));
                break;
            default:
                binding.txtStatus.setBackgroundColor(requireContext().getColor(R.color.finish_color));
                binding.txtStatus.setText(getString(R.string.finish));
                break;
        }


        if(trip_management.description == null || trip_management.description.equals("")){
            binding.txtDescription.setText(getString(R.string.no_description_available));
        } else {
            binding.txtDescription.setText(trip_management.description);
        }
    }

    private void OpenUserProfile(String user_id) {
        String current_user_id = SharedPreferencesUtil.GetString(requireContext(), FieldName.USER_ID);
        if(current_user_id.equals(user_id)){
            startActivity(new Intent(requireContext(), ProfileActivity.class));
        } else {
            Intent user_profile_intent = new Intent(requireContext(), UserProfileActivity.class);
            user_profile_intent.putExtra(FieldName.USER_ID, user_id);
            startActivity(user_profile_intent);
        }
    }

    private final ActivityResultLauncher<Intent> edit_trip_launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

            }
    );

}