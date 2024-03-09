package com.tech.cybercars.ui.main.fragment.setting;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tech.cybercars.R;
import com.tech.cybercars.databinding.FragmentSettingBinding;
import com.tech.cybercars.ui.base.BaseFragment;
import com.tech.cybercars.ui.main.fragment.setting.driver_register.DriverRegistration;

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
            startActivity(new Intent(this.getActivity(), DriverRegistration.class));
        });
    }

    @Override
    protected void InitObserve() {

    }

    @Override
    protected void InitCommon() {

    }
}