package com.tech.cybercars.adapter.paper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tech.cybercars.ui.main.fragment.trip.trip_detail.fragment.InformationTripDetailFragment;
import com.tech.cybercars.ui.main.fragment.trip.trip_detail.fragment.LocationTripDetailFragment;
import com.tech.cybercars.ui.main.fragment.trip.trip_detail.fragment.MemberTripDetailFragment;

public class TripDetailPageAdapter extends FragmentStateAdapter {
    public TripDetailPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new InformationTripDetailFragment();
            case 1:
                return new LocationTripDetailFragment();
            default:
                return new MemberTripDetailFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
