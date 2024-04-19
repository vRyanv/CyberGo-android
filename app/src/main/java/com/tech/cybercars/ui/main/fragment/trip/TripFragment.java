package com.tech.cybercars.ui.main.fragment.trip;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.tech.cybercars.R;
import com.tech.cybercars.databinding.FragmentTripBinding;
import com.tech.cybercars.ui.base.BaseFragment;


public class TripFragment extends BaseFragment<FragmentTripBinding, TripViewModel> {

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

    }

    @Override
    protected void InitObserve() {

    }

    @Override
    protected void InitCommon() {

    }
}