package com.tech.cybercars.ui.main.setting;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tech.cybercars.R;
import com.tech.cybercars.databinding.FragmentSettingBinding;
import com.tech.cybercars.ui.base.BaseFragment;

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
    protected void InitView() {

    }

    @Override
    protected void InitObserve() {

    }

    @Override
    protected void InitCommon() {

    }
}