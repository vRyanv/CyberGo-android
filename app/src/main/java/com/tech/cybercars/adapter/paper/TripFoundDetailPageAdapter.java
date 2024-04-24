package com.tech.cybercars.adapter.paper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tech.cybercars.ui.main.fragment.go.find_trip.trip_found_detail.fragment.InformationTripFoundDetailFragment;
import com.tech.cybercars.ui.main.fragment.go.find_trip.trip_found_detail.fragment.LocationTripFoundDetailFragment;
import com.tech.cybercars.ui.main.fragment.go.find_trip.trip_found_detail.fragment.MemberTripFoundDetailFragment;

public class TripFoundDetailPageAdapter extends FragmentStateAdapter {
    public TripFoundDetailPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new InformationTripFoundDetailFragment();
            case 1:
                return new LocationTripFoundDetailFragment();
            default:
                return new MemberTripFoundDetailFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
