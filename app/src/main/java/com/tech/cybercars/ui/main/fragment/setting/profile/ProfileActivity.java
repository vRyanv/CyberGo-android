package com.tech.cybercars.ui.main.fragment.setting.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.tech.cybercars.R;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.databinding.ActivityProfileBinding;
import com.tech.cybercars.ui.main.fragment.setting.driver_register.DriverRegistrationViewModel;

public class ProfileActivity extends BaseActivity<ActivityProfileBinding, ProfileViewModel> {

    @NonNull
    @Override
    protected ProfileViewModel InitViewModel() {
        return new ViewModelProvider(this).get(ProfileViewModel.class);
    }

    @Override
    protected ActivityProfileBinding InitBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {

    }

    @Override
    protected void InitObserve() {

    }

    @Override
    protected void InitCommon() {

    }

    @Override
    protected void OnBackPress() {
        finish();
    }
}