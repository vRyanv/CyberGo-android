package com.tech.cybercars.ui.main.fragment.go.share_trip.add_share_trip_information.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tech.cybercars.R;
import com.tech.cybercars.adapter.road_sections.RoadSectionsAdapter;
import com.tech.cybercars.constant.DestinationType;
import com.tech.cybercars.data.sub_models.Road;
import com.tech.cybercars.data.sub_models.TripSharing;
import com.tech.cybercars.databinding.FragmentLocationTabBinding;
import com.tech.cybercars.ui.base.BaseFragment;
import com.tech.cybercars.ui.main.fragment.go.share_trip.add_share_trip_information.AddShareTripInformationViewModel;
import com.tech.cybercars.utils.DateUtil;
import com.tech.cybercars.utils.Helper;

import java.util.ArrayList;
import java.util.List;

public class LocationTabFragment extends BaseFragment<FragmentLocationTabBinding, AddShareTripInformationViewModel> {
    private RoadSectionsAdapter road_section_adapter;

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
        road_section_adapter = new RoadSectionsAdapter(new ArrayList<>());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.rcvRoadSection.setLayoutManager(layoutManager);
        binding.rcvRoadSection.setAdapter(road_section_adapter);

        binding.btnNextToTripInfoFm.setOnClickListener(view -> {
            view_model.current_page.setValue(1);
        });
    }

    @Override
    protected void InitObserve() {
        view_model.trip_sharing.observe(requireActivity(), this::BindDataToUI);
        view_model.destination_type.observe(requireActivity(), destination_type -> {
            if(destination_type.equals(DestinationType.SINGLE)){
                binding.wrapperDistance.setVisibility(View.GONE);
                binding.wrapperTime.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void InitCommon() {

    }

    private void BindDataToUI(TripSharing trip_sharing) {
        List<Road> road_list = trip_sharing.road_sections;
        road_section_adapter.UpdateAdapter(road_list);
        double distance = 0;
        double time = 0;
        for (Road road : road_list) {
            distance += road.distance;
            time += road.time;
        }
        String total_distance = getString(R.string.total_distance) + ": " + Helper.ConvertMeterToKiloMeterString(distance) + " Km";
        binding.txtTotalDistance.setText(total_distance);

        String total_time = getString(R.string.total_time) + ": " + DateUtil.ConvertSecondToHour(time);
        binding.txtTotalTime.setText(total_time);

        binding.txtOriginAddress.setText(trip_sharing.origin_address);
    }
}