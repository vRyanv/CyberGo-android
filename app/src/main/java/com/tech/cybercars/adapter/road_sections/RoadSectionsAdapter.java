package com.tech.cybercars.adapter.road_sections;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tech.cybercars.R;
import com.tech.cybercars.data.sub_models.Road;
import com.tech.cybercars.utils.DateUtil;
import com.tech.cybercars.utils.Helper;

import java.util.List;

public class RoadSectionsAdapter extends RecyclerView.Adapter<RoadSectionsViewHolder> {
    private List<Road> road_list;
    public RoadSectionsAdapter(List<Road> road_list){
        this.road_list = road_list;
    }
    @NonNull
    @Override
    public RoadSectionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_road_sections, parent, false);
        return new RoadSectionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoadSectionsViewHolder holder, int position) {
        Road road_section = road_list.get(position);
        String time = DateUtil.ConvertSecondToHour(road_section.time);
        holder.txt_time_road.setText(time);

        String distance = Helper.ConvertMeterToKiloMeterString(road_section.distance) + " Km";
        holder.txt_distance_road.setText(distance);
        holder.txt_address.setText(road_section.location.destination_address);
    }

    @Override
    public int getItemCount() {
        return road_list.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void UpdateAdapter(List<Road> road_list){
        this.road_list = road_list;
        this.notifyDataSetChanged();
    }

    public interface RoadSectionsListener{
        void OnClicked(Road road);
    }
}
