package com.tech.cybercars.ui.main.fragment.trip.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.tech.cybercars.R;
import com.tech.cybercars.adapter.trip.TripAdapter;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.Tag;
import com.tech.cybercars.constant.TripStatus;
import com.tech.cybercars.data.models.TripManagement;
import com.tech.cybercars.databinding.FragmentJoinedTripBinding;
import com.tech.cybercars.databinding.FragmentSharedTripBinding;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.base.BaseFragment;
import com.tech.cybercars.ui.main.fragment.trip.TripViewModel;
import com.tech.cybercars.ui.main.fragment.trip.trip_detail.TripDetailActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SharedTripFragment extends Fragment {
    private TripAdapter shared_trip_adapter;
    private TripViewModel view_model;
    private FragmentSharedTripBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        assert getParentFragment() != null;
        view_model = new ViewModelProvider(getParentFragment()).get(TripViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shared_trip, container, false);

        InitView();
        InitObserve();
        return binding.getRoot();
    }

    protected void InitView() {
        shared_trip_adapter = new TripAdapter(requireContext(), new ArrayList<>());
        shared_trip_adapter.SetOnTripClicked(trip_management -> {
            Intent trip_detail_intent = new Intent(requireContext(), TripDetailActivity.class);
            trip_detail_intent.putExtra(FieldName.TRIP, trip_management);
            startActivity(trip_detail_intent);
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.rcvSharedTrip.setLayoutManager(layoutManager);
        binding.rcvSharedTrip.setAdapter(shared_trip_adapter);
    }

    protected void InitObserve() {
        view_model.shared_trip_list.observe(getViewLifecycleOwner(), this::BindDataToUI);

        view_model.is_loading.observe(getViewLifecycleOwner(), is_loading->{

        });
    }

    private void BindDataToUI(List<TripManagement> shared_trip_list) {
        int opening_quantity = 0;
        int closed_quantity = 0;
        int finish_quantity = 0;
        for (TripManagement trip :shared_trip_list) {
            switch (trip.trip_status){
                case TripStatus.OPENING:
                    opening_quantity++;
                    break;
                case TripStatus.CLOSED:
                    closed_quantity++;
                    break;
                default:
                    finish_quantity++;
                    break;
            }
        }
        binding.txtOpeningSharedTripQuantity.setText(String.valueOf(opening_quantity));
        binding.txtClosedSharedTripQuantity.setText(String.valueOf(closed_quantity));
        binding.txtFinishSharedTripQuantity.setText(String.valueOf(finish_quantity));
        shared_trip_adapter.UpdateData(shared_trip_list);
    }
}