package com.tech.cybercars.ui.main.fragment.trip.trip_detail;

import android.app.Dialog;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayoutMediator;
import com.tech.cybercars.R;
import com.tech.cybercars.adapter.paper.TripDetailPageAdapter;
import com.tech.cybercars.constant.ActivityResult;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.TripStatus;
import com.tech.cybercars.data.models.TripManagement;
import com.tech.cybercars.databinding.ActivityTripDetailBinding;
import com.tech.cybercars.services.eventbus.TripFinishEvent;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.component.dialog.DeleteDialog;
import com.tech.cybercars.ui.component.dialog.NotificationDialog;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class TripDetailActivity extends BaseActivity<ActivityTripDetailBinding, TripDetailViewModel> {

    @NonNull
    @Override
    protected TripDetailViewModel InitViewModel() {
        return new ViewModelProvider(this).get(TripDetailViewModel.class);
    }

    @Override
    protected ActivityTripDetailBinding InitBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_trip_detail);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void InitView() {
        binding.btnDeleteTrip.setOnClickListener(view -> {
            new DeleteDialog(this, getString(R.string.delete_this_trip))
                    .SetButtonSelectedCallback(new DeleteDialog.SelectButtonCallback() {
                        @Override
                        public void OnDelete(Dialog dialog) {
                            dialog.dismiss();
                        }

                        @Override
                        public void OnCancel(Dialog dialog) {
                            dialog.dismiss();
                        }
                    }).show();
        });

        binding.btnOutScreen.setOnClickListener(v -> {
            OnBackPress();
        });


        binding.btnCloseTrip.setOnClickListener(view -> {
            view_model.UpdateTripStatus(TripStatus.CLOSED);
        });

        binding.btnReopenTrip.setOnClickListener(view -> {
            view_model.UpdateTripStatus(TripStatus.OPENING);
        });

        binding.btnFinishTrip.setOnClickListener(view -> {
            view_model.UpdateTripStatus(TripStatus.FINISH);
        });

        binding.btnDeleteTrip.setOnClickListener(view -> {
            new DeleteDialog(this, getString(R.string.delete_this_trip))
                    .SetButtonSelectedCallback(new DeleteDialog.SelectButtonCallback() {
                        @Override
                        public void OnDelete(Dialog dialog) {
                            view_model.HandleDeleteTrip();
                            dialog.dismiss();
                        }

                        @Override
                        public void OnCancel(Dialog dialog) {
                            dialog.dismiss();
                        }
                    }).show();
        });

        TripDetailPageAdapter trip_detail_page = new TripDetailPageAdapter(getSupportFragmentManager(), this.getLifecycle());
        binding.paperTripDetail.setAdapter(trip_detail_page);
        binding.paperTripDetail.setUserInputEnabled(true);
        String[] tab_name = new String[]{
                getString(R.string.information),
                getString(R.string.location),
                getString(R.string.member)};
        new TabLayoutMediator(binding.tabTripDetail, binding.paperTripDetail, (tab, position) -> {
            tab.setText(tab_name[position]);
        }).attach();

    }

    @Override
    protected void InitObserve() {
        view_model.error_call_server.observe(this, this::ShowErrorCallServer);
        view_model.trip_management.observe(this, this::ActiveTripEditor);
        view_model.is_leave_trip_success.observe(this, is_leave_trip_success -> {
            finish();
        });
        view_model.is_delete_trip_success.observe(this, is_delete_trip_success -> {
            Toast.makeText(this, getString(R.string.deleted_successfully), Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    @Override
    protected void InitCommon() {
        TripManagement trip_management = (TripManagement) getIntent().getSerializableExtra(FieldName.TRIP);
        view_model.trip_management.setValue(trip_management);
    }

    @Override
    protected void OnBackPress() {
        finish();
    }

    private void ActiveTripEditor(TripManagement trip_management) {
        if (trip_management == null) {
            return;
        }
        // hide close and show re open
        if (trip_management.trip_status.equals(TripStatus.CLOSED)) {
            binding.btnCloseTrip.setVisibility(View.GONE);
            binding.btnReopenTrip.setVisibility(View.VISIBLE);
        } else {
            binding.btnCloseTrip.setVisibility(View.VISIBLE);
            binding.btnReopenTrip.setVisibility(View.GONE);
        }

        //show edit button if trip owner
        String current_user_id = SharedPreferencesUtil.GetString(this, FieldName.USER_ID);
        boolean is_trip_owner = trip_management.trip_owner.user_id.equals(current_user_id);
        binding.setIsTripOwner(is_trip_owner);

        //show control if trip owner and trip not finish status
        if (is_trip_owner && !trip_management.trip_status.equals(TripStatus.FINISH)) {
            view_model.can_perform_trip.setValue(true);
            binding.wrapperControlButton.setVisibility(View.VISIBLE);

        } else {
            view_model.can_perform_trip.setValue(false);
            binding.wrapperControlButton.setVisibility(View.GONE);
        }


        if (!is_trip_owner && !trip_management.trip_status.equals(TripStatus.FINISH)) {
            binding.wrapperBtnLeave.setVisibility(View.VISIBLE);
            binding.btnLeaveTrip.setOnClickListener(view -> {
                TripManagement.Member member_leave = null;
                for (TripManagement.Member member : trip_management.members) {
                    if (member.user_id.equals(current_user_id)) {
                        member_leave = member;
                        break;
                    }
                }
                LeaveTrip(trip_management.trip_id, member_leave);
            });
        } else {
            binding.wrapperBtnLeave.setVisibility(View.GONE);
        }
    }

    private void LeaveTrip(String trip_id, TripManagement.Member member) {
        new DeleteDialog(this, getString(R.string.leave_this_trip)).SetTextDeleteButton(getString(R.string.leave))
                .SetButtonSelectedCallback(new DeleteDialog.SelectButtonCallback() {
                    @Override
                    public void OnDelete(Dialog dialog) {
                        view_model.HandleLeaveTrip(trip_id, member);
                        dialog.dismiss();
                    }

                    @Override
                    public void OnCancel(Dialog dialog) {
                        dialog.dismiss();
                    }
                }).show();

    }

    private void ShowErrorCallServer(String error_call_server) {
        NotificationDialog.Builder(this)
                .SetIcon(R.drawable.ic_error)
                .SetTitle(getResources().getString(R.string.something_went_wrong))
                .SetSubtitle(error_call_server)
                .SetTextMainButton(getResources().getString(R.string.close))
                .SetOnMainButtonClicked(Dialog::dismiss).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnTripFinishEvent(TripFinishEvent trip_finish_event) {
        String trip_id = trip_finish_event.trip_id;
        String status = trip_finish_event.status;
        if (view_model.trip_management.getValue().trip_id.equals(trip_id)) {
            view_model.UpdateTripFinish(status);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}