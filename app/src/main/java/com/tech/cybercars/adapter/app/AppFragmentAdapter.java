package com.tech.cybercars.adapter.app;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tech.cybercars.ui.main.fragment.activity.ActivityFragment;
import com.tech.cybercars.ui.main.fragment.go.GoFragment;
import com.tech.cybercars.ui.main.fragment.message.MessageFragment;
import com.tech.cybercars.ui.main.fragment.setting.SettingFragment;

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
                return new MessageFragment();
            case 3:
                return new SettingFragment();
            default:
                return new GoFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
