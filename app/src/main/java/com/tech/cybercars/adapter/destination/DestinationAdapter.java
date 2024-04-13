package com.tech.cybercars.adapter.destination;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tech.cybercars.R;
import com.tech.cybercars.data.models.trip.Destination;
import com.tech.cybercars.utils.DateUtil;
import com.tech.cybercars.utils.Helper;

import java.util.List;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapterViewHolder> {
    private List<Destination> destination_list;
    public DestinationAdapter(List<Destination> road_list){
        this.destination_list = road_list;
    }
    @NonNull
    @Override
    public DestinationAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_destinations, parent, false);
        return new DestinationAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DestinationAdapterViewHolder holder, int position) {
        Destination destination = destination_list.get(position);
        String time = DateUtil.ConvertSecondToHour(destination.time);
        holder.txt_time_road.setText(time);
        String distance = Helper.ConvertMeterToKiloMeterString(destination.distance) + " Km";
        holder.txt_distance_road.setText(distance);
        holder.txt_address.setText(destination.address);
    }

    @Override
    public int getItemCount() {
        return destination_list.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void UpdateAdapter(List<Destination> destination_list){
        this.destination_list = destination_list;
        this.notifyDataSetChanged();
    }

    public interface DestinationListener{
        void OnClicked(Destination destination);
    }
}
