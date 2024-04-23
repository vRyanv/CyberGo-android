package com.tech.cybercars.data.remote.trip.find_trip;

public class FindTripBody {
    public String origin_city;
    public String origin_state;
    public String origin_county;
    public String origin_address;
    public String start_date;
    public float route_match_percentage;
    public String geometry;

    public FindTripBody(String origin_city, String origin_state, String origin_county, String origin_address,
                        String start_date, float route_match_percentage, String geometry) {
        this.origin_city = origin_city;
        this.origin_state = origin_state;
        this.origin_county = origin_county;
        this.origin_address = origin_address;
        this.start_date = start_date;
        this.route_match_percentage = route_match_percentage;
        this.geometry = geometry;
    }
}
