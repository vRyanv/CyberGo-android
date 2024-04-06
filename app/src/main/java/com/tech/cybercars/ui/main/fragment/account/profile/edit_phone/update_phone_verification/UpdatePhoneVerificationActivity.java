package com.tech.cybercars.ui.main.fragment.account.profile.edit_phone.update_phone_verification;

import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.tech.cybercars.R;
import com.tech.cybercars.databinding.ActivityUpdatePhoneVerificationBinding;
import com.tech.cybercars.ui.base.BaseActivity;

public class UpdatePhoneVerificationActivity extends BaseActivity<ActivityUpdatePhoneVerificationBinding, UpdatePhoneVerificationViewModel> {

    @NonNull
    @Override
    protected UpdatePhoneVerificationViewModel InitViewModel() {
        return new ViewModelProvider(this).get(UpdatePhoneVerificationViewModel.class);
    }

    @Override
    protected ActivityUpdatePhoneVerificationBinding InitBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_phone_verification);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {
        binding.btnOutScreen.setOnClickListener(view ->{
            OnBackPress();
        });
    }

    @Override
    protected void InitObserve() {
        view_model.otp_1.observe(this, number_str -> {
            SwitchOTPCodeBox(number_str, binding.inputOtpNumber2Verification);
        });
        view_model.otp_2.observe(this, number_str -> {
            SwitchOTPCodeBox(number_str, binding.inputOtpNumber3Verification);
        });
        view_model.otp_3.observe(this, number_str -> {
            SwitchOTPCodeBox(number_str, binding.inputOtpNumber4Verification);
        });
        view_model.otp_4.observe(this, number_str -> {
            SwitchOTPCodeBox(number_str, binding.inputOtpNumber5Verification);
        });
        view_model.otp_5.observe(this, number_str -> {
            SwitchOTPCodeBox5(number_str, binding.inputOtpNumber5Verification);
        });
    }

    private void SwitchOTPCodeBox(String number_str, EditText next_input_otp){
        if(!number_str.equals("")){
            next_input_otp.requestFocus();
            next_input_otp.setSelection(next_input_otp.getText().length());
        }
    }

    private void SwitchOTPCodeBox5(String number_str, EditText input_otp_5){
        if(!number_str.equals("")){
            input_otp_5.clearFocus();
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