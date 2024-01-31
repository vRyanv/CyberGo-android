package com.tech.cybercars.adapter.search_address;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.tech.cybercars.R;
import com.tech.cybercars.data.remote.map.reverse_geocoding.ReverseGeocodingResponse;

public class SearchAddressResultViewHolder extends RecyclerView.ViewHolder {
    public TextView txt_address;
    public TextView txt_place_name;
    public SearchAddressResultViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_address = itemView.findViewById(R.id.txt_address);
        txt_place_name = itemView.findViewById(R.id.txt_place_name);
    }

    public void InitEvent(final ReverseGeocodingResponse reverse_geocoding, final SearchAddressResultCallback callback){
        itemView.setOnClickListener(view ->{
            callback.OnClick(reverse_geocoding);
        });
    }
}
