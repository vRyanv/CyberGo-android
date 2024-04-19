package com.tech.cybercars.ui.main.fragment.account.driver_register.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.tech.cybercars.R;
import com.tech.cybercars.databinding.FragmentDrivingLicenseTabBinding;
import com.tech.cybercars.ui.base.BaseFragment;
import com.tech.cybercars.ui.main.fragment.account.driver_register.DriverRegistrationViewModel;
import com.tech.cybercars.utils.FileUtil;


public class DrivingLicenseTabFragment extends BaseFragment<FragmentDrivingLicenseTabBinding, DriverRegistrationViewModel> {

    private final String PICK_FRONT_DRIVING_LICENSE = "PICK_FRONT_DRIVING_LICENSE";
    private final String PICK_BACK_DRIVING_LICENSE = "PICK_BACK_DRIVING_LICENSE";
    private String current_pick_img_action;

    @NonNull
    @Override
    protected DriverRegistrationViewModel InitViewModel() {
        return new ViewModelProvider(requireActivity()).get(DriverRegistrationViewModel.class);
    }

    @Override
    protected FragmentDrivingLicenseTabBinding InitBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_driving_license_tab, container, false);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {
        binding.btnUploadFrontDrivingLicense.setOnClickListener(view -> {
            Intent take_photo = new Intent(Intent.ACTION_PICK);
            take_photo.setType("image/*");
            current_pick_img_action = PICK_FRONT_DRIVING_LICENSE;
            take_img_launcher.launch(take_photo);
        });

        binding.btnUploadBackDrivingLicense.setOnClickListener(view -> {
            Intent take_photo = new Intent(Intent.ACTION_PICK);
            take_photo.setType("image/*");
            current_pick_img_action = PICK_BACK_DRIVING_LICENSE;
            take_img_launcher.launch(take_photo);
        });
    }

    @Override
    protected void InitObserve() {

    }

    @Override
    protected void InitCommon() {

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


    private void BindImgToUI(Uri img_uri, Bitmap img_bitmap) {
        switch (current_pick_img_action) {
            case PICK_FRONT_DRIVING_LICENSE:
                view_model.front_driving_licence_uri = img_uri;
                binding.imgFrontDrivingLicenseCard.setImageBitmap(img_bitmap);
                binding.imgFrontDrivingLicenseCard.setScaleType(ImageView.ScaleType.FIT_XY);
                break;
            case PICK_BACK_DRIVING_LICENSE:
                view_model.back_driving_licence_uri = img_uri;
                binding.imgBackDrivingLicenseCard.setImageBitmap(img_bitmap);
                binding.imgBackDrivingLicenseCard.setScaleType(ImageView.ScaleType.FIT_XY);
                break;
        }
    }
}