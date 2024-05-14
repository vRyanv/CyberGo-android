package com.tech.cybercars.ui.main.fragment.trip.trip_detail.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tech.cybercars.R;
import com.tech.cybercars.adapter.destination.DestinationAdapter;
import com.tech.cybercars.constant.DestinationType;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.TripStatus;
import com.tech.cybercars.data.models.TripManagement;
import com.tech.cybercars.data.models.trip.Destination;
import com.tech.cybercars.databinding.FragmentLocationTripDetailBinding;
import com.tech.cybercars.services.eventbus.UpdateTripLocationEvent;
import com.tech.cybercars.ui.base.BaseFragment;
import com.tech.cybercars.ui.main.fragment.trip.edit_trip.map_edit_location.MapEditLocationActivity;
import com.tech.cybercars.ui.main.fragment.trip.trip_detail.TripDetailViewModel;
import com.tech.cybercars.utils.DateUtil;
import com.tech.cybercars.utils.Helper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class LocationTripDetailFragment extends BaseFragment<FragmentLocationTripDetailBinding, TripDetailViewModel> {
    private DestinationAdapter destination_adapter;

    @NonNull
    @Override
    protected TripDetailViewModel InitViewModel() {
        return new ViewModelProvider(requireActivity()).get(TripDetailViewModel.class);
    }

    @Override
    protected FragmentLocationTripDetailBinding InitBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_location_trip_detail, container, false);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void InitView() {
        destination_adapter = new DestinationAdapter(new ArrayList<>());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.rcvDestinations.setLayoutManager(layoutManager);
        binding.rcvDestinations.setAdapter(destination_adapter);
    }

    @Override
    protected void InitObserve() {
        view_model.trip_management.observe(requireActivity(), this::BindDataToUI);
    }

    @Override
    protected void InitCommon() {

    }

    private void BindDataToUI(TripManagement trip_management) {
        if (trip_management == null) {
            return;
        }
        binding.txtOriginAddress.setText(trip_management.origin_address);
        if (trip_management.destination_type.equals(DestinationType.SINGLE)) {
            binding.wrapperTime.setVisibility(View.GONE);
            binding.wrapperDistance.setVisibility(View.GONE);
        }

        destination_adapter.UpdateAdapter(trip_management.destinations);
        double distance = 0;
        double time = 0;
        for (Destination destination : trip_management.destinations) {
            distance += destination.distance;
            time += destination.time;
        }
        String total_distance = getString(R.string.total_distance) + ": " + Helper.ConvertMeterToKiloMeterString(distance) + " Km";
        binding.txtTotalDistance.setText(total_distance);

        String total_time = getString(R.string.total_time) + ": " + DateUtil.ConvertSecondToHour(time);
        binding.txtTotalTime.setText(total_time);

        if (!trip_management.trip_status.equals(TripStatus.FINISH)) {
            binding.btnUpdateTripLocation.setVisibility(View.VISIBLE);
            binding.btnUpdateTripLocation.setOnClickListener(view -> {

                Intent map_edit_location_intent = new Intent(requireContext(), MapEditLocationActivity.class);
                map_edit_location_intent.putExtra(FieldName.TRIP, trip_management);
                startActivity(map_edit_location_intent);
            });
        }
    }

    @Subscribe
    public void LocationUpdated(UpdateTripLocationEvent update_trip_location_event) {
        String origin_address = update_trip_location_event.trip_management.origin_address;
        binding.txtOriginAddress.setText(origin_address);
        List<Destination> destinations =  update_trip_location_event.trip_management.destinations;
        destination_adapter.UpdateAdapter(destinations);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}