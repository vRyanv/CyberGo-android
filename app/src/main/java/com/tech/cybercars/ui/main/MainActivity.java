package com.tech.cybercars.ui.main;


import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.tech.cybercars.R;
import com.tech.cybercars.adapter.app.AppFragmentAdapter;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.Tag;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.data.models.Notification;
import com.tech.cybercars.databinding.ActivityMainBinding;
import com.tech.cybercars.services.notification.NotificationService;
import com.tech.cybercars.services.socket.SocketService;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.main.notification.NotificationActivity;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> implements NavigationView.OnNavigationItemSelectedListener{

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
        EventBus.getDefault().register(this);
    }

    @Override
    protected void InitView() {
        initDrawerNavigation();

        binding.bottomNavMain.setOnItemSelectedListener(item -> {
            int selected_item = item.getItemId();
            if(R.id.go_fragment_item == selected_item){
                binding.pagerMain.setCurrentItem(0, false);
            } else if (R.id.activity_fragment_item == selected_item) {
                binding.pagerMain.setCurrentItem(1, false);
            } else if (R.id.setting_fragment_item == selected_item) {
                binding.pagerMain.setCurrentItem(3, false);
            }
            return true;
        });

        binding.btnOpenNavDrawer.setOnClickListener(view -> {
            binding.drawerLayout.open();
        });

        binding.btnOpenNotification.setOnClickListener(view -> {
            startActivity(new Intent(this, NotificationActivity.class));
            view_model.has_notification.setValue(false);
        });

        binding.btnOpenMessage.setOnClickListener(view -> {

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
        InitHeaderDrawer();

        AppFragmentAdapter app_fm_adapter = new AppFragmentAdapter(getSupportFragmentManager(), this.getLifecycle());
        binding.pagerMain.setAdapter(app_fm_adapter);
        binding.pagerMain.setUserInputEnabled(false);
        view_model.HandleUpdateFirebaseToken();
    }

    @Override
    protected void OnBackPress() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnNotificationEvent(Notification notification){
        NotificationService.PushNormal(
                getApplicationContext(),
                notification.avatar,
                notification.title,
                notification.content
        );
        view_model.has_notification.setValue(true);
    }


    private void InitHeaderDrawer() {
        View view = binding.navDrawerView.getHeaderView(0);
        String avatar = SharedPreferencesUtil.GetString(this, FieldName.AVATAR);
        String avatar_full_path = URL.BASE_URL + URL.AVATAR_RES_PATH + avatar;
        ImageView img_avatar_drawer = view.findViewById(R.id.img_avatar_drawer);

        Glide.with(this)
                .load(avatar_full_path)
                .into(img_avatar_drawer);

        String full_name = SharedPreferencesUtil.GetString(this, FieldName.FULL_NAME);
        TextView txt_full_name_drawer = view.findViewById(R.id.txt_full_name_drawer);
        txt_full_name_drawer.setText(full_name);

        String phone_number = SharedPreferencesUtil.GetString(this, FieldName.PHONE_NUMBER);
        TextView txt_phone_drawer = view.findViewById(R.id.txt_phone_drawer);
        txt_phone_drawer.setText(phone_number);
    }

    private void initDrawerNavigation(){
        binding.navDrawerView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        binding.drawerLayout.close();
        return false;
    }
}