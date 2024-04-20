package com.tech.cybercars.data.models;

import com.tech.cybercars.data.models.trip.Destination;

import java.io.Serializable;
import java.util.ArrayList;

public class TripFound implements Serializable {
    public String trip_id;
    public User owner;
    public String trip_name;
    public String vehicle_type;
    public String start_date;
    public String start_time;
    public double price;
    public ArrayList<User> member_list;
    public ArrayList<Destination> destination_list;
    public String destination_type;
    public String origin_address;
    public String origin_city;
    public String origin_state;
    public String origin_county;
    public double origin_longitude;
    public double origin_latitude;
    public String description;
    public static class User implements Serializable{
        public String user_id;
        public String full_name;
        public String avatar;
        public float rating;
    }
}
