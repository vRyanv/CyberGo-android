package com.tech.cybercars.adapter.paper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tech.cybercars.ui.main.fragment.trip.fragment.JoinedTripFragment;
import com.tech.cybercars.ui.main.fragment.trip.fragment.SharedTripFragment;

public class TripManagementPageAdapter extends FragmentStateAdapter {


    public TripManagementPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new JoinedTripFragment();
        }
        return new SharedTripFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
