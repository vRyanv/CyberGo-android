package com.tech.cybercars.ui.main.fragment.go.share_trip.add_share_trip_information.fragment;

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
import com.tech.cybercars.data.models.trip.Destination;
import com.tech.cybercars.data.models.trip.Trip;
import com.tech.cybercars.databinding.FragmentLocationTabBinding;
import com.tech.cybercars.ui.base.BaseFragment;
import com.tech.cybercars.ui.main.fragment.go.share_trip.add_share_trip_information.AddShareTripInformationViewModel;
import com.tech.cybercars.utils.DateUtil;
import com.tech.cybercars.utils.Helper;

import java.util.ArrayList;
import java.util.List;

public class LocationTabFragment extends BaseFragment<FragmentLocationTabBinding, AddShareTripInformationViewModel> {
    private DestinationAdapter destination_adapter;

    @NonNull
    @Override
    protected AddShareTripInformationViewModel InitViewModel() {
        return new ViewModelProvider(requireActivity()).get(AddShareTripInformationViewModel.class);
    }

    @Override
    protected FragmentLocationTabBinding InitBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_location_tab, container, false);
        binding.setViewModel(view_model);
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

        binding.btnNextToTripInfoFm.setOnClickListener(view -> {
            view_model.current_page.setValue(1);
        });
    }

    @Override
    protected void InitObserve() {
        view_model.trip.observe(requireActivity(), this::BindTripDataToUI);
        view_model.destination_list.observe(requireActivity(), this::BindDestinationDataToUI);
    }

    @Override
    protected void InitCommon() {

    }

    private void BindTripDataToUI(Trip trip) {
        binding.txtOriginAddress.setText(trip.origin_address);
        if(trip.destination_type.equals(DestinationType.SINGLE)){
            binding.wrapperTime.setVisibility(View.GONE);
            binding.wrapperDistance.setVisibility(View.GONE);
        }
    }
    private void BindDestinationDataToUI(ArrayList<Destination> destination_list) {
        destination_adapter.UpdateAdapter(destination_list);
        double distance = 0;
        double time = 0;
        for (Destination destination : destination_list) {
            distance += destination.distance;
            time += destination.time;
        }
        String total_distance = getString(R.string.total_distance) + ": " + Helper.ConvertMeterToKiloMeterString(distance) + " Km";
        binding.txtTotalDistance.setText(total_distance);

        String total_time = getString(R.string.total_time) + ": " + DateUtil.ConvertSecondToHour(time);
        binding.txtTotalTime.setText(total_time);
    }
}