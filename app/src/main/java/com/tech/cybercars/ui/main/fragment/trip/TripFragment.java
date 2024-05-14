package com.tech.cybercars.ui.main.fragment.trip;


import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayoutMediator;
import com.tech.cybercars.R;
import com.tech.cybercars.adapter.paper.TripManagementPageAdapter;
import com.tech.cybercars.databinding.FragmentTripBinding;
import com.tech.cybercars.services.eventbus.ActionEvent;
import com.tech.cybercars.ui.base.BaseFragment;
import com.tech.cybercars.ui.component.dialog.NotificationDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


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
        EventBus.getDefault().register(this);
    }

    @Override
    protected void InitView() {
        TripManagementPageAdapter trip_management_adapter = new TripManagementPageAdapter(getChildFragmentManager(), getLifecycle());
        binding.paperTrip.setAdapter(trip_management_adapter);
        binding.paperTrip.setUserInputEnabled(false);
        String[] tab_name = new String[]{
                getString(R.string.joining_trip),
                getString(R.string.shared_trip)};
        new TabLayoutMediator(binding.tabTrip, binding.paperTrip, (tab, position) -> {
            tab.setText(tab_name[position]);
        }).attach();

        binding.swipeRefresh.setColorSchemeColors(requireContext().getColor(R.color.orange));
        binding.swipeRefresh.setOnRefreshListener(() -> {
            view_model.HandleGetTripList();
        });
    }

    @Override
    protected void InitObserve() {
        view_model.is_loading.observe(this, is_loading->{
            if(is_loading){
                binding.skeletonLoading.startShimmerAnimation();
            } else {
                binding.swipeRefresh.setRefreshing(false);
                binding.skeletonLoading.stopShimmerAnimation();
            }
        });

        view_model.error_call_server.observe(this, this::ShowErrorCallServer);
    }

    @Override
    protected void InitCommon() {
        view_model.HandleGetTripList();
    }

    private ActivityResultLauncher<Intent> trip_detail_launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

            }
    );

    private void ShowErrorCallServer(String error_call_server) {
        NotificationDialog.Builder(requireContext())
                .SetIcon(R.drawable.ic_error)
                .SetTitle(getResources().getString(R.string.something_went_wrong))
                .SetSubtitle(error_call_server)
                .SetTextMainButton(getResources().getString(R.string.close))
                .SetOnMainButtonClicked(Dialog::dismiss).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void UpdatePassengerRequestEvent(ActionEvent action_event) {
        if(!action_event.action.equals(ActionEvent.PASSENGER_REQUEST)){
            return;
        }
        view_model.HandleGetTripList();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ReFreshTripListEvent(ActionEvent action_event){
        if(action_event.action.equals(ActionEvent.REFRESH_TRIP_LIST)){
            view_model.HandleGetTripList();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}