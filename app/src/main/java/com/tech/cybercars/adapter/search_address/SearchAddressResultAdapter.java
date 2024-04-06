package com.tech.cybercars.adapter.search_address;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tech.cybercars.R;
import com.tech.cybercars.data.remote.map.reverse_geocoding.ReverseGeocodingResponse;

import java.util.List;

public class SearchAddressResultAdapter extends RecyclerView.Adapter<SearchAddressResultViewHolder> {
    private final List<ReverseGeocodingResponse> search_address_result;
    private SearchAddressResultCallback callback;

    public SearchAddressResultAdapter(List<ReverseGeocodingResponse> search_address_result) {
        this.search_address_result = search_address_result;
    }

    @NonNull
    @Override
    public SearchAddressResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_address_result, parent, false);
        return new SearchAddressResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAddressResultViewHolder holder, int position) {
        ReverseGeocodingResponse reverse_geocoding = search_address_result.get(position);
        holder.InitEvent(reverse_geocoding, callback);
        holder.txt_place_name.setText(reverse_geocoding.name);
        if (reverse_geocoding.display_name != null || !reverse_geocoding.display_name.equals("")) {
            holder.txt_address.setText(reverse_geocoding.display_name);
        } else if (reverse_geocoding.address.road != null) {
            holder.txt_address.setText(reverse_geocoding.address.road);
        } else {
            holder.txt_address.setHeight(0);
        }
    }

    public void SetOnItemClick(SearchAddressResultCallback callback) {
        this.callback = callback;
    }

    @Override
    public int getItemCount() {
        return search_address_result.size();
    }
}
