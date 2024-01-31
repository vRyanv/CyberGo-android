package com.tech.cybercars.ui.main;


import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.tech.cybercars.R;
import com.tech.cybercars.adapter.app.AppFragmentAdapter;
import com.tech.cybercars.databinding.ActivityMainBinding;
import com.tech.cybercars.ui.base.BaseActivity;

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
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {
        binding.bottomNavMain.setOnItemSelectedListener(item -> {
            int selected_item = item.getItemId();
            if(R.id.go_fragment_item == selected_item){
                binding.pagerMain.setCurrentItem(0, false);
            } else if (R.id.activity_fragment_item == selected_item) {
                binding.pagerMain.setCurrentItem(1, false);
            } else if (R.id.chat_fragment_item == selected_item) {
                binding.pagerMain.setCurrentItem(2, false);
            } else if (R.id.setting_fragment_item == selected_item) {
                binding.pagerMain.setCurrentItem(3, false);
            }
            return true;
        });

//        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.container_fragment_main);
//        assert navHostFragment != null;
//        NavController nav_controller = navHostFragment.getNavController();
//        NavigationUI.setupWithNavController(binding.bottomNavMain, nav_controller);
//        binding.bottomNavMain.getOrCreateBadge(R.id.chat_fragment_item).setNumber(99);
    }

    @Override
    protected void InitObserve() {

    }

    @Override
    protected void InitCommon() {
        AppFragmentAdapter app_fm_adapter = new AppFragmentAdapter(getSupportFragmentManager(), this.getLifecycle());
        binding.pagerMain.setAdapter(app_fm_adapter);
        binding.pagerMain.setUserInputEnabled(false);
    }

    @Override
    protected void OnBackPress() {

    }
}