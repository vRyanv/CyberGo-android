package com.tech.cybercars.adapter.driver_registration;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tech.cybercars.ui.main.fragment.setting.driver_register.fragment.RegistrationCertificateTabFragment;
import com.tech.cybercars.ui.main.fragment.setting.driver_register.fragment.VehicleTabFragment;
import com.tech.cybercars.ui.main.fragment.setting.driver_register.fragment.DrivingLicenseTabFragment;

public class DriverRegistrationAdapter extends FragmentStateAdapter {
    public DriverRegistrationAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new RegistrationCertificateTabFragment();
            case 1:
                return new DrivingLicenseTabFragment();
            case 2:
                return new VehicleTabFragment();
            default:
                return new RegistrationCertificateTabFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
