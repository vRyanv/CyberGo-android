package com.tech.cybercars.services.mapbox;

import com.mapbox.mapboxsdk.maps.Style;

public interface MapboxMapServiceCallback {
    public void OnStyleReadyCallback(Style style);
}
