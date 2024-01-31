package com.tech.cybercars.adapter.search_address;

import com.tech.cybercars.data.remote.map.reverse_geocoding.ReverseGeocodingResponse;

public interface SearchAddressResultCallback {
    public void OnClick(ReverseGeocodingResponse reverse_geocoding);
}
