package com.tech.cybercars.ui.main.fragment.trip.edit_trip;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.data.models.TripManagement;
import com.tech.cybercars.databinding.ActivityEditTripInformationBinding;
import com.tech.cybercars.services.eventbus.UpdateTripInformationEvent;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.utils.DateTimePicker;
import com.tech.cybercars.utils.DateUtil;
import com.tech.cybercars.utils.TimePicker;

import org.greenrobot.eventbus.EventBus;

public class EditTripInformationActivity extends BaseActivity<ActivityEditTripInformationBinding, EditTripInformationViewModel> {
    private TripManagement trip_management;
    private DateTimePicker date_time_picker;
    private TimePicker time_picker;
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

        date_time_picker = new DateTimePicker(
                getSupportFragmentManager(),
                DateTimePicker.M_D_Y
        );
        date_time_picker.SetOnDateTimePicked((calendar, date_time_format) -> {
            view_model.start_date.setValue(date_time_format);
            view_model.start_date_data = DateUtil.YMDFormat(calendar);
        });
        binding.inputStartDate.getEditText().setOnClickListener(view -> {
            date_time_picker.Run();
        });


        time_picker = new TimePicker(getSupportFragmentManager());
        time_picker.SetTimePickerCallback((hourOfDay, minute) -> {
            String start_time = hourOfDay + ":" + minute;
            view_model.start_time.setValue(start_time);
        });
        binding.inputStartTime.getEditText().setOnClickListener(view -> {
            time_picker.Run();
        });

        binding.btnUpdateTrip.setOnClickListener(view -> {
            view_model.HandleUpdateTripInformation();
        });
    }

    @Override
    protected void InitObserve() {
        view_model.trip_name.observe(this, trip_name->{
            if(!trip_name.equals("")){
                binding.inputTripName.setError(null);
            }
        });
        view_model.trip_name_error.observe(this, trip_name_error->{
            binding.inputTripName.setError(trip_name_error);
        });

        view_model.start_date.observe(this, start_date->{
            if(!start_date.equals("")){
                binding.inputStartDate.setError(null);
            }
        });
        view_model.start_date_error.observe(this, start_date_error->{
            binding.inputStartDate.setError(start_date_error);
        });

        view_model.start_time.observe(this, start_time->{
            if(!start_time.equals("")){
                binding.inputStartTime.setError(null);
            }
        });
        view_model.start_time_error.observe(this, start_time_error->{
            binding.inputStartTime.setError(start_time_error);
        });

        view_model.price.observe(this, price->{
            if(!price.equals("")){
                binding.inputPrice.setError(null);
            }
        });
        view_model.price_error.observe(this, price_error->{
            binding.inputPrice.setError(price_error);
        });

        view_model.error_call_server.observe(this, this::ShowErrorDialog);

        view_model.is_success.observe(this, is_success -> {
            if(is_success){
                UpdateSuccess();
            }
        });
    }

    @Override
    protected void InitCommon() {
        trip_management = (TripManagement) getIntent().getSerializableExtra(FieldName.TRIP);
        assert trip_management != null;
        view_model.trip_id = trip_management.trip_id;
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

    private void UpdateSuccess(){
        trip_management.trip_name = view_model.trip_name.getValue();
        trip_management.start_date = view_model.start_date.getValue();
        trip_management.start_time = view_model.start_time.getValue();
        trip_management.price = Float.parseFloat(view_model.price.getValue());
        trip_management.description = view_model.description.getValue();
        EventBus.getDefault().post(new UpdateTripInformationEvent(trip_management));
        String message = getString(R.string.successfully_updated);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        finish();
    }
}