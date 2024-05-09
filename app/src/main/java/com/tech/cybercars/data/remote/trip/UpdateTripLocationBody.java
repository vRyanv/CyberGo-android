package com.tech.cybercars.data.remote.trip;

import com.tech.cybercars.data.models.trip.Destination;

public class UpdateTripLocationBody {
    public String trip_id;
    public double origin_longitude;
    public double origin_latitude;
    public String origin_city;
    public String origin_state;
    public String origin_county;
    public String origin_address;
    public Destination destination;

    public UpdateTripLocationBody(String trip_id, double origin_longitude, double origin_latitude, String origin_city, String origin_state, String origin_county, String origin_address, Destination destination) {
        this.trip_id = trip_id;
        this.origin_longitude = origin_longitude;
        this.origin_latitude = origin_latitude;
        this.origin_city = origin_city;
        this.origin_state = origin_state;
        this.origin_county = origin_county;
        this.origin_address = origin_address;
        this.destination = destination;
    }
}
