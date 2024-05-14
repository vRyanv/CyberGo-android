package com.tech.cybercars.ui.main.fragment.trip.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.tech.cybercars.adapter.trip.TripAdapter;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.TripStatus;
import com.tech.cybercars.data.models.TripManagement;
import com.tech.cybercars.databinding.FragmentJoinedTripBinding;
import com.tech.cybercars.services.eventbus.TripFinishEvent;
import com.tech.cybercars.ui.main.fragment.trip.TripViewModel;
import com.tech.cybercars.ui.main.fragment.trip.trip_detail.TripDetailActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        EventBus.getDefault().register(this);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnTripFinishEvent(TripFinishEvent trip_finish_event) {
        String trip_id = trip_finish_event.trip_id;
        String status = trip_finish_event.status;
        ExecutorService executor_service = Executors.newSingleThreadExecutor();
        executor_service.execute(()-> {
            for (TripManagement trip_management: view_model.joined_trip_list.getValue()) {
                if(trip_management.trip_id.equals(trip_id)){
                    int index_of_trip =  view_model.joined_trip_list.getValue().indexOf(trip_management);
                    trip_management.trip_status = status;
                    Handler main_handler = new Handler(Looper.getMainLooper());
                    main_handler.post(()->{
                        trip_join_adapter.UpdateData(trip_management, index_of_trip);
                    });
                    break;
                }
            }
        });
        executor_service.shutdown();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}