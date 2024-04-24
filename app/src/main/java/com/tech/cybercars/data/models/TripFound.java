package com.tech.cybercars.data.models;

import com.tech.cybercars.data.models.trip.Destination;

import java.io.Serializable;
import java.util.ArrayList;

public class TripFound implements Serializable {
    public String trip_id;
    public User owner;
    public String trip_name;
    public Vehicle vehicle;
    public String start_date;
    public String start_time;
    public double price;
    public float route_match_percentage;
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

    public static class Vehicle implements Serializable{
        public String id;
        public String type;
        public String front_vehicle;
        public String back_vehicle;
        public String left_vehicle;
        public String right_vehicle;
    }
}
