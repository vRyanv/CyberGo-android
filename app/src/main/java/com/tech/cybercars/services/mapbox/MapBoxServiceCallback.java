package com.tech.cybercars.services.mapbox;

import com.mapbox.mapboxsdk.maps.Style;

public interface MapBoxServiceCallback {
    public void OnStyleReadyCallback(Style style);

}
