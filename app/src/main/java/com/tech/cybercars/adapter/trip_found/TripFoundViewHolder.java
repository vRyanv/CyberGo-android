package com.tech.cybercars.adapter.trip_found;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tech.cybercars.R;
import com.tech.cybercars.ui.component.AvatarListView;
import com.willy.ratingbar.BaseRatingBar;

public class TripFoundViewHolder extends RecyclerView.ViewHolder {
    public ImageView img_avatar;
    public BaseRatingBar rating_bar;
    public ImageView img_vehicle_type;
    public TextView txt_trip_name;
    public TextView txt_start_date;
    public TextView txt_start_time;
    public TextView txt_origin_address;
    public TextView txt_destination_address;
    public TextView txt_price;
    public TextView txt_trip_match_route_ratio;
    public AvatarListView view_avatar_list;
    public ImageButton btn_see_overview_trip;
    public TripFoundViewHolder(@NonNull View itemView) {
        super(itemView);
        img_avatar = itemView.findViewById(R.id.img_avatar);
        rating_bar = itemView.findViewById(R.id.rating_bar);
        img_vehicle_type = itemView.findViewById(R.id.img_vehicle_type);
        txt_trip_name = itemView.findViewById(R.id.txt_trip_name);
        txt_start_date = itemView.findViewById(R.id.txt_start_date);
        txt_start_time = itemView.findViewById(R.id.txt_start_time);
        txt_origin_address = itemView.findViewById(R.id.txt_origin_address);
        txt_destination_address = itemView.findViewById(R.id.txt_destination_address);
        txt_price = itemView.findViewById(R.id.txt_price);
        txt_trip_match_route_ratio = itemView.findViewById(R.id.txt_trip_match_route_ratio);
        view_avatar_list = itemView.findViewById(R.id.view_avatar_list);
        btn_see_overview_trip = itemView.findViewById(R.id.btn_see_overview_trip);
    }
}
