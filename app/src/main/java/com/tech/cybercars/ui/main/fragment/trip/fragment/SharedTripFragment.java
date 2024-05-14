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
import com.tech.cybercars.databinding.FragmentSharedTripBinding;
import com.tech.cybercars.services.eventbus.UpdateTripInformationEvent;
import com.tech.cybercars.services.eventbus.UpdateTripLocationEvent;
import com.tech.cybercars.ui.main.fragment.trip.TripViewModel;
import com.tech.cybercars.ui.main.fragment.trip.trip_detail.TripDetailActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

        EventBus.getDefault().register(this);
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

        view_model.is_loading.observe(getViewLifecycleOwner(), is_loading -> {

        });
    }

    private void BindDataToUI(List<TripManagement> shared_trip_list) {
        if (shared_trip_list == null) {
            return;
        }
        int opening_quantity = 0;
        int closed_quantity = 0;
        int finish_quantity = 0;
        for (TripManagement trip : shared_trip_list) {
            switch (trip.trip_status) {
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

    @Subscribe
    public void LocationUpdated(UpdateTripLocationEvent update_trip_location_event) {
        ExecutorService executor_service = Executors.newSingleThreadExecutor();
        executor_service.execute(() -> {
            String trip_id = update_trip_location_event.trip_management.trip_id;
            List<TripManagement> trip_management_list = view_model.shared_trip_list.getValue();
            for (int i = 0; i < trip_management_list.size(); i++) {
                if (trip_management_list.get(i).trip_id.equals(trip_id)) {

                    Handler main_handler = new Handler(Looper.getMainLooper());
                    int finalI = i;
                    main_handler.post(() -> {
                        shared_trip_adapter.UpdateData(update_trip_location_event.trip_management, finalI);
                    });

                    break;
                }
            }
        });
        executor_service.shutdown();
    }

    @Subscribe
    public void InformationUpdated(UpdateTripInformationEvent update_trip_info_event) {
        ExecutorService executor_service = Executors.newSingleThreadExecutor();
        executor_service.execute(() -> {
            String trip_id = update_trip_info_event.trip_management.trip_id;
            List<TripManagement> trip_management_list = view_model.shared_trip_list.getValue();
            for (int i = 0; i < trip_management_list.size(); i++) {
                if (trip_management_list.get(i).trip_id.equals(trip_id)) {
                    Handler main_handler = new Handler(Looper.getMainLooper());
                    int finalI = i;
                    main_handler.post(() -> {
                        shared_trip_adapter.UpdateData(update_trip_info_event.trip_management, finalI);
                        int closed = 0;
                        int opening = 0;
                        int finish = 0;
                        for (TripManagement trip : trip_management_list) {
                            switch (trip.trip_status) {
                                case TripStatus.OPENING:
                                    opening++;
                                    break;
                                case TripStatus.CLOSED:
                                    closed++;
                                    break;
                                default:
                                    finish++;
                            }
                        }
                        binding.txtFinishSharedTripQuantity.setText(String.valueOf(finish));
                        binding.txtClosedSharedTripQuantity.setText(String.valueOf(closed));
                        binding.txtOpeningSharedTripQuantity.setText(String.valueOf(opening));
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