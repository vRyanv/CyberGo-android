package com.tech.cybercars.adapter.paper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tech.cybercars.ui.signin.forgot_password.fragment.EnterMailFragment;
import com.tech.cybercars.ui.signin.forgot_password.fragment.EnterOTPFragment;

public class ForgotPasswordAdapter extends FragmentStateAdapter {
    public ForgotPasswordAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new EnterMailFragment();
        }
        return new EnterOTPFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
