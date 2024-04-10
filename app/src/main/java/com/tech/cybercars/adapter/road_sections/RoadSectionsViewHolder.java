package com.tech.cybercars.adapter.road_sections;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tech.cybercars.R;

public class RoadSectionsViewHolder extends RecyclerView.ViewHolder {
    public TextView txt_time_road;
    public TextView txt_distance_road;
    public TextView txt_address;
    public RoadSectionsViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_address = itemView.findViewById(R.id.txt_address);
        txt_time_road = itemView.findViewById(R.id.txt_time_road);
        txt_distance_road = itemView.findViewById(R.id.txt_distance_road);
    }
}
