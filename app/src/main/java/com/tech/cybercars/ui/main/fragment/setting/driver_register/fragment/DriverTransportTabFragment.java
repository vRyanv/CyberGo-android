package com.tech.cybercars.ui.main.fragment.setting.driver_register.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.tech.cybercars.R;
import com.tech.cybercars.databinding.FragmentDriverInfomationTabBinding;
import com.tech.cybercars.databinding.FragmentDriverTransportTabBinding;
import com.tech.cybercars.ui.main.fragment.setting.driver_register.DriverRegistrationViewModel;
import com.tech.cybercars.utils.FileUtil;

public class DriverTransportTabFragment extends Fragment {

    private FragmentDriverTransportTabBinding binding;
    private DriverRegistrationViewModel view_model;
    private final String PICK_FRONT_TRANSPORT = "PICK_FRONT_TRANSPORT";
    private final String PICK_BACK_TRANSPORT = "PICK_BACK_TRANSPORT";
    private final String PICK_RIGHT_TRANSPORT = "PICK_RIGHT_TRANSPORT";
    private final String PICK_LEFT_TRANSPORT = "PICK_LEFT_TRANSPORT";
    private String current_pick_img_action;

    private void InitView() {

        binding.imgFrontTransport.setOnClickListener(view -> {
            Intent take_photo = new Intent(Intent.ACTION_PICK);
            take_photo.setType("image/*");
            current_pick_img_action = PICK_FRONT_TRANSPORT;
            if (take_photo.resolveActivity(requireActivity().getPackageManager()) != null) {
                take_img_launcher.launch(take_photo);
            }
        });

        binding.imgBackTransport.setOnClickListener(view -> {
            Intent take_photo = new Intent(Intent.ACTION_PICK);
            take_photo.setType("image/*");
            current_pick_img_action = PICK_BACK_TRANSPORT;
            if (take_photo.resolveActivity(requireActivity().getPackageManager()) != null) {
                take_img_launcher.launch(take_photo);
            }
        });

        binding.imgRightTransport.setOnClickListener(view -> {
            Intent take_photo = new Intent(Intent.ACTION_PICK);
            take_photo.setType("image/*");
            current_pick_img_action = PICK_RIGHT_TRANSPORT;
            if (take_photo.resolveActivity(requireActivity().getPackageManager()) != null) {
                take_img_launcher.launch(take_photo);
            }
        });

        binding.imgRightTransport.setOnClickListener(view -> {
            Intent take_photo = new Intent(Intent.ACTION_PICK);
            take_photo.setType("image/*");
            current_pick_img_action = PICK_RIGHT_TRANSPORT;
            if (take_photo.resolveActivity(requireActivity().getPackageManager()) != null) {
                take_img_launcher.launch(take_photo);
            }
        });

        binding.imgLeftTransport.setOnClickListener(view -> {
            Intent take_photo = new Intent(Intent.ACTION_PICK);
            take_photo.setType("image/*");
            current_pick_img_action = PICK_LEFT_TRANSPORT;
            if (take_photo.resolveActivity(requireActivity().getPackageManager()) != null) {
                take_img_launcher.launch(take_photo);
            }
        });

    }

    private ActivityResultLauncher take_img_launcher;

    private void RegisterActivityResult() {
        take_img_launcher = registerForActivityResult(
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
    }

    private void BindImgToUI(Uri img_uri, Bitmap img_bitmap){
        switch (current_pick_img_action) {
            case PICK_FRONT_TRANSPORT:
                view_model.front_transport_uri = img_uri;
                binding.imgFrontTransport.setImageBitmap(img_bitmap);
                binding.imgFrontTransport.setScaleType(ImageView.ScaleType.FIT_XY);
                break;
            case PICK_BACK_TRANSPORT:
                view_model.back_transport_uri = img_uri;
                binding.imgBackTransport.setImageBitmap(img_bitmap);
                binding.imgBackTransport.setScaleType(ImageView.ScaleType.FIT_XY);
                break;
            case PICK_RIGHT_TRANSPORT:
                view_model.right_transport_uri = img_uri;
                binding.imgRightTransport.setImageBitmap(img_bitmap);
                binding.imgRightTransport.setScaleType(ImageView.ScaleType.FIT_XY);
                break;
            case PICK_LEFT_TRANSPORT:
                view_model.left_transport_uri = img_uri;
                binding.imgLeftTransport.setImageBitmap(img_bitmap);
                binding.imgLeftTransport.setScaleType(ImageView.ScaleType.FIT_XY);
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_driver_transport_tab, container, false);
        view_model = new ViewModelProvider(requireActivity()).get(DriverRegistrationViewModel.class);
        binding.setViewModel(view_model);
        InitView();
        RegisterActivityResult();
        return binding.getRoot();
    }
}