package com.tech.cybercars.adapter.vehicles;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.VehicleStatus;
import com.tech.cybercars.constant.VehicleType;
import com.tech.cybercars.data.models.Notification;
import com.tech.cybercars.data.models.Vehicle;

import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleViewHolder> {
    private List<Vehicle> vehicle_list;
    private Context context;
    private VehicleListener vehicle_listener;
    public VehicleAdapter(Context context, List<Vehicle> vehicle_list) {
        this.vehicle_list = vehicle_list;
        this.context = context;
    }
    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehicle, parent, false);
        return new VehicleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        Vehicle vehicle = vehicle_list.get(position);
        switch (vehicle.vehicle_type) {
            case VehicleType.CAR:
                holder.img_vehicle_type.setImageResource(R.drawable.ic_car);
                break;
            case VehicleType.MOTO:
                holder.img_vehicle_type.setImageResource(R.drawable.ic_motorcycle);
                break;
            case VehicleType.TRUCK:
                holder.img_vehicle_type.setImageResource(R.drawable.ic_truck);
                break;
        }
        holder.txt_vehicle_name.setText(vehicle.vehicle_name);
        holder.txt_status.setText(vehicle.status);
        switch (vehicle.status){
            case VehicleStatus.ACCEPTED:
                holder.txt_status.setBackgroundColor(context.getColor(R.color.green));
                break;
            case VehicleStatus.REFUSED:
                holder.txt_status.setBackgroundColor(context.getColor(R.color.dark_red));
                break;
            default:
                holder.txt_status.setBackgroundColor(context.getColor(R.color.lavender));
                break;
        }

        holder.itemView.setOnClickListener(v -> {
            vehicle_listener.OnClicked(vehicle);
        });
    }

    @Override
    public int getItemCount() {
        return vehicle_list.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void UpdateData(List<Vehicle> vehicle_list) {
        this.vehicle_list = vehicle_list;
        this.notifyDataSetChanged();
    }

    public void SetOnClickListener(VehicleListener vehicle_listener){
        this.vehicle_listener = vehicle_listener;
    }

    public interface VehicleListener{
        public void OnClicked(Vehicle vehicle);
    }
}
