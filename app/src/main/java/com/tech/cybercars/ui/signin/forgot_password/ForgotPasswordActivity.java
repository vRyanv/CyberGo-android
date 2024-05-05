package com.tech.cybercars.ui.signin.forgot_password;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.tech.cybercars.R;
import com.tech.cybercars.adapter.paper.DriverRegistrationPageAdapter;
import com.tech.cybercars.adapter.paper.ForgotPasswordAdapter;
import com.tech.cybercars.databinding.ActivityForgotPasswordBinding;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.main.MainViewModel;
import com.tech.cybercars.ui.signin.forgot_password.fragment.EnterMailFragment;

public class ForgotPasswordActivity extends BaseActivity<ActivityForgotPasswordBinding, ForgotPasswordViewModel> {

    @NonNull
    @Override
    protected ForgotPasswordViewModel InitViewModel() {
        return new ViewModelProvider(this).get(ForgotPasswordViewModel.class);
    }

    @Override
    protected ActivityForgotPasswordBinding InitBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {
        ForgotPasswordAdapter forgot_pass_adapter = new ForgotPasswordAdapter(getSupportFragmentManager(), this.getLifecycle());
        binding.paperForgotPass.setAdapter(forgot_pass_adapter);

        binding.headerPrimary.btnOutScreen.setOnClickListener(view -> {
            OnBackPress();
        });
    }

    @Override
    protected void InitObserve() {
        view_model.current_step.observe(this, this::MoveToStep);
    }

    private void MoveToStep(int current_step) {
        if(current_step == view_model.ENTER_MAIL_STEP){
            binding.paperForgotPass.setCurrentItem(0);
        } else {
            binding.paperForgotPass.setCurrentItem(1);
        }
    }

    @Override
    protected void InitCommon() {

    }

    @Override
    protected void OnBackPress() {
        finish();
    }
}