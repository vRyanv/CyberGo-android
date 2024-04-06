package com.tech.cybercars.adapter.vehicle_detail;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tech.cybercars.ui.main.fragment.account.my_vehicle.vehicle_detail.fragments.DrivingLicenseDetaiTablFragment;
import com.tech.cybercars.ui.main.fragment.account.my_vehicle.vehicle_detail.fragments.VehicleDetailTabFragment;
import com.tech.cybercars.ui.main.fragment.account.my_vehicle.vehicle_detail.fragments.VehicleRegistrationCertificateDetailTabFragment;

public class VehicleDetailAdapter extends FragmentStateAdapter {
    public VehicleDetailAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new VehicleRegistrationCertificateDetailTabFragment();
            case 1:
                return new DrivingLicenseDetaiTablFragment();
            default:
                return new VehicleDetailTabFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
