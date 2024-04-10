package com.tech.cybercars.adapter.driver_registration;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tech.cybercars.ui.main.fragment.account.driver_register.fragment.DrivingLicenseTabFragment;
import com.tech.cybercars.ui.main.fragment.account.driver_register.fragment.VehicleRegistrationCertificateTabFragment;
import com.tech.cybercars.ui.main.fragment.account.driver_register.fragment.VehicleTabFragment;

public class DriverRegistrationAdapter extends FragmentStateAdapter {
    public DriverRegistrationAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new VehicleRegistrationCertificateTabFragment();
            case 1:
                return new DrivingLicenseTabFragment();
            default:
                return new VehicleTabFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
