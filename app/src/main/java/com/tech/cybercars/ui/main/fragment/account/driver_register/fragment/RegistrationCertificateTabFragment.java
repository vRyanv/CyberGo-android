package com.tech.cybercars.ui.main.fragment.account.driver_register.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.VehicleType;
import com.tech.cybercars.databinding.FragmentVehicleRegistrationCertificateTabBinding;
import com.tech.cybercars.ui.base.BaseFragment;
import com.tech.cybercars.ui.main.fragment.account.driver_register.DriverRegistrationViewModel;
import com.tech.cybercars.utils.FileUtil;

public class RegistrationCertificateTabFragment extends BaseFragment<FragmentVehicleRegistrationCertificateTabBinding, DriverRegistrationViewModel> {
    private final String PICK_BACK_CERTIFICATE = "PICK_BACK_CERTIFICATE";
    private final String PICK_FRONT_CERTIFICATE = "PICK_FRONT_CERTIFICATE";
    private String current_pick_img_action;
    private String[] vehicle_type_choices;
    private AlertDialog vehicle_type_dialog;

    @NonNull
    @Override
    protected DriverRegistrationViewModel InitViewModel() {
        return new ViewModelProvider(requireActivity()).get(DriverRegistrationViewModel.class);
    }

    @Override
    protected FragmentVehicleRegistrationCertificateTabBinding InitBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_vehicle_registration_certificate_tab, container, false);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {
        InitAlertVehicleTypeDialog();

        //vehicle type selection
        binding.inputVehicleType.getEditText().setOnClickListener(view -> {
            vehicle_type_dialog.show();
        });

        binding.btnUploadFrontCertificate.setOnClickListener(view -> {
            Intent take_photo = new Intent(Intent.ACTION_PICK);
            take_photo.setType("image/*");
            current_pick_img_action = PICK_FRONT_CERTIFICATE;
            take_img_launcher.launch(take_photo);
        });

        binding.btnUploadBackCertificate.setOnClickListener(view -> {
            Intent take_photo = new Intent(Intent.ACTION_PICK);
            take_photo.setType("image/*");
            current_pick_img_action = PICK_BACK_CERTIFICATE;
            take_img_launcher.launch(take_photo);
        });
    }

    @Override
    protected void InitObserve() {
//        view_model.vehicle_type.observe(this, vehicle_type -> {
//                binding.inputVehicleType.getEditText().setText(vehicle_type);
//        });
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

    private void BindImgToUI(Uri img_uri, Bitmap img_bitmap){
        switch (current_pick_img_action) {
            case PICK_FRONT_CERTIFICATE:
                view_model.front_vehicle_registration_certificate_uri = img_uri;
                binding.imgFrontCertificate.setImageBitmap(img_bitmap);
                binding.imgFrontCertificate.setScaleType(ImageView.ScaleType.FIT_XY);
                break;
            case PICK_BACK_CERTIFICATE:
                view_model.back_vehicle_registration_certificate_uri = img_uri;
                binding.imgBackCertificate.setImageBitmap(img_bitmap);
                binding.imgBackCertificate.setScaleType(ImageView.ScaleType.FIT_XY);
                break;
        }
    }

    private void InitAlertVehicleTypeDialog() {
        vehicle_type_choices = new String[]{VehicleType.CAR, VehicleType.MOTO, VehicleType.TRUCK};
        AlertDialog.Builder gender_dialog_builder = new AlertDialog.Builder(requireContext());
        gender_dialog_builder
                .setTitle(getResources().getString(R.string.select_vehicle_type))
                .setItems(vehicle_type_choices, (dialog, which) -> {
                    view_model.vehicle_type.setValue(vehicle_type_choices[which]);
                    dialog.dismiss();
                });
        vehicle_type_dialog = gender_dialog_builder.create();
    }
}