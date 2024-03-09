package com.tech.cybercars.ui.main.fragment.setting.driver_register;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayoutMediator;
import com.tech.cybercars.R;
import com.tech.cybercars.adapter.driver_registration.DriverRegistrationAdapter;
import com.tech.cybercars.adapter.share_trip_information.ShareTripInformationAdapter;
import com.tech.cybercars.databinding.ActivityDriverRegistrationBinding;
import com.tech.cybercars.ui.base.BaseActivity;

public class DriverRegistration extends BaseActivity<ActivityDriverRegistrationBinding, DriverRegistrationViewModel> {
    @NonNull
    @Override
    protected DriverRegistrationViewModel InitViewModel() {
        return new ViewModelProvider(this).get(DriverRegistrationViewModel.class);
    }

    @Override
    protected ActivityDriverRegistrationBinding InitBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_driver_registration);
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
        DriverRegistrationAdapter driver_registration_adapter = new DriverRegistrationAdapter(getSupportFragmentManager(), this.getLifecycle());
        binding.paperDriverRegistration.setAdapter(driver_registration_adapter);
        String tab_name[] = new String[]{
                getString(R.string.my_information),
                getString(R.string.driving_license),
                getString(R.string.my_transport)};
        new TabLayoutMediator(binding.tabShareTripInfo, binding.paperDriverRegistration, (tab, position) -> {
            tab.setText(tab_name[position]);
        }).attach();
    }

    @Override
    protected void OnBackPress() {

    }
}