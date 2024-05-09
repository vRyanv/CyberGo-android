package com.tech.cybercars.ui.main;


import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.tech.cybercars.R;
import com.tech.cybercars.adapter.paper.AppFragmentPageAdapter;
import com.tech.cybercars.constant.ActivityResult;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.PaperMain;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.data.models.Notification;
import com.tech.cybercars.databinding.ActivityMainBinding;
import com.tech.cybercars.services.eventbus.ActionEvent;
import com.tech.cybercars.services.notification.NotificationService;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.main.chat.ChatActivity;
import com.tech.cybercars.ui.main.notification.NotificationActivity;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> implements NavigationView.OnNavigationItemSelectedListener {

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
            if (R.id.go_fragment_item == selected_item) {
                binding.pagerMain.setCurrentItem(PaperMain.GO_FRAGMENT, true);
            } else if (R.id.trip_fragment_item == selected_item) {
                binding.pagerMain.setCurrentItem(PaperMain.TRIP_FRAGMENT, true);
            } else if (R.id.account_fragment_item == selected_item) {
                binding.pagerMain.setCurrentItem(PaperMain.ACCOUNT_FRAGMENT, true);
            }
            return true;
        });

        binding.pagerMain.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case PaperMain.GO_FRAGMENT:
                        binding.bottomNavMain.setSelectedItemId(R.id.go_fragment_item);
                        break;
                    case PaperMain.TRIP_FRAGMENT:
                        binding.bottomNavMain.setSelectedItemId(R.id.trip_fragment_item);
                        break;
                    default:
                        binding.bottomNavMain.setSelectedItemId(R.id.account_fragment_item);
                        break;
                }
            }
        });

        binding.btnOpenNavDrawer.setOnClickListener(view -> {
            binding.drawerLayout.open();
        });

        binding.btnOpenNotification.setOnClickListener(view -> {
            main_launcher.launch(new Intent(this, NotificationActivity.class));
            view_model.has_notification.setValue(false);
            SharedPreferencesUtil.SetBoolean(this, SharedPreferencesUtil.HAS_NOTIFICATION, false);
        });

        binding.btnOpenChat.setOnClickListener(view -> {
            startActivity(new Intent(this, ChatActivity.class));
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
        boolean has_notification = SharedPreferencesUtil.GetBoolean(this, SharedPreferencesUtil.HAS_NOTIFICATION);
        view_model.has_notification.setValue(has_notification);

        InitHeaderDrawer();

        AppFragmentPageAdapter app_fm_adapter = new AppFragmentPageAdapter(getSupportFragmentManager(), this.getLifecycle());
        binding.pagerMain.setAdapter(app_fm_adapter);
        binding.pagerMain.setUserInputEnabled(false);
        view_model.HandleUpdateFirebaseToken();
    }

    @Subscribe
    public void GoToTripFragment(ActionEvent action_event) {
        if (action_event.action.equals(ActionEvent.GO_TO_TRIP_FRAGMENT)) {
            binding.pagerMain.setCurrentItem(PaperMain.TRIP_FRAGMENT);
            binding.bottomNavMain.setSelectedItemId(R.id.trip_fragment_item);
        }
    }

    @Override
    protected void OnBackPress() {

    }

    private final ActivityResultLauncher<Intent> main_launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == ActivityResult.GO_TO_TRIP_FRAGMENT) {
                    binding.pagerMain.setCurrentItem(PaperMain.TRIP_FRAGMENT);
                }
            }
    );

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnNotificationEvent(Notification notification) {
        view_model.has_notification.setValue(true);
        SharedPreferencesUtil.SetBoolean(this, SharedPreferencesUtil.HAS_NOTIFICATION, true);
    }

    private ImageView img_avatar_drawer;
    private TextView txt_full_name_drawer;
    private TextView txt_phone_drawer;

    private void InitHeaderDrawer() {
        View view = binding.navDrawerView.getHeaderView(0);
        String avatar = SharedPreferencesUtil.GetString(this, FieldName.AVATAR);
        String avatar_full_path = URL.BASE_URL + URL.AVATAR_RES_PATH + avatar;
        img_avatar_drawer = view.findViewById(R.id.img_avatar_drawer);

        Glide.with(this)
                .load(avatar_full_path)
                .into(img_avatar_drawer);

        String full_name = SharedPreferencesUtil.GetString(this, FieldName.FULL_NAME);
        txt_full_name_drawer = view.findViewById(R.id.txt_full_name_drawer);
        txt_full_name_drawer.setText(full_name);

        String phone_number = SharedPreferencesUtil.GetString(this, FieldName.PHONE_NUMBER);
        txt_phone_drawer = view.findViewById(R.id.txt_phone_drawer);
        txt_phone_drawer.setText(phone_number);
    }

    @Subscribe
    public void UpdateAvatarDrawer(ActionEvent action_event) {
        if (action_event.action.equals(ActionEvent.UPDATE_DRAWER_INFO)) {
            String avatar = SharedPreferencesUtil.GetString(this, FieldName.AVATAR);
            String avatar_full_path = URL.BASE_URL + URL.AVATAR_RES_PATH + avatar;
            Glide.with(this).load(avatar_full_path).into(img_avatar_drawer);

            String full_name = SharedPreferencesUtil.GetString(this, FieldName.FULL_NAME);
            txt_full_name_drawer.setText(full_name);

            String phone = SharedPreferencesUtil.GetString(this, FieldName.PHONE_NUMBER);
            txt_phone_drawer.setText(phone);
        }
    }

    private void initDrawerNavigation() {
        binding.navDrawerView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        binding.drawerLayout.close();
        return false;
    }
}