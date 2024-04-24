package com.tech.cybercars.ui.main.fragment.go.find_trip.trip_found_detail.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tech.cybercars.R;
import com.tech.cybercars.adapter.member.MemberAdapter;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.data.models.TripFound;
import com.tech.cybercars.databinding.FragmentMemberTripFoundDetailBinding;
import com.tech.cybercars.ui.base.BaseFragment;
import com.tech.cybercars.ui.main.fragment.account.profile.ProfileActivity;
import com.tech.cybercars.ui.main.fragment.go.find_trip.trip_found_detail.TripFoundDetailViewModel;
import com.tech.cybercars.ui.main.user_profile.UserProfileActivity;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import java.util.ArrayList;

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
        member_adapter.SetMemberClicked(this::OpenUserProfile);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.rcvMember.setLayoutManager(layoutManager);
        binding.rcvMember.setAdapter(member_adapter);
    }

    private void OpenUserProfile(String user_id) {
        String current_user_id = SharedPreferencesUtil.GetString(requireContext(), FieldName.USER_ID);
        if(current_user_id.equals(user_id)){
            startActivity(new Intent(requireContext(), ProfileActivity.class));
        } else {
            Intent user_profile_intent = new Intent(requireContext(), UserProfileActivity.class);
            user_profile_intent.putExtra(FieldName.USER_ID, user_id);
            startActivity(user_profile_intent);
        }
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