package com.tech.cybercars.ui.main.fragment.go.share_trip.add_share_trip_information.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.tech.cybercars.R;
import com.tech.cybercars.databinding.FragmentTripInformationBinding;
import com.tech.cybercars.ui.base.BaseFragment;
import com.tech.cybercars.ui.main.fragment.account.driver_register.DriverRegistrationViewModel;
import com.tech.cybercars.ui.main.fragment.go.share_trip.add_share_trip_information.AddShareTripInformationViewModel;
import com.tech.cybercars.utils.DateTimePicker;
import com.tech.cybercars.utils.TimePicker;

import java.util.Calendar;

public class TripInformationFragment extends BaseFragment<FragmentTripInformationBinding, AddShareTripInformationViewModel> {
    private TimePicker time_picker;
    private DateTimePicker date_time_picker;

    @NonNull
    @Override
    protected AddShareTripInformationViewModel InitViewModel() {
        return new ViewModelProvider(requireActivity()).get(AddShareTripInformationViewModel.class);
    }

    @Override
    protected FragmentTripInformationBinding InitBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_trip_information, container, false);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    public void InitView() {
        time_picker = new TimePicker(getChildFragmentManager());
        time_picker.SetTimePickerCallback((hourOfDay, minute) -> {
            String start_time = hourOfDay + ":" + minute;
            view_model.start_time.setValue(start_time);
        });
        binding.inputStartTime.getEditText().setOnClickListener(view -> {
            time_picker.Run();
        });

        date_time_picker = new DateTimePicker(
                getChildFragmentManager(),
                DateTimePicker.M_D_Y
        );
        date_time_picker.SetOnDateTimePicked((calendar, date_time_format) -> {
            view_model.start_date.setValue(date_time_format);
        });
        binding.inputStartDate.getEditText().setOnClickListener(view -> {
            date_time_picker.Run();
        });

        binding.btnPreviousToLocationInfoFm.setOnClickListener(view -> {
            view_model.current_page.setValue(0);
        });
    }

    @Override
    protected void InitObserve() {

    }

    @Override
    protected void InitCommon() {

    }
}