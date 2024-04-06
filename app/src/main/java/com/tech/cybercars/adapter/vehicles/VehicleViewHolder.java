package com.tech.cybercars.adapter.vehicles;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tech.cybercars.R;

public class VehicleViewHolder extends RecyclerView.ViewHolder {
    public ImageView img_vehicle_type;
    public TextView txt_vehicle_name;
    public TextView txt_status;
    public VehicleViewHolder(@NonNull View itemView) {
        super(itemView);
        img_vehicle_type = itemView.findViewById(R.id.img_vehicle_type);
        txt_vehicle_name = itemView.findViewById(R.id.txt_vehicle_name);
        txt_status = itemView.findViewById(R.id.txt_status);
    }
}
