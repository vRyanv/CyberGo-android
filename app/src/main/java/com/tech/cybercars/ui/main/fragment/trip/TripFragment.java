package com.tech.cybercars.ui.main.fragment.trip;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayoutMediator;
import com.tech.cybercars.R;
import com.tech.cybercars.adapter.trip.TripAdapter;
import com.tech.cybercars.adapter.paper.TripFoundDetailAdapter;
import com.tech.cybercars.databinding.FragmentTripBinding;
import com.tech.cybercars.ui.base.BaseFragment;

import java.util.ArrayList;


public class TripFragment extends BaseFragment<FragmentTripBinding, TripViewModel> {
    private TripAdapter my_trip_adapter;
    private TripAdapter trip_join_adapter;
    @NonNull
    @Override
    protected TripViewModel InitViewModel() {
        return new ViewModelProvider(this).get(TripViewModel.class);
    }

    @Override
    protected FragmentTripBinding InitBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_trip, container, false);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {
        trip_join_adapter = new TripAdapter(requireContext(), new ArrayList<>());
        trip_join_adapter.SetOnTripClicked(trip_user_vehicle -> {
            startActivity(new Intent(requireContext(), TripDetailActivity.class));
        });

        my_trip_adapter = new TripAdapter(requireContext(), new ArrayList<>());
        my_trip_adapter.SetOnTripClicked(trip_user_vehicle -> {
            trip_detail_launcher.launch(new Intent(requireContext(), TripDetailActivity.class));
        });


        TripFoundDetailAdapter trip_found_detail = new TripFoundDetailAdapter(requireActivity().getSupportFragmentManager(), this.getLifecycle());
        binding.paperTripFoundDetail.setAdapter(trip_found_detail);
        binding.paperTripFoundDetail.setUserInputEnabled(true);
        String[] tab_name = new String[]{
                getString(R.string.information),
                getString(R.string.location),
                getString(R.string.member)};
        new TabLayoutMediator(binding.tabTripFoundDetail, binding.paperTripFoundDetail, (tab, position) -> {
            tab.setText(tab_name[position]);
        }).attach();
    }

    @Override
    protected void InitObserve() {

    }

    @Override
    protected void InitCommon() {

    }

    private ActivityResultLauncher<Intent> trip_detail_launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

            }
    );
}