package com.tech.cybercars.ui.main.fragment.account.my_vehicle.vehicle_detail.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tech.cybercars.R;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.data.models.Vehicle;
import com.tech.cybercars.databinding.FragmentDrivingLicenseDetailTabBinding;
import com.tech.cybercars.databinding.FragmentDrivingLicenseTabBinding;
import com.tech.cybercars.ui.main.fragment.account.driver_register.DriverRegistrationViewModel;
import com.tech.cybercars.ui.main.fragment.account.my_vehicle.vehicle_detail.VehicleDetailViewModel;

public class DrivingLicenseDetaiTablFragment extends Fragment {
    private FragmentDrivingLicenseDetailTabBinding binding;
    private VehicleDetailViewModel view_model;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_driving_license_detail_tab, container, false);
        view_model = new ViewModelProvider(requireActivity()).get(VehicleDetailViewModel.class);
        InitView();
        InitObserver();
        return binding.getRoot();
    }

    private void InitView(){

    }

    private void InitObserver(){
        view_model.vehicle.observe(requireActivity(), this::BindDataToUI);
    }

    private void BindDataToUI(Vehicle vehicle){
        String img_driving_license_full_path  = URL.BASE_URL + URL.DRIVER_REGISTRATION_RES_PATH + vehicle.front_driving_license;
        Glide.with(this)
                .load(img_driving_license_full_path)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                        binding.imgFrontDrivingLicenseCard.setScaleType(ImageView.ScaleType.FIT_XY);
                        return false;
                    }
                })
                .into(binding.imgFrontDrivingLicenseCard);

        img_driving_license_full_path  = URL.BASE_URL + URL.DRIVER_REGISTRATION_RES_PATH + vehicle.back_driving_license;
        Glide.with(this)
                .load(img_driving_license_full_path)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                        binding.imgBackDrivingLicenseCard.setScaleType(ImageView.ScaleType.FIT_XY);
                        return false;
                    }
                })
                .into(binding.imgBackDrivingLicenseCard);
    }
}