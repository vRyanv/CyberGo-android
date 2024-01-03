package com.tech.cybercars.ui.signup.password;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.tech.cybercars.R;
import com.tech.cybercars.databinding.ActivitySetPasswordBinding;
import com.tech.cybercars.ui.base.BaseActivity;

public class SetPasswordActivity  extends BaseActivity<ActivitySetPasswordBinding, SetPasswordViewModel> {

    @NonNull
    @Override
    protected SetPasswordViewModel InitViewModel() {
        return new ViewModelProvider(this).get(SetPasswordViewModel.class);
    }

    @Override
    public ActivitySetPasswordBinding InitBinding(ViewModel view_model) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_password);
        binding.setViewModel((SetPasswordViewModel) view_model);
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