package com.tech.cybercars.ui.main.fragment.setting;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.tech.cybercars.R;
import com.tech.cybercars.constant.ActivityResult;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.databinding.FragmentSettingBinding;
import com.tech.cybercars.ui.base.BaseFragment;
import com.tech.cybercars.ui.main.fragment.setting.driver_register.DriverRegistrationActivity;
import com.tech.cybercars.ui.main.fragment.setting.profile.ProfileActivity;
import com.tech.cybercars.utils.SharedPreferencesUtil;

public class SettingFragment extends BaseFragment<FragmentSettingBinding, SettingViewModel> {
    @NonNull
    @Override
    protected SettingViewModel InitViewModel() {
        return new ViewModelProvider(this).get(SettingViewModel.class);
    }

    @Override
    protected FragmentSettingBinding InitBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {
        binding.btnOpenRegisterAsDriver.setOnClickListener(view -> {
            startActivity(new Intent(this.getActivity(), DriverRegistrationActivity.class));
        });

        binding.btnOpenProfile.setOnClickListener(view->{
            profile_result_launcher.launch(new Intent(this.getActivity(), ProfileActivity.class));
        });
    }

    @Override
    protected void InitObserve() {

    }

    @Override
    protected void InitCommon() {
        BindUserUI();
    }

    private final ActivityResultLauncher<Intent> profile_result_launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
               BindUserUI();
            });

    private void BindUserUI(){
        String full_name = SharedPreferencesUtil.GetString(requireContext(), FieldName.FULL_NAME);
        binding.txtFullName.setText(full_name);

        String avatar = SharedPreferencesUtil.GetString(requireContext(), FieldName.AVATAR);
        String full_path_res = URL.BASE_URL + URL.AVATAR_RES_PATH + avatar;
        Glide.with(this)
                .load(full_path_res)
                .into(binding.imgAvatar);
    }
}