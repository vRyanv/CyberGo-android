package com.tech.cybercars.ui.main.fragment.go.find_trip.trip_found_detail.fragment;

import android.content.Intent;
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
import com.tech.cybercars.adapter.destination.DestinationAdapter;
import com.tech.cybercars.adapter.member.MemberAdapter;
import com.tech.cybercars.data.models.TripFound;
import com.tech.cybercars.databinding.FragmentMemberTripFoundDetailBinding;
import com.tech.cybercars.ui.base.BaseFragment;
import com.tech.cybercars.ui.main.fragment.go.find_trip.trip_found_detail.TripFoundDetailViewModel;
import com.tech.cybercars.ui.main.user_profile.UserProfileActivity;

import java.util.ArrayList;
import java.util.List;

public class MemberTripFoundDetailFragment extends BaseFragment<FragmentMemberTripFoundDetailBinding, TripFoundDetailViewModel> {
    private MemberAdapter member_adapter;
    @NonNull
    @Override
    protected TripFoundDetailViewModel InitViewModel() {
        return new ViewModelProvider(requireActivity()).get(TripFoundDetailViewModel.class);
    }

    @Override
    protected FragmentMemberTripFoundDetailBinding InitBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_member_trip_found_detail, container, false);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {
        member_adapter = new MemberAdapter(requireContext(), new ArrayList<>());
        member_adapter.SetMemberClicked(user_id -> {
            startActivity(new Intent(requireContext(), UserProfileActivity.class));
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.rcvMember.setLayoutManager(layoutManager);
        binding.rcvMember.setAdapter(member_adapter);
    }

    @Override
    protected void InitObserve() {
        view_model.trip_found.observe(this, this::BindDataToUI);
    }

    @Override
    protected void InitCommon() {

    }

    private void BindDataToUI(TripFound trip_found){
        binding.txtMemberCount.setText(String.valueOf(trip_found.member_list.size()));
        member_adapter.UpdateData(trip_found.member_list);
    }
}