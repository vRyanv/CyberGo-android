package com.tech.cybercars.ui.main.fragment.go.add_share_trip_information.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tech.cybercars.R;
import com.tech.cybercars.databinding.FragmentLocationTabBinding;

public class LocationTabFragment extends Fragment {
    FragmentLocationTabBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_location_tab, container, false);
        return binding.getRoot();
    }
}