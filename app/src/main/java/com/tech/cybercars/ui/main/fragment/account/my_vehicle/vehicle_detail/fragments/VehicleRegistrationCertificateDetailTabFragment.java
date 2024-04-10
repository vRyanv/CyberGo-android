package com.tech.cybercars.ui.main.fragment.account.my_vehicle.vehicle_detail.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tech.cybercars.R;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.constant.VehicleType;
import com.tech.cybercars.data.models.Vehicle;
import com.tech.cybercars.databinding.FragmentVehicleRegistrationCertificateDetailTabBinding;
import com.tech.cybercars.ui.main.fragment.account.my_vehicle.vehicle_detail.VehicleDetailViewModel;

public class VehicleRegistrationCertificateDetailTabFragment extends Fragment {

    private FragmentVehicleRegistrationCertificateDetailTabBinding binding;
    private VehicleDetailViewModel view_model;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_vehicle_registration_certificate_detail_tab, container, false);
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
        switch (vehicle.vehicle_type){
            case VehicleType.CAR:
                binding.imgVehicleType.setImageResource(R.drawable.ic_car);
                break;
            case VehicleType.MOTO:
                binding.imgVehicleType.setImageResource(R.drawable.ic_motorcycle);
                break;
            default:
                binding.imgVehicleType.setImageResource(R.drawable.ic_truck);
                break;
        }
        String vehicle_name = getString(R.string.vehicle_name) + ": " + vehicle.vehicle_name;
        binding.txtVehicleName.setText(vehicle_name);

        String front_certificate_full_path = URL.BASE_URL + URL.DRIVER_REGISTRATION_RES_PATH + vehicle.front_vehicle_registration_certificate;
        Glide.with(this)
                .load(front_certificate_full_path)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                        binding.imgFrontCertificate.setScaleType(ImageView.ScaleType.FIT_XY);
                        return false;
                    }
                })
                .into(binding.imgFrontCertificate);

        String back_certificate_full_path = URL.BASE_URL + URL.DRIVER_REGISTRATION_RES_PATH + vehicle.back_vehicle_registration_certificate;
        Glide.with(this)
                .load(back_certificate_full_path)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                        binding.imgBackCertificate.setScaleType(ImageView.ScaleType.FIT_XY);
                        return false;
                    }
                })
                .into(binding.imgBackCertificate);
    }
}