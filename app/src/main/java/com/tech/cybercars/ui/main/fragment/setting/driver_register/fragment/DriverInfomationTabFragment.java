package com.tech.cybercars.ui.main.fragment.setting.driver_register.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.databinding.FragmentDriverInfomationTabBinding;
import com.tech.cybercars.databinding.FragmentGoBinding;
import com.tech.cybercars.ui.main.fragment.go.SelectTransportActivity;
import com.tech.cybercars.ui.main.fragment.go.find_trip.FindTransportActivity;
import com.tech.cybercars.ui.main.fragment.setting.driver_register.DriverRegistrationViewModel;
import com.tech.cybercars.utils.FileUtil;
import com.tech.cybercars.utils.RealPathUtil;

import java.io.File;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class DriverInfomationTabFragment extends Fragment {

    private FragmentDriverInfomationTabBinding binding;
    private DriverRegistrationViewModel view_model;
    private final String PICK_BACK_ID_CARD = "PICK_BACK_ID_CARD";
    private final String PICK_FRONT_ID_CARD = "PICK_FRONT_ID_CARD";
    private final String PICK_DRIVER_AVATAR = "PICK_DRIVER_AVATAR";
    private final String PICK_CURRICULUM_VITAE = "PICK_CURRICULUM_VITAE";
    private String current_pick_img_action;

    private void InitView() {

        binding.imgFrontIdCard.setOnClickListener(view -> {
            Intent take_photo = new Intent(Intent.ACTION_PICK);
            take_photo.setType("image/*");
            current_pick_img_action = PICK_FRONT_ID_CARD;
            if (take_photo.resolveActivity(requireActivity().getPackageManager()) != null) {
                take_img_launcher.launch(take_photo);
            }
        });

        binding.imgBackIdCard.setOnClickListener(view -> {
            Intent take_photo = new Intent(Intent.ACTION_PICK);
            take_photo.setType("image/*");
            current_pick_img_action = PICK_BACK_ID_CARD;
            if (take_photo.resolveActivity(requireActivity().getPackageManager()) != null) {
                take_img_launcher.launch(take_photo);
            }
        });

        binding.imgDriverAvatar.setOnClickListener(view -> {
            Intent take_photo = new Intent(Intent.ACTION_PICK);
            take_photo.setType("image/*");
            current_pick_img_action = PICK_DRIVER_AVATAR;
            if (take_photo.resolveActivity(requireActivity().getPackageManager()) != null) {
                take_img_launcher.launch(take_photo);
            }
        });

        binding.imgCurriculumVitae.setOnClickListener(view -> {
            Intent take_photo = new Intent(Intent.ACTION_PICK);
            take_photo.setType("image/*");
            current_pick_img_action = PICK_CURRICULUM_VITAE;
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
            case PICK_FRONT_ID_CARD:
                view_model.front_id_card_uri = img_uri;
                binding.imgFrontIdCard.setImageBitmap(img_bitmap);
                binding.imgFrontIdCard.setScaleType(ImageView.ScaleType.FIT_XY);
                break;
            case PICK_BACK_ID_CARD:
                view_model.back_id_card_uri = img_uri;
                binding.imgBackIdCard.setImageBitmap(img_bitmap);
                binding.imgBackIdCard.setScaleType(ImageView.ScaleType.FIT_XY);
                break;
            case PICK_DRIVER_AVATAR:
                view_model.driver_avatar_uri = img_uri;
                binding.imgDriverAvatar.setImageBitmap(img_bitmap);
                binding.imgDriverAvatar.setScaleType(ImageView.ScaleType.FIT_XY);
                break;
            case PICK_CURRICULUM_VITAE:
                view_model.curriculum_vitae_uri = img_uri;
                binding.imgCurriculumVitae.setImageBitmap(img_bitmap);
                binding.imgCurriculumVitae.setScaleType(ImageView.ScaleType.FIT_XY);
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_driver_infomation_tab, container, false);
        view_model = new ViewModelProvider(requireActivity()).get(DriverRegistrationViewModel.class);
        binding.setViewModel(view_model);
        InitView();
        RegisterActivityResult();
        return binding.getRoot();
    }
}