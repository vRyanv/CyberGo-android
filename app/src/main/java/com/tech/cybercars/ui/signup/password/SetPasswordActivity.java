package com.tech.cybercars.ui.signup.password;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.tech.cybercars.BuildConfig;
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
        view_model.password.observe(this, password ->{
            if(!password.isEmpty()){
                binding.inputEnterPasswordRegister.setError(null);
            }
            Toast.makeText(this,  BuildConfig.BASE_URL, Toast.LENGTH_SHORT).show();
        });
        view_model.password_error.observe(this, password_error->{
            if(!password_error.isEmpty()){
                binding.inputEnterPasswordRegister.setError(password_error);
            }
        });

        view_model.confirm_password.observe(this, confirm_password ->{
            if(!confirm_password.isEmpty()){
                binding.inputConfirmPasswordRegister.setError(null);
            }
        });
        view_model.confirm_password_error.observe(this, confirm_password_error->{
            if(!confirm_password_error.isEmpty()){
                binding.inputConfirmPasswordRegister.setError(confirm_password_error);
            }
        });


    }

    @Override
    protected void InitCommon() {

    }
}