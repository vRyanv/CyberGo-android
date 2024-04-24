package com.tech.cybercars.ui.main.fragment.trip.trip_detail;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayoutMediator;
import com.tech.cybercars.R;
import com.tech.cybercars.adapter.paper.TripDetailPageAdapter;
import com.tech.cybercars.adapter.paper.TripFoundDetailPageAdapter;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.data.models.TripManagement;
import com.tech.cybercars.databinding.ActivityTripDetailBinding;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.utils.SharedPreferencesUtil;

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

    }

    @Override
    protected void InitView() {
        binding.btnOutScreen.setOnClickListener(v -> {
            OnBackPress();
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

    }

    @Override
    protected void InitCommon() {
        TripManagement trip_management = (TripManagement) getIntent().getSerializableExtra(FieldName.TRIP);
        view_model.trip_management.setValue(trip_management);
        assert trip_management != null;
        ActiveTripEditor(trip_management);
    }

    @Override
    protected void OnBackPress() {
        finish();
    }

    private void ActiveTripEditor(TripManagement trip_management){
        String current_user_id = SharedPreferencesUtil.GetString(this, FieldName.USER_ID);
        binding.setIsTripOwner(trip_management.trip_owner.user_id.equals(current_user_id));
    }
}