package com.tech.cybercars.adapter.trip;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tech.cybercars.R;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.constant.VehicleType;
import com.tech.cybercars.data.models.Member;
import com.tech.cybercars.data.models.TripAndUserAndVehicle;
import com.tech.cybercars.data.models.TripFound;
import com.tech.cybercars.data.models.User;
import com.tech.cybercars.data.models.trip.Destination;
import com.tech.cybercars.data.models.trip.Trip;
import com.tech.cybercars.utils.Helper;

import java.util.ArrayList;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripViewHolder> {
    private List<TripAndUserAndVehicle> trip_user_vehicle_list;
    private Context context;
    private TripClickedCallback trip_clicked_callback;

    public TripAdapter(Context context, List<TripAndUserAndVehicle> trip_user_vehicle_list) {
        this.trip_user_vehicle_list = trip_user_vehicle_list;
        this.context = context;
    }


    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        TripAndUserAndVehicle trip_user_vehicle = trip_user_vehicle_list.get(position);
        BindData(holder, trip_user_vehicle);
        holder.itemView.setOnClickListener(view -> {
            trip_clicked_callback.OnClicked(trip_user_vehicle);
        });
    }

    @Override
    public int getItemCount() {
        return trip_user_vehicle_list.size();
    }

    private void BindData(TripViewHolder holder, TripAndUserAndVehicle trip_user_vehicle) {
        String avatar_full_path = URL.BASE_URL + URL.AVATAR_RES_PATH + trip_user_vehicle.user.avatar;
        Glide.with(context)
                .load(avatar_full_path)
                .placeholder(R.drawable.loading_placeholder)
                .into(holder.img_avatar);

        holder.rating_bar.setClickable(false);
        holder.rating_bar.setRating(trip_user_vehicle.user.rating);

        holder.txt_trip_name.setText(trip_user_vehicle.trip.name);

        switch (trip_user_vehicle.vehicle.vehicle_type) {
            case VehicleType.CAR:
                holder.img_vehicle_type.setImageResource(R.drawable.ic_car);
                break;
            case VehicleType.MOTO:
                holder.img_vehicle_type.setImageResource(R.drawable.ic_motorcycle);
                break;
            default:
                holder.img_vehicle_type.setImageResource(R.drawable.ic_truck);
                break;
        }

        holder.txt_start_date.setText(trip_user_vehicle.trip.start_date);
        holder.txt_start_time.setText(trip_user_vehicle.trip.start_time);

        if(trip_user_vehicle.trip.origin_city != null){
            holder.txt_origin_address.setText(trip_user_vehicle.trip.origin_city);
        } else if (trip_user_vehicle.trip.origin_state != null) {
            holder.txt_origin_address.setText(trip_user_vehicle.trip.origin_state);
        } else if (trip_user_vehicle.trip.origin_county != null) {
            holder.txt_origin_address.setText(trip_user_vehicle.trip.origin_county);
        } else {
            holder.txt_origin_address.setText(trip_user_vehicle.trip.origin_address);
        }

        Destination destination = trip_user_vehicle.trip.destinations.get(trip_user_vehicle.trip.destinations.size()-1);
        if(destination.city != null){
            holder.txt_destination_address.setText(destination.city);
        } else if (destination.state != null) {
            holder.txt_destination_address.setText(destination.state);
        } else if (destination.county != null) {
            holder.txt_destination_address.setText(destination.county);
        } else {
            holder.txt_destination_address.setText(destination.address);
        }

        if (trip_user_vehicle.trip.price > 0) {
            holder.txt_price.setText(String.valueOf(trip_user_vehicle.trip.price));
            holder.txt_price.setTextColor(context.getColor(R.color.black));
        } else {
            holder.txt_price.setText(context.getString(R.string.free));
            holder.txt_price.setTextColor(context.getColor(R.color.green));
        }

        List<String> avatar_list = new ArrayList<>();
        for (User member :trip_user_vehicle.members) {
            avatar_list.add(member.avatar);
        }
        holder.view_avatar_list.SetAvatarList(avatar_list);
    }


    @SuppressLint("NotifyDataSetChanged")
    public void UpdateData(List<TripAndUserAndVehicle> trip_user_vehicle_list){
        this.trip_user_vehicle_list = trip_user_vehicle_list;
        this.notifyDataSetChanged();
    }

    public void SetOnTripClicked(TripClickedCallback trip_clicked_callback){
        this.trip_clicked_callback = trip_clicked_callback;
    }

    public interface TripClickedCallback{
        void OnClicked(TripAndUserAndVehicle trip_user_vehicle);
    }
}
