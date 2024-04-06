package com.tech.cybercars.adapter.app;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tech.cybercars.ui.main.fragment.account.AccountFragment;
import com.tech.cybercars.ui.main.fragment.activity.ActivityFragment;
import com.tech.cybercars.ui.main.fragment.go.GoFragment;

public class AppFragmentAdapter extends FragmentStateAdapter {
    public AppFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new GoFragment();
            case 1:
                return new ActivityFragment();
            case 2:
                return new AccountFragment();
            default:
                return new GoFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
