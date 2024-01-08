package com.tech.cybercars.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.tech.cybercars.R;
import com.tech.cybercars.databinding.ActivityMainBinding;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.signup.SignUpViewModel;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {

    @NonNull
    @Override
    protected MainViewModel InitViewModel() {
        return new ViewModelProvider(this).get(MainViewModel.class);
    }

    @Override
    protected ActivityMainBinding InitBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitView() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.container_fragment_main);
        NavController nav_controller = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.bottomNavMain, nav_controller);
    }

    @Override
    protected void InitObserve() {

    }

    @Override
    protected void InitCommon() {

    }

    @Override
    protected void OnBackPress() {

    }
}