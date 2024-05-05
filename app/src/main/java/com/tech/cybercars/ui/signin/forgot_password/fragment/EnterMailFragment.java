package com.tech.cybercars.ui.signin.forgot_password.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tech.cybercars.R;
import com.tech.cybercars.databinding.FragmentEnterMailBinding;
import com.tech.cybercars.ui.base.BaseFragment;
import com.tech.cybercars.ui.signin.SignInViewModel;
import com.tech.cybercars.ui.signin.forgot_password.ForgotPasswordViewModel;

public class EnterMailFragment extends BaseFragment<FragmentEnterMailBinding, ForgotPasswordViewModel> {


    @NonNull
    @Override
    protected ForgotPasswordViewModel InitViewModel() {
        return new ViewModelProvider(requireActivity()).get(ForgotPasswordViewModel.class);
    }

    @Override
    protected FragmentEnterMailBinding InitBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_enter_mail, container, false);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {
        binding.btnNext.setOnClickListener(view -> {
            view_model.current_step.setValue(view_model.ENTER_OTP_STEP);
        });
    }

    @Override
    protected void InitObserve() {

    }

    @Override
    protected void InitCommon() {

    }
}