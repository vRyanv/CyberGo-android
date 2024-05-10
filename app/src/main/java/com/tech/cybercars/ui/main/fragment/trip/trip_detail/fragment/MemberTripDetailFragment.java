package com.tech.cybercars.ui.main.fragment.trip.trip_detail.fragment;

import android.app.Dialog;
import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tech.cybercars.R;
import com.tech.cybercars.adapter.member_trip_detail.MemberTripDetailAdapter;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.MemberStatus;
import com.tech.cybercars.data.models.TripManagement;
import com.tech.cybercars.databinding.FragmentMemberTripDetailBinding;
import com.tech.cybercars.ui.base.BaseFragment;
import com.tech.cybercars.ui.component.dialog.DeleteDialog;
import com.tech.cybercars.ui.main.feedback.FeedbackActivity;
import com.tech.cybercars.ui.main.fragment.account.profile.ProfileActivity;
import com.tech.cybercars.ui.main.fragment.trip.trip_detail.TripDetailViewModel;
import com.tech.cybercars.ui.main.fragment.trip.trip_detail.view_member_location.ViewLocationMemberActivity;
import com.tech.cybercars.ui.main.user_profile.UserProfileActivity;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import java.util.ArrayList;

public class MemberTripDetailFragment extends BaseFragment<FragmentMemberTripDetailBinding, TripDetailViewModel> {
    private MemberTripDetailAdapter member_adapter;
    @NonNull
    @Override
    protected TripDetailViewModel InitViewModel() {
        return new ViewModelProvider(requireActivity()).get(TripDetailViewModel.class);
    }

    @Override
    protected FragmentMemberTripDetailBinding InitBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_member_trip_detail, container, false);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {
        member_adapter = new MemberTripDetailAdapter(requireContext(), new ArrayList<>());
        member_adapter.SetMemberClicked(this::OpenUserProfile);
        member_adapter.SetMakeRatingMemberClicked(this::MakeRatingMember);
        member_adapter.SetAcceptMemberClicked(this::AcceptMember);
        member_adapter.SetDeniedMemberClicked(this::DeniedMember);
        member_adapter.SetViewLocationClicked(this::OpenViewLocation);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.rcvMember.setLayoutManager(layoutManager);
        binding.rcvMember.setAdapter(member_adapter);
    }

    @Override
    protected void InitObserve() {
        view_model.trip_management.observe(this, this::BindDataToUI);
    }

    @Override
    protected void InitCommon() {

    }

    private void BindDataToUI(TripManagement trip_management) {
        if(trip_management == null){
            return;
        }
        binding.txtMemberCount.setText(String.valueOf(trip_management.members.size()));
        String current_user_id = SharedPreferencesUtil.GetString(requireContext(), FieldName.USER_ID);
        member_adapter.SetTripCondition(trip_management.trip_status, trip_management.trip_owner.user_id, current_user_id);
        member_adapter.UpdateData(trip_management.members);
    }

    private final ActivityResultLauncher<Intent> make_rating_launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

            }
    );

    private void MakeRatingMember(TripManagement.Member member) {
        Intent feedback_intent = new Intent(requireContext(), FeedbackActivity.class);
        feedback_intent.putExtra(FieldName.AVATAR, member.avatar);
        feedback_intent.putExtra(FieldName.FULL_NAME, member.full_name);
        make_rating_launcher.launch(feedback_intent);
    }

    private void OpenUserProfile(TripManagement.Member member) {
        String current_user_id = SharedPreferencesUtil.GetString(requireContext(), FieldName.USER_ID);
        if(current_user_id.equals(member.user_id)){
            startActivity(new Intent(requireContext(), ProfileActivity.class));
        } else {
            Intent user_profile_intent = new Intent(requireContext(), UserProfileActivity.class);
            user_profile_intent.putExtra(FieldName.USER_ID, member.user_id);
            startActivity(user_profile_intent);
        }
    }

    private void AcceptMember(TripManagement.Member member){
        view_model.HandleMemberRequestJoin(member.member_id, member.user_id, MemberStatus.JOINED);
    }

    private void DeniedMember(TripManagement.Member member){
        new DeleteDialog(requireContext(), getString(R.string.refuse_this_passenger))
                .SetTextDeleteButton(getString(R.string.refuse))
                .SetButtonSelectedCallback(new DeleteDialog.SelectButtonCallback() {
                    @Override
                    public void OnDelete(Dialog dialog) {
                        view_model.HandleMemberRequestJoin(member.member_id, member.user_id, MemberStatus.DENIED);
                        dialog.dismiss();
                    }

                    @Override
                    public void OnCancel(Dialog dialog) {
                        dialog.dismiss();
                    }
                }).show();

    }

    private void OpenViewLocation(TripManagement.Member member){
        Intent view_location_intent = new Intent(requireContext(), ViewLocationMemberActivity.class);
        view_location_intent.putExtra(FieldName.TRIP, view_model.trip_management.getValue());
        view_location_intent.putExtra(FieldName.MEMBER, member);
        startActivity(view_location_intent);
    }
}