package com.tech.cybercars.data.remote.trip.find_trip;

public class FindTripBody {
    public String start_city;
    public String start_state;
    public String start_county;
    public String geometry;

    public FindTripBody(String start_city, String start_state, String start_county,String geometry) {
        this.start_city = start_city;
        this.start_state = start_state;
        this.start_county = start_county;
        this.geometry = geometry;
    }
}
