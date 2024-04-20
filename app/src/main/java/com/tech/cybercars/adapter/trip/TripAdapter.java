package com.tech.cybercars.adapter.trip;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tech.cybercars.R;
import com.tech.cybercars.adapter.notification.NotificationViewHolder;
import com.tech.cybercars.data.models.trip.Trip;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripViewHolder> {
    private List<Trip> trip_list;

    public TripAdapter(List<Trip> trip_list) {
        this.trip_list = trip_list;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return trip_list.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void UpdateData(List<Trip> trip_list){
        this.trip_list = trip_list;
        this.notifyDataSetChanged();
    }
}
