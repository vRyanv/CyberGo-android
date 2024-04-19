package com.tech.cybercars.ui.main.fragment.go.find_trip.trip_found_detail.fragment;

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
import com.tech.cybercars.data.models.TripFound;
import com.tech.cybercars.data.models.trip.Destination;
import com.tech.cybercars.databinding.FragmentLocationTripFoundDetailBinding;
import com.tech.cybercars.ui.base.BaseFragment;
import com.tech.cybercars.ui.main.fragment.go.find_trip.trip_found_detail.TripFoundDetailViewModel;
import com.tech.cybercars.utils.DateUtil;
import com.tech.cybercars.utils.Helper;

import java.util.ArrayList;

public class LocationTripFoundDetailFragment extends BaseFragment<FragmentLocationTripFoundDetailBinding, TripFoundDetailViewModel>{
    private DestinationAdapter destination_adapter;
    @NonNull
    @Override
    protected TripFoundDetailViewModel InitViewModel() {
        return new ViewModelProvider(requireActivity()).get(TripFoundDetailViewModel.class);
    }

    @Override
    protected FragmentLocationTripFoundDetailBinding InitBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_location_trip_found_detail, container, false);
        return binding;
    }

    @Override
    protected void InitFirst() {

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
        view_model.trip_found.observe(requireActivity(), this::BindTripDataToUI);
    }

    @Override
    protected void InitCommon() {

    }

    private void BindTripDataToUI(TripFound trip_found){
        binding.txtOriginAddress.setText(trip_found.origin_address);
        if(trip_found.destination_type.equals(DestinationType.SINGLE)){
            binding.wrapperTime.setVisibility(View.GONE);
            binding.wrapperDistance.setVisibility(View.GONE);
        }

        destination_adapter.UpdateAdapter(trip_found.destination_list);
        double distance = 0;
        double time = 0;
        for (Destination destination : trip_found.destination_list) {
            distance += destination.distance;
            time += destination.time;
        }
        String total_distance = getString(R.string.total_distance) + ": " + Helper.ConvertMeterToKiloMeterString(distance) + " Km";
        binding.txtTotalDistance.setText(total_distance);

        String total_time = getString(R.string.total_time) + ": " + DateUtil.ConvertSecondToHour(time);
        binding.txtTotalTime.setText(total_time);
    }
}