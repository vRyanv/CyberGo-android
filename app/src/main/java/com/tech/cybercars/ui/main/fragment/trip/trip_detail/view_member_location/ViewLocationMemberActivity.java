package com.tech.cybercars.ui.main.fragment.trip.trip_detail.view_member_location;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.data.models.TripManagement;
import com.tech.cybercars.databinding.ActivityViewLocationMemberBinding;
import com.tech.cybercars.utils.DateUtil;
import com.tech.cybercars.utils.Helper;

public class ViewLocationMemberActivity extends AppCompatActivity {
    ActivityViewLocationMemberBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_location_member);
        InitView();
    }

    private void InitView() {
        TripManagement trip_management = (TripManagement) getIntent().getSerializableExtra(FieldName.TRIP);
        TripManagement.Member member = (TripManagement.Member) getIntent().getSerializableExtra(FieldName.MEMBER);
        assert member != null;
        assert trip_management != null;

        binding.headerPrimary.setText(member.full_name);

        String time = DateUtil.ConvertSecondToHour(member.destination.time);
        binding.txtTimeRoad.setText(time);

        double distance_meter = member.destination.distance;
        if (distance_meter < 1000) {
            binding.txtDistanceRoad.setText(Math.round(distance_meter) + "m");
        } else {
            String rounded_distance = Helper.ConvertMeterToKiloMeterString(distance_meter);
            binding.txtDistanceRoad.setText(rounded_distance + " Km");
        }

        String origin_address = member.origin.address;
        binding.txtOriginAddress.setText(origin_address);

        String destination_address = member.destination.address;
        binding.txtDestinationAddress.setText(destination_address);

        binding.btnOutScreen.setOnClickListener(view -> {
            finish();
        });

        binding.btnOpenViewOnMap.setOnClickListener(view -> {
            StartViewOnMap();
        });
    }

    private void StartViewOnMap() {
        Intent view_on_map_intent = new Intent(this, ViewMemberOnMapActivity.class);
        TripManagement trip_management = (TripManagement) getIntent().getSerializableExtra(FieldName.TRIP);
        TripManagement.Member member = (TripManagement.Member) getIntent().getSerializableExtra(FieldName.MEMBER);
        view_on_map_intent.putExtra(FieldName.TRIP, trip_management);
        view_on_map_intent.putExtra(FieldName.MEMBER, member);
        startActivity(view_on_map_intent);
    }


}