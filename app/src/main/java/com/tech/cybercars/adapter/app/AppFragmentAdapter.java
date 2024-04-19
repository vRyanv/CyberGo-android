package com.tech.cybercars.adapter.app;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tech.cybercars.constant.PaperMain;
import com.tech.cybercars.ui.main.fragment.account.AccountFragment;
import com.tech.cybercars.ui.main.fragment.go.GoFragment;
import com.tech.cybercars.ui.main.fragment.trip.TripFragment;

public class AppFragmentAdapter extends FragmentStateAdapter {
    public AppFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case PaperMain.GO_FRAGMENT:
                return new GoFragment();
            case PaperMain.TRIP_FRAGMENT:
                return new TripFragment();
            default:
                return new AccountFragment();
        }
    }

    @Override
    public int getItemCount() {
        return PaperMain.COUNT;
    }
}
