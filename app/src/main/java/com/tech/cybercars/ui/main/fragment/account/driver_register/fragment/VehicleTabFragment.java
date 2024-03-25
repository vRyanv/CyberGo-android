package com.tech.cybercars.ui.main.fragment.account.driver_register.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.tech.cybercars.R;
import com.tech.cybercars.databinding.FragmentVehicleTabBinding;
import com.tech.cybercars.ui.main.fragment.account.driver_register.DriverRegistrationViewModel;
import com.tech.cybercars.utils.FileUtil;

public class VehicleTabFragment extends Fragment {

    private FragmentVehicleTabBinding binding;
    private DriverRegistrationViewModel view_model;
    private final String PICK_FRONT_TRANSPORT = "PICK_FRONT_TRANSPORT";
    private final String PICK_BACK_TRANSPORT = "PICK_BACK_TRANSPORT";
    private final String PICK_RIGHT_TRANSPORT = "PICK_RIGHT_TRANSPORT";
    private final String PICK_LEFT_TRANSPORT = "PICK_LEFT_TRANSPORT";
    private String current_pick_img_action;

    private void InitView() {

        binding.btnUploadFrontVehicle.setOnClickListener(view -> {
            Intent take_photo = new Intent(Intent.ACTION_PICK);
            take_photo.setType("image/*");
            current_pick_img_action = PICK_FRONT_TRANSPORT;
            take_img_launcher.launch(take_photo);
        });

        binding.btnUploadBackVehicle.setOnClickListener(view -> {
            Intent take_photo = new Intent(Intent.ACTION_PICK);
            take_photo.setType("image/*");
            current_pick_img_action = PICK_BACK_TRANSPORT;
            take_img_launcher.launch(take_photo);
        });

        binding.imgRightTransport.setOnClickListener(view -> {
            Intent take_photo = new Intent(Intent.ACTION_PICK);
            take_photo.setType("image/*");
            current_pick_img_action = PICK_RIGHT_TRANSPORT;
            take_img_launcher.launch(take_photo);
        });

        binding.btnUploadRightVehicle.setOnClickListener(view -> {
            Intent take_photo = new Intent(Intent.ACTION_PICK);
            take_photo.setType("image/*");
            current_pick_img_action = PICK_RIGHT_TRANSPORT;
            take_img_launcher.launch(take_photo);
        });

        binding.btnUploadLeftVehicle.setOnClickListener(view -> {
            Intent take_photo = new Intent(Intent.ACTION_PICK);
            take_photo.setType("image/*");
            current_pick_img_action = PICK_LEFT_TRANSPORT;
            take_img_launcher.launch(take_photo);
        });

    }

    private final ActivityResultLauncher<Intent> take_img_launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (data != null) {
                    Uri img_uri = data.getData();
                    if (img_uri != null) {
                        try {
                            Bitmap img_bitmap = FileUtil.CreateBitMapFromUri(requireContext(), img_uri);
                            BindImgToUI(img_uri, img_bitmap);
                        } catch (Exception e) {
                            Toast.makeText(requireActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
    );

    private void BindImgToUI(Uri img_uri, Bitmap img_bitmap){
        switch (current_pick_img_action) {
            case PICK_FRONT_TRANSPORT:
                view_model.front_vehicle_uri = img_uri;
                binding.imgFrontTransport.setImageBitmap(img_bitmap);
                binding.imgFrontTransport.setScaleType(ImageView.ScaleType.FIT_XY);
                break;
            case PICK_BACK_TRANSPORT:
                view_model.back_vehicle_uri = img_uri;
                binding.imgBackTransport.setImageBitmap(img_bitmap);
                binding.imgBackTransport.setScaleType(ImageView.ScaleType.FIT_XY);
                break;
            case PICK_RIGHT_TRANSPORT:
                view_model.right_vehicle_uri = img_uri;
                binding.imgRightTransport.setImageBitmap(img_bitmap);
                binding.imgRightTransport.setScaleType(ImageView.ScaleType.FIT_XY);
                break;
            case PICK_LEFT_TRANSPORT:
                view_model.left_vehicle_uri = img_uri;
                binding.imgLeftTransport.setImageBitmap(img_bitmap);
                binding.imgLeftTransport.setScaleType(ImageView.ScaleType.FIT_XY);
                break;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_vehicle_tab, container, false);
        view_model = new ViewModelProvider(requireActivity()).get(DriverRegistrationViewModel.class);
        binding.setViewModel(view_model);
        InitView();

        return binding.getRoot();
    }
}