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
import com.tech.cybercars.constant.TripStatus;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.constant.VehicleType;
import com.tech.cybercars.data.models.Member;
import com.tech.cybercars.data.models.TripFound;
import com.tech.cybercars.data.models.TripManagement;
import com.tech.cybercars.data.models.User;
import com.tech.cybercars.data.models.trip.Destination;
import com.tech.cybercars.data.models.trip.Trip;
import com.tech.cybercars.utils.Helper;

import java.util.ArrayList;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripViewHolder> {
    private List<TripManagement> trip_maganement_list;
    private Context context;
    private TripClickedCallback trip_clicked_callback;

    public TripAdapter(Context context, List<TripManagement> trip_maganement_list) {
        this.trip_maganement_list = trip_maganement_list;
        this.context = context;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        TripManagement trip_user_vehicle = trip_maganement_list.get(position);
        BindData(holder, trip_user_vehicle);
        holder.itemView.setOnClickListener(view -> {
            trip_clicked_callback.OnClicked(trip_user_vehicle);
        });
    }

    @Override
    public int getItemCount() {
        return trip_maganement_list.size();
    }

    private void BindData(TripViewHolder holder, TripManagement trip_management) {

        switch (trip_management.trip_status){
            case TripStatus.OPENING:
                holder.wrapper_item_trip.setBackgroundColor(context.getColor(R.color.opening_color));
                break;
            case TripStatus.CLOSED:
                holder.wrapper_item_trip.setBackgroundColor(context.getColor(R.color.closed_color));
                break;
            default:
                holder.wrapper_item_trip.setBackgroundColor(context.getColor(R.color.finish_color));
                break;
        }

        String avatar_full_path = URL.BASE_URL + URL.AVATAR_RES_PATH + trip_management.trip_owner.avatar;
        Glide.with(context)
                .load(avatar_full_path)
                .placeholder(R.drawable.loading_placeholder)
                .into(holder.img_avatar);

        holder.rating_bar.setClickable(false);
        holder.rating_bar.setRating(trip_management.trip_owner.rating);

        holder.txt_trip_name.setText(trip_management.trip_name);

        switch (trip_management.vehicle.type) {
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

        holder.txt_start_date.setText(trip_management.start_date);
        holder.txt_start_time.setText(trip_management.start_time);

        if(trip_management.origin_city != null){
            holder.txt_origin_address.setText(trip_management.origin_city);
        } else if (trip_management.origin_state != null) {
            holder.txt_origin_address.setText(trip_management.origin_state);
        } else if (trip_management.origin_county != null) {
            holder.txt_origin_address.setText(trip_management.origin_county);
        } else {
            holder.txt_origin_address.setText(trip_management.origin_address);
        }

        Destination destination = trip_management.destinations.get(trip_management.destinations.size()-1);
        if(destination.city != null){
            holder.txt_destination_address.setText(destination.city);
        } else if (destination.state != null) {
            holder.txt_destination_address.setText(destination.state);
        } else if (destination.county != null) {
            holder.txt_destination_address.setText(destination.county);
        } else {
            holder.txt_destination_address.setText(destination.address);
        }

        if (trip_management.price > 0) {
            holder.txt_price.setText(String.valueOf(trip_management.price));
            holder.txt_price.setTextColor(context.getColor(R.color.black));
        } else {
            holder.txt_price.setText(context.getString(R.string.free));
            holder.txt_price.setTextColor(context.getColor(R.color.green));
        }

        List<String> avatar_list = new ArrayList<>();
        for (TripManagement.Member member :trip_management.members) {
            avatar_list.add(member.avatar);
        }
        holder.view_avatar_list.SetAvatarList(avatar_list);
    }


    @SuppressLint("NotifyDataSetChanged")
    public void UpdateData(List<TripManagement> trip_maganement_list){
        this.trip_maganement_list = trip_maganement_list;
        this.notifyDataSetChanged();
    }

    public void UpdateData(TripManagement trip_management, int index){
        this.trip_maganement_list.set(index, trip_management);
        this.notifyItemChanged(index);
    }

    public void SetOnTripClicked(TripClickedCallback trip_clicked_callback){
        this.trip_clicked_callback = trip_clicked_callback;
    }

    public interface TripClickedCallback{
        void OnClicked(TripManagement trip_management);
    }
}
