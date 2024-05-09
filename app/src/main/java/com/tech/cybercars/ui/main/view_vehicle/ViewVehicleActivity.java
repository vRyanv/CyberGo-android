package com.tech.cybercars.ui.main.view_vehicle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tech.cybercars.R;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.data.models.TripManagement;
import com.tech.cybercars.data.models.Vehicle;
import com.tech.cybercars.databinding.ActivityViewVehicleBinding;
import com.tech.cybercars.ui.base.BaseActivity;

public class ViewVehicleActivity extends AppCompatActivity {
    private ActivityViewVehicleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_vehicle);
        InitView();
    }

    private void InitView() {
        String full_name = getIntent().getStringExtra(FieldName.FULL_NAME);
        String title = getString(R.string.vehicle_of) + " " + full_name;
        binding.setTitle(title);

        Vehicle vehicle = (Vehicle) getIntent().getSerializableExtra(FieldName.VEHICLE);
        assert vehicle != null;

        String license_plates = getString(R.string.license_plates) + ": " + vehicle.license_plates;
        binding.txtLicencePlate.setText(license_plates);

        final String base_url = URL.BASE_URL + URL.DRIVER_REGISTRATION_RES_PATH;
        String vehicle_img_full_path = base_url + vehicle.front_vehicle;
        Glide.with(this).load(vehicle_img_full_path).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                binding.imgFrontTransport.setScaleType(ImageView.ScaleType.FIT_XY);
                return false;
            }
        }).placeholder(R.drawable.loading_placeholder).into(binding.imgFrontTransport);


        vehicle_img_full_path = base_url + vehicle.back_vehicle;
        Glide.with(this).load(vehicle_img_full_path).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                binding.imgBackTransport.setScaleType(ImageView.ScaleType.FIT_XY);
                return false;
            }
        }).placeholder(R.drawable.loading_placeholder).into(binding.imgBackTransport);

        vehicle_img_full_path = base_url + vehicle.left_vehicle;
        Glide.with(this).load(vehicle_img_full_path).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                binding.imgLeftTransport.setScaleType(ImageView.ScaleType.FIT_XY);
                return false;
            }
        }).placeholder(R.drawable.loading_placeholder).into(binding.imgLeftTransport);

        vehicle_img_full_path = base_url + vehicle.right_vehicle;
        Glide.with(this).load(vehicle_img_full_path).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                binding.imgRightTransport.setScaleType(ImageView.ScaleType.FIT_XY);
                return false;
            }
        }).placeholder(R.drawable.loading_placeholder).into(binding.imgRightTransport);
    }

}