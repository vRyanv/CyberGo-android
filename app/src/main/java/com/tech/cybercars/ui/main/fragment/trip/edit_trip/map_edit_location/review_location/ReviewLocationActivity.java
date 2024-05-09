package com.tech.cybercars.ui.main.fragment.trip.edit_trip.map_edit_location.review_location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.installations.internal.FidListener;
import com.tech.cybercars.R;
import com.tech.cybercars.constant.ActivityResult;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.data.models.TripManagement;
import com.tech.cybercars.data.models.trip.Destination;
import com.tech.cybercars.databinding.ActivityReviewLocationBinding;
import com.tech.cybercars.services.eventbus.UpdateTripLocationEvent;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.main.fragment.trip.edit_trip.map_edit_location.MapEditLocationViewModel;
import com.tech.cybercars.utils.DateUtil;
import com.tech.cybercars.utils.Helper;

import org.greenrobot.eventbus.EventBus;

public class ReviewLocationActivity extends BaseActivity<ActivityReviewLocationBinding, ReviewLocationViewModel> {

    @NonNull
    @Override
    protected ReviewLocationViewModel InitViewModel() {
        return new ViewModelProvider(this).get(ReviewLocationViewModel.class);
    }

    @Override
    protected ActivityReviewLocationBinding InitBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_review_location);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {
        binding.btnOutScreen.setOnClickListener(view -> {
            OnBackPress();
        });

        binding.btnUpdate.setOnClickListener(view -> {
            view_model.HandleUpdateLocation();
        });
    }

    @Override
    protected void InitObserve() {
        view_model.trip_management.observe(this, this::BindDataToUI);

        view_model.is_success.observe(this, is_success-> {
            if(is_success){
                EventBus.getDefault().post(new UpdateTripLocationEvent(view_model.trip_management.getValue()));
                setResult(ActivityResult.UPDATED);
                String mess = getString(R.string.successfully_updated);
                Toast.makeText(this, mess, Toast.LENGTH_SHORT).show();
                finish();
            }
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

    private void BindDataToUI(TripManagement trip_management) {
        Destination destination = trip_management.destinations.get(0);
        String time = DateUtil.ConvertSecondToHour(destination.time);
        binding.txtTimeRoad.setText(time);

        double distance_meter = destination.distance;
        if (distance_meter < 1000) {
            binding.txtDistanceRoad.setText(Math.round(distance_meter) + "m");
        } else {
            String rounded_distance = Helper.ConvertMeterToKiloMeterString(distance_meter);
            binding.txtDistanceRoad.setText(rounded_distance + " Km");
        }

        binding.txtOriginAddress.setText(trip_management.origin_address);
        binding.txtDestinationAddress.setText(destination.address);
    }
}