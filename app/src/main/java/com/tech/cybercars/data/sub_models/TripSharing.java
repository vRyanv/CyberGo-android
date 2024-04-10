package com.tech.cybercars.data.sub_models;

import java.io.Serializable;
import java.util.List;

public class TripSharing implements Serializable {
    public String origin_city;
    public String origin_state;
    public String origin_county;
    public String origin_address;
    public List<Road> road_sections;
    public String vehicle_id;
    public Long start_date;
    public Long start_time;
    public double price;

    public TripSharing(String origin_city, String origin_state, String origin_county, String origin_address,
                       List<Road> road_sections, String vehicle_id, Long start_date, Long start_time, double price) {
        this.origin_city = origin_city;
        this.origin_state = origin_state;
        this.origin_county = origin_county;
        this.origin_address = origin_address;
        this.road_sections = road_sections;
        this.vehicle_id = vehicle_id;
        this.start_date = start_date;
        this.start_time = start_time;
        this.price = price;
    }
}
