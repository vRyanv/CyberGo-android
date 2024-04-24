package com.tech.cybercars.ui.main.fragment.go.share_trip.add_share_trip_information;

import android.app.Dialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayoutMediator;
import com.tech.cybercars.R;
import com.tech.cybercars.adapter.paper.ShareTripInformationPageAdapter;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.data.models.trip.Destination;
import com.tech.cybercars.data.models.trip.Trip;
import com.tech.cybercars.databinding.ActivityAddShareTripInformationBinding;
import com.tech.cybercars.services.eventbus.ActionEvent;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.component.dialog.NotificationDialog;
import com.tech.cybercars.ui.main.MainActivity;
import com.tech.cybercars.utils.KeyBoardUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class AddShareTripInformationActivity extends BaseActivity<ActivityAddShareTripInformationBinding, AddShareTripInformationViewModel> {

    @NonNull
    @Override
    protected AddShareTripInformationViewModel InitViewModel() {
        return new ViewModelProvider(this).get(AddShareTripInformationViewModel.class);
    }

    @Override
    protected ActivityAddShareTripInformationBinding InitBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_share_trip_information);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {
        binding.headerShareTripInformation.btnOutScreen.setOnClickListener(view -> {
            OnBackPress();
        });
    }

    @Override
    protected void InitObserve() {
        view_model.current_page.observe(this, current_page ->{
            binding.paperShareTripInformation.setCurrentItem(current_page, true);
        });

        view_model.is_loading.observe(this, is_loading -> {
            KeyBoardUtil.HideKeyBoard(this);
        });

        view_model.is_success.observe(this, is_success -> {
            ShowSuccessShareTrip();
        });

        view_model.error_call_server.observe(this, this::ShowErrorCallServer);
    }

    @Override
    protected void InitCommon() {
        binding.paperShareTripInformation.setUserInputEnabled(false);
        ShareTripInformationPageAdapter share_trip_information_fm_adapter = new ShareTripInformationPageAdapter(getSupportFragmentManager(), this.getLifecycle());
        binding.paperShareTripInformation.setAdapter(share_trip_information_fm_adapter);
        String[] tab_name = new String[]{getString(R.string.location),getString(R.string.trip_information)};
        new TabLayoutMediator(binding.tabShareTripInfo, binding.paperShareTripInformation, (tab, position) -> {
            tab.setText(tab_name[position]);
        }).attach();

        Trip trip = (Trip) getIntent().getSerializableExtra(FieldName.TRIP);
        view_model.trip.postValue(trip);

        ArrayList<Destination> destination_list = (ArrayList<Destination>) getIntent().getSerializableExtra(FieldName.DESTINATIONS);
        view_model.destination_list.setValue(destination_list);
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

    private void ShowSuccessShareTrip() {
        NotificationDialog.Builder(this)
                .SetIcon(R.drawable.ic_success)
                .SetTitle(getResources().getString(R.string.shared_successfully))
                .SetSubtitle(getResources().getString(R.string.you_will_get_notification_when_someone_else_wants_to_join_you))
                .SetTextMainButton(getResources().getString(R.string.trip_management))
                .SetOnMainButtonClicked(dialog -> {
                    Intent main_activity = new Intent(this, MainActivity.class);
                    main_activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(main_activity);
                    EventBus.getDefault().post(new ActionEvent(ActionEvent.GO_TO_TRIP_FRAGMENT));
                    finish();
                }).show();
    }
}