package com.tech.cybercars.ui.main.fragment.trip.trip_detail.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.tech.cybercars.R;
import com.tech.cybercars.constant.ActivityResult;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.TripStatus;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.constant.VehicleType;
import com.tech.cybercars.data.models.TripManagement;
import com.tech.cybercars.data.models.Vehicle;
import com.tech.cybercars.databinding.FragmentInformationTripDetailBinding;
import com.tech.cybercars.services.eventbus.UpdateTripInformationEvent;
import com.tech.cybercars.ui.base.BaseFragment;
import com.tech.cybercars.ui.main.rating.RatingActivity;
import com.tech.cybercars.ui.main.fragment.account.profile.ProfileActivity;
import com.tech.cybercars.ui.main.fragment.trip.edit_trip.EditTripInformationActivity;
import com.tech.cybercars.ui.main.fragment.trip.trip_detail.TripDetailViewModel;
import com.tech.cybercars.ui.main.user_profile.UserProfileActivity;
import com.tech.cybercars.ui.main.view_vehicle.ViewVehicleActivity;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
        EventBus.getDefault().register(this);
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

        String current_user_id = SharedPreferencesUtil.GetString(requireContext(), FieldName.USER_ID);
        if(trip_management.trip_status.equals(TripStatus.FINISH) && !trip_management.trip_owner.user_id.equals(current_user_id)){
            binding.btnMakeRatingOwner.setVisibility(View.VISIBLE);
            binding.btnMakeRatingOwner.setOnClickListener(view -> {
                Intent rating_intent = new Intent(requireContext(), RatingActivity.class);
                rating_intent.putExtra(FieldName.USER_ID, trip_management.trip_owner.user_id);
                rating_intent.putExtra(FieldName.AVATAR, trip_management.trip_owner.avatar);
                rating_intent.putExtra(FieldName.FULL_NAME, trip_management.trip_owner.full_name);
                startActivity(rating_intent);
            });
        }

        binding.btnOpenViewVehicle.setOnClickListener(view -> {
            Intent view_vehicle_intent = new Intent(requireContext(), ViewVehicleActivity.class);
            Vehicle vehicle = new Vehicle();
            vehicle.license_plates = trip_management.vehicle.license_plates;
            vehicle.front_vehicle = trip_management.vehicle.front_vehicle;
            vehicle.back_vehicle = trip_management.vehicle.back_vehicle;
            vehicle.left_vehicle = trip_management.vehicle.left_vehicle;
            vehicle.right_vehicle = trip_management.vehicle.right_vehicle;

            view_vehicle_intent.putExtra(FieldName.VEHICLE, vehicle);
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
                if(result.getResultCode() == ActivityResult.UPDATED){
                    assert result.getData() != null;
                    TripManagement trip_management = (TripManagement) result.getData().getSerializableExtra(FieldName.TRIP);
                    view_model.trip_management.setValue(trip_management);

                }
            }
    );

    @Subscribe
    public void InformationUpdated(UpdateTripInformationEvent update_trip_info_event){
        BindDataToUI(update_trip_info_event.trip_management);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}