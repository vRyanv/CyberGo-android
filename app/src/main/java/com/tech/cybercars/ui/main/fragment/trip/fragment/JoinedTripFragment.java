package com.tech.cybercars.ui.main.fragment.trip.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tech.cybercars.R;
import com.tech.cybercars.adapter.trip.TripAdapter;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.TripStatus;
import com.tech.cybercars.data.models.TripManagement;
import com.tech.cybercars.databinding.FragmentJoinedTripBinding;
import com.tech.cybercars.ui.base.BaseFragment;
import com.tech.cybercars.ui.main.MainViewModel;
import com.tech.cybercars.ui.main.fragment.trip.TripViewModel;
import com.tech.cybercars.ui.main.fragment.trip.trip_detail.TripDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class JoinedTripFragment extends Fragment {
    private TripAdapter trip_join_adapter;
    private TripViewModel view_model;
    private FragmentJoinedTripBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view_model = new ViewModelProvider(requireParentFragment()).get(TripViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_joined_trip, container, false);
        InitView();
        InitObserve();
        return binding.getRoot();
    }

    protected void InitView() {
        trip_join_adapter = new TripAdapter(requireContext(), new ArrayList<>());
        trip_join_adapter.SetOnTripClicked(trip_management -> {
            Intent trip_detail_intent = new Intent(requireContext(), TripDetailActivity.class);
            trip_detail_intent.putExtra(FieldName.TRIP, trip_management);
            startActivity(trip_detail_intent);
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.rcvJoinTrip.setLayoutManager(layoutManager);
        binding.rcvJoinTrip.setAdapter(trip_join_adapter);
    }

    protected void InitObserve() {
        view_model.joined_trip_list.observe(getViewLifecycleOwner(), this::BindDataToUI);
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
        binding.txtOpeningJoinTripQuantity.setText(String.valueOf(opening_quantity));
        binding.txtClosedJoinTripQuantity.setText(String.valueOf(closed_quantity));
        binding.txtFinishJoinTripQuantity.setText(String.valueOf(finish_quantity));
        trip_join_adapter.UpdateData(shared_trip_list);
    }
}