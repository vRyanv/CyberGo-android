package com.tech.cybercars.adapter.share_trip_information;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tech.cybercars.ui.main.fragment.activity.ActivityFragment;
import com.tech.cybercars.ui.main.fragment.go.GoFragment;
import com.tech.cybercars.ui.main.fragment.go.add_share_trip_information.fragment.GeneralTabFragment;
import com.tech.cybercars.ui.main.fragment.go.add_share_trip_information.fragment.LocationTabFragment;
import com.tech.cybercars.ui.main.fragment.go.add_share_trip_information.fragment.ThumbTabFragment;
import com.tech.cybercars.ui.main.fragment.message.MessageFragment;
import com.tech.cybercars.ui.main.fragment.setting.SettingFragment;

public class ShareTripInformationAdapter extends FragmentStateAdapter {

    public ShareTripInformationAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new LocationTabFragment();
            case 1:
                return new GeneralTabFragment();
            case 2:
                return new ThumbTabFragment();
            default:
                return new LocationTabFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
