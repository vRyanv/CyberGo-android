package com.tech.cybercars.ui.main.fragment.trip.edit_trip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.data.models.TripManagement;
import com.tech.cybercars.databinding.ActivityEditTripInformationBinding;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.main.fragment.trip.trip_detail.TripDetailViewModel;

public class EditTripInformationActivity extends BaseActivity<ActivityEditTripInformationBinding, EditTripInformationViewModel> {
    @NonNull
    @Override
    protected EditTripInformationViewModel InitViewModel() {
        return new ViewModelProvider(this).get(EditTripInformationViewModel.class);
    }

    @Override
    protected ActivityEditTripInformationBinding InitBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_trip_information);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {
        binding.headerPrimary.btnOutScreen.setOnClickListener(view -> {
            OnBackPress();
        });
    }

    @Override
    protected void InitObserve() {

    }

    @Override
    protected void InitCommon() {
        TripManagement trip_management = (TripManagement) getIntent().getSerializableExtra(FieldName.TRIP);
        assert trip_management != null;
        view_model.trip_name.setValue(trip_management.trip_name);
        view_model.start_date.setValue(trip_management.start_date);
        view_model.start_time.setValue(trip_management.start_time);
        view_model.price.setValue(String.valueOf(trip_management.price));
        view_model.description.setValue(trip_management.description);
    }

    @Override
    protected void OnBackPress() {
        finish();
    }
}