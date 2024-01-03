package com.tech.cybercars.ui.signin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.tech.cybercars.R;
import com.tech.cybercars.databinding.ActivitySignInBinding;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.signup.SignUpViewModel;

public class SignInActivity extends BaseActivity<ActivitySignInBinding, SignInViewModel> {


    @NonNull
    @Override
    protected SignInViewModel InitViewModel() {
        return new ViewModelProvider(this).get(SignInViewModel.class);
    }

    @Override
    public ActivitySignInBinding InitBinding(ViewModel view_model) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);
        binding.setViewModel((SignInViewModel) view_model);
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