package com.tech.cybercars.ui.main.fragment.account.profile.edit_phone;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.databinding.ActivityEditPhoneBinding;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.main.fragment.account.profile.edit_phone.update_phone_verification.UpdatePhoneVerificationActivity;

public class EditPhoneActivity extends BaseActivity<ActivityEditPhoneBinding, EditPhoneViewModel> {

    @NonNull
    @Override
    protected EditPhoneViewModel InitViewModel() {
        return new ViewModelProvider(this).get(EditPhoneViewModel.class);
    }

    @Override
    protected ActivityEditPhoneBinding InitBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_phone);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {
        InitCountryPicker();
        String phone_number = getIntent().getStringExtra(FieldName.PHONE_NUMBER);
        view_model.phone_number.setValue(phone_number);

        String country_name_code = getIntent().getStringExtra(FieldName.COUNTRY_NAME_CODE);
        view_model.country_name_code.setValue(country_name_code);
        binding.countryCodePicker.setCountryForNameCode(country_name_code);

        binding.btnOutScreen.setOnClickListener(view -> {
            OnBackPress();
        });

        binding.btnUpdatePhone.setOnClickListener(view -> {
            startActivity(new Intent(this,  UpdatePhoneVerificationActivity.class));
        });
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

    private void InitCountryPicker(){
        binding.countryCodePicker.registerCarrierNumberEditText(binding.inputPhone);
        binding.countryCodePicker.setOnCountryChangeListener(() -> {
            view_model.country_name_code.setValue(binding.countryCodePicker.getSelectedCountryNameCode());
        });
    }
}