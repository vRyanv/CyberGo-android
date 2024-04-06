package com.tech.cybercars.ui.main.notification;

import android.app.Dialog;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tech.cybercars.R;
import com.tech.cybercars.adapter.notification.NotificationAdapter;
import com.tech.cybercars.data.models.Notification;
import com.tech.cybercars.databinding.ActivityNotificationBinding;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.component.dialog.NotificationDialog;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends BaseActivity<ActivityNotificationBinding, NotificationViewModel> {
    private NotificationAdapter notification_adapter;
    @NonNull
    @Override
    protected NotificationViewModel InitViewModel() {
        return new ViewModelProvider(this).get(NotificationViewModel.class);
    }

    @Override
    protected ActivityNotificationBinding InitBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {
        notification_adapter = new NotificationAdapter(this, new ArrayList<>());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rcvNotification.setLayoutManager(layoutManager);
        binding.rcvNotification.setAdapter(notification_adapter);

        binding.headerTitle.btnOutScreen.setOnClickListener(view -> {
            OnBackPress();
        });

        binding.swipeRefresh.setOnRefreshListener(() -> {
            view_model.HandleLoadNotification();
        });
    }

    @Override
    protected void InitObserve() {
        view_model.is_loading.observe(this, is_loading -> {
            if(is_loading){
                binding.skeletonLoading.startShimmerAnimation();
            } else {
                binding.skeletonLoading.stopShimmerAnimation();
                binding.swipeRefresh.setRefreshing(false);
            }
        });

        view_model.notification_list.observe(this, this::BindNotificationToList);

        view_model.error_call_server.observe(this, error_call_server -> {
            if(!error_call_server.equals("")){
                ShowErrorCallServer(error_call_server);
            }
        });
    }



    @Override
    protected void InitCommon() {
        view_model.HandleLoadNotification();
    }

    @Override
    protected void OnBackPress() {
        finish();
    }

    private void ShowErrorCallServer(String error_call_server) {
        NotificationDialog.Builder(this)
                .SetIcon(R.drawable.ic_error)
                .SetTitle(getResources().getString(R.string.something_went_wrong))
                .SetSubtitle(error_call_server)
                .SetTextMainButton(getResources().getString(R.string.close))
                .SetOnMainButtonClicked(Dialog::dismiss).show();
    }

    private void BindNotificationToList(List<Notification> notification_list) {
        if(!notification_list.isEmpty()){
            notification_adapter.UpdateData(notification_list);
        }
    }
}