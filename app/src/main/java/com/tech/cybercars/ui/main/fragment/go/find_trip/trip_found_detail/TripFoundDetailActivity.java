package com.tech.cybercars.ui.main.fragment.go.find_trip.trip_found_detail;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayoutMediator;
import com.tech.cybercars.R;
import com.tech.cybercars.adapter.paper.TripFoundDetailAdapter;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.data.remote.trip.find_trip.MemberBody;
import com.tech.cybercars.data.models.TripFound;
import com.tech.cybercars.databinding.ActivityTripFoundDetailBinding;
import com.tech.cybercars.services.eventbus.ActionEvent;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.component.dialog.NotificationDialog;
import com.tech.cybercars.ui.main.MainActivity;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;

public class TripFoundDetailActivity extends BaseActivity<ActivityTripFoundDetailBinding, TripFoundDetailViewModel> {

    @NonNull
    @Override
    protected TripFoundDetailViewModel InitViewModel() {
        return new ViewModelProvider(this).get(TripFoundDetailViewModel.class);
    }

    @Override
    protected ActivityTripFoundDetailBinding InitBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_trip_found_detail);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {
        binding.btnOutScreen.setOnClickListener(v -> {
            OnBackPress();
        });

        binding.btnRequestToJoin.setOnClickListener(view -> {
            view_model.HandleRequestToJoin();
        });
    }

    @Override
    protected void InitObserve() {
        view_model.is_success.observe(this, is_success -> {
            ShowSuccessRequestJoinTrip();
        });

        view_model.trip_found.observe(this, this::BindDataToUI);

        view_model.error_call_server.observe(this, this::ShowCallServerError);
    }



    @Override
    protected void InitCommon() {
        TripFoundDetailAdapter trip_found_detail = new TripFoundDetailAdapter(getSupportFragmentManager(), this.getLifecycle());
        binding.paperTripFoundDetail.setAdapter(trip_found_detail);
        binding.paperTripFoundDetail.setUserInputEnabled(true);
        String[] tab_name = new String[]{
                getString(R.string.information),
                getString(R.string.location),
                getString(R.string.member)};
        new TabLayoutMediator(binding.tabTripFoundDetail, binding.paperTripFoundDetail, (tab, position) -> {
            tab.setText(tab_name[position]);
        }).attach();

        TripFound trip_found = (TripFound) getIntent().getSerializableExtra(FieldName.TRIP_FOUND);
        view_model.trip_found.setValue(trip_found);

        view_model.member = (MemberBody) getIntent().getSerializableExtra(FieldName.MEMBER);
    }

    @Override
    protected void OnBackPress() {
        finish();
    }

    private void BindDataToUI(TripFound trip_found) {
        String user_id = SharedPreferencesUtil.GetString(this, FieldName.USER_ID);
        for (TripFound.User member: trip_found.member_list) {
            if(member.user_id.equals(user_id)){
                binding.btnRequestToJoin.setVisibility(View.GONE);
                binding.requestInQueue.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    private void ShowSuccessRequestJoinTrip(){
        NotificationDialog.Builder(this)
                .SetIcon(R.drawable.ic_success)
                .SetTitle(getResources().getString(R.string.request_has_been_sent))
                .SetSubtitle(getResources().getString(R.string.the_request_to_join_the_trip_has_been_sent_please_wait_for_notification_from_owner))
                .SetTextMainButton(getResources().getString(R.string.trip_management))
                .SetOnMainButtonClicked(dialog -> {
                    Intent main_activity = new Intent(this, MainActivity.class);
                    main_activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(main_activity);
                    EventBus.getDefault().post(new ActionEvent(ActionEvent.GO_TO_TRIP_FRAGMENT));
                    finish();
                }).show();
    }

    private void ShowCallServerError(String error_call_server) {
        NotificationDialog.Builder(this)
                .SetIcon(R.drawable.ic_error)
                .SetTitle(getResources().getString(R.string.something_went_wrong))
                .SetSubtitle(error_call_server)
                .SetTextMainButton(getResources().getString(R.string.close))
                .SetOnMainButtonClicked(Dialog::dismiss).show();
    }
}