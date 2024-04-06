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
import com.tech.cybercars.databinding.FragmentDrivingLicenseTabBinding;
import com.tech.cybercars.databinding.FragmentVehicleDetailTabBinding;
import com.tech.cybercars.ui.main.fragment.account.my_vehicle.vehicle_detail.VehicleDetailViewModel;


public class VehicleDetailTabFragment extends Fragment {
    private FragmentVehicleDetailTabBinding binding;
    private VehicleDetailViewModel view_model;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_vehicle_detail_tab, container, false);
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
        String license_plates = getString(R.string.license_plates) + ": " + vehicle.license_plates;
        binding.txtLicensePlate.setText(license_plates);

        String img_vehicle_full_path  = URL.BASE_URL + URL.DRIVER_REGISTRATION_RES_PATH + vehicle.front_vehicle;
        Glide.with(this)
                .load(img_vehicle_full_path)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                        binding.imgFrontTransport.setScaleType(ImageView.ScaleType.FIT_XY);
                        return false;
                    }
                })
                .into(binding.imgFrontTransport);

        img_vehicle_full_path  = URL.BASE_URL + URL.DRIVER_REGISTRATION_RES_PATH + vehicle.back_vehicle;
        Glide.with(this)
                .load(img_vehicle_full_path)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                        binding.imgBackTransport.setScaleType(ImageView.ScaleType.FIT_XY);
                        return false;
                    }
                })
                .into(binding.imgBackTransport);

        img_vehicle_full_path  = URL.BASE_URL + URL.DRIVER_REGISTRATION_RES_PATH + vehicle.right_vehicle;
        Glide.with(this)
                .load(img_vehicle_full_path)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                        binding.imgRightTransport.setScaleType(ImageView.ScaleType.FIT_XY);
                        return false;
                    }
                })
                .into(binding.imgRightTransport);

        img_vehicle_full_path  = URL.BASE_URL + URL.DRIVER_REGISTRATION_RES_PATH + vehicle.left_vehicle;
        Glide.with(this)
                .load(img_vehicle_full_path)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                        binding.imgLeftTransport.setScaleType(ImageView.ScaleType.FIT_XY);
                        return false;
                    }
                })
                .into(binding.imgLeftTransport);
    }
}