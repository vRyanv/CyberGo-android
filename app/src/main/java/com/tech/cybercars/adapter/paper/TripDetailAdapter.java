package com.tech.cybercars.adapter.paper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tech.cybercars.ui.main.fragment.go.find_trip.trip_found_detail.fragment.InformationTripFoundDetailFragment;
import com.tech.cybercars.ui.main.fragment.go.find_trip.trip_found_detail.fragment.LocationTripFoundDetailFragment;
import com.tech.cybercars.ui.main.fragment.go.share_trip.add_share_trip_information.fragment.LocationTabFragment;
import com.tech.cybercars.ui.main.fragment.go.share_trip.add_share_trip_information.fragment.TripInformationFragment;

public class TripDetailAdapter extends FragmentStateAdapter {
    public TripDetailAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new InformationTripFoundDetailFragment();
        }
        return new LocationTripFoundDetailFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
