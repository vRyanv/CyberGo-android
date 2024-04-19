package com.tech.cybercars.ui.main.fragment.go.find_trip.trip_found_detail;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayoutMediator;
import com.tech.cybercars.R;
import com.tech.cybercars.adapter.trip_found_detail.TripFoundDetailAdapter;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.data.models.TripFound;
import com.tech.cybercars.databinding.ActivityTripFoundDetailBinding;
import com.tech.cybercars.ui.base.BaseActivity;

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
        binding.btnOutScreen.setOnClickListener(v ->{
            OnBackPress();
        });

        binding.btnRequestToJoin.setOnClickListener(view -> {
            view_model.HandleRequestToJoin();
        });
    }

    @Override
    protected void InitObserve() {

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
    }

    @Override
    protected void OnBackPress() {
        finish();
    }
}