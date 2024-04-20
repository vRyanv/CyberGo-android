package com.tech.cybercars.adapter.trip_found;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tech.cybercars.R;
import com.tech.cybercars.constant.DestinationType;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.constant.VehicleType;
import com.tech.cybercars.data.models.TripFound;
import com.tech.cybercars.data.models.trip.Destination;

import java.util.ArrayList;
import java.util.List;

public class TripFoundAdapter extends RecyclerView.Adapter<TripFoundViewHolder> {
    private List<TripFound> trip_found_list;
    private final Context context;
    private ItemClickedCallback item_clicked_callback;
    private ItemClickedCallback btn_see_overview_clicked_callback;
    private TargetVehicleTypeClickedCallback btn_target_vehicle_type_clicked_callback;

    public TripFoundAdapter(Context context, List<TripFound> trip_found_list) {
        this.trip_found_list = trip_found_list;
        this.context = context;
    }

    @NonNull
    @Override
    public TripFoundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip_found, parent, false);
        return new TripFoundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripFoundViewHolder holder, int position) {
        TripFound trip_found = trip_found_list.get(position);
        BindData(holder, trip_found);
        holder.btn_see_overview_trip.setOnClickListener(view -> btn_see_overview_clicked_callback.OnClicked(trip_found));
        holder.img_vehicle_type.setOnClickListener(view -> btn_target_vehicle_type_clicked_callback.OnClicked(position));
        holder.itemView.setOnClickListener(view -> item_clicked_callback.OnClicked(trip_found));
    }

    @Override
    public int getItemCount() {
        return trip_found_list != null ? trip_found_list.size() : 0;
    }

    public void SetOnItemClicked(ItemClickedCallback item_clicked_callback){
        this.item_clicked_callback = item_clicked_callback;
    }

    public void SetOnSeeOverviewButtonClicked(ItemClickedCallback btn_see_overview_clicked_callback){
        this.btn_see_overview_clicked_callback = btn_see_overview_clicked_callback;
    }

    public void SetOnTargetVehicleButtonClicked(TargetVehicleTypeClickedCallback btn_target_vehicle_type_clicked_callback){
        this.btn_target_vehicle_type_clicked_callback = btn_target_vehicle_type_clicked_callback;
    }

    private void BindData(TripFoundViewHolder holder, TripFound trip_found) {
        String avatar_full_path = URL.BASE_URL + URL.AVATAR_RES_PATH + trip_found.owner.avatar;
        Glide.with(context)
                .load(avatar_full_path)
                .placeholder(R.drawable.loading_placeholder)
                .into(holder.img_avatar);

        holder.rating_bar.setClickable(false);
        holder.rating_bar.setRating(trip_found.owner.rating);

        holder.txt_trip_name.setText(trip_found.trip_name);

        switch (trip_found.vehicle_type) {
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

        holder.txt_start_date.setText(trip_found.start_date);
        holder.txt_start_time.setText(trip_found.start_time);

        if(trip_found.origin_city != null){
            holder.txt_origin_address.setText(trip_found.origin_city);
        } else if (trip_found.origin_state != null) {
            holder.txt_origin_address.setText(trip_found.origin_state);
        } else if (trip_found.origin_county != null) {
            holder.txt_origin_address.setText(trip_found.origin_county);
        } else {
            holder.txt_origin_address.setText(trip_found.origin_address);
        }

        Destination destination = trip_found.destination_list.get(trip_found.destination_list.size()-1);
        if(destination.city != null){
            holder.txt_destination_address.setText(destination.city);
        } else if (destination.state != null) {
            holder.txt_destination_address.setText(destination.state);
        } else if (destination.county != null) {
            holder.txt_destination_address.setText(destination.county);
        } else {
            holder.txt_destination_address.setText(destination.address);
        }

        if (trip_found.price > 0) {
            holder.txt_price.setText(String.valueOf(trip_found.price));
        } else {
            holder.txt_price.setText(context.getString(R.string.free));
            holder.txt_price.setTextColor(context.getColor(R.color.green));
        }
        List<String> avatar_list = new ArrayList<>();
        for (TripFound.User user :trip_found.member_list) {
            avatar_list.add(user.avatar);
        }
        holder.view_avatar_list.SetAvatarList(avatar_list);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void UpdateAdapter(List<TripFound> trip_found_list) {
        this.trip_found_list = trip_found_list;
        this.notifyDataSetChanged();
    }

    public interface ItemClickedCallback{
        void OnClicked(TripFound trip_found);
    }

    public interface TargetVehicleTypeClickedCallback{
        void OnClicked(int trip_index);
    }

}
