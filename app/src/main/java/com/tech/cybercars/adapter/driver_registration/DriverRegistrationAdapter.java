package com.tech.cybercars.adapter.driver_registration;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tech.cybercars.ui.main.fragment.setting.driver_register.fragment.DriverInfomationTabFragment;
import com.tech.cybercars.ui.main.fragment.setting.driver_register.fragment.DriverTransportTabFragment;
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
                return new DriverInfomationTabFragment();
            case 1:
                return new DrivingLicenseTabFragment();
            case 2:
                return new DriverTransportTabFragment();
            default:
                return new DriverInfomationTabFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
