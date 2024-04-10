package com.tech.cybercars.adapter.share_trip_information;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tech.cybercars.ui.main.fragment.go.share_trip.add_share_trip_information.fragment.LocationTabFragment;
import com.tech.cybercars.ui.main.fragment.go.share_trip.add_share_trip_information.fragment.TripInformationFragment;

public class ShareTripInformationAdapter extends FragmentStateAdapter {

    public ShareTripInformationAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new LocationTabFragment();
        }
        return new TripInformationFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
