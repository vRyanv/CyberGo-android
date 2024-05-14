package com.tech.cybercars.data.models;

import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.tech.cybercars.data.models.trip.Destination;

import java.io.Serializable;
import java.util.List;

public class TripManagement implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String trip_id;
    public String trip_owner_id;
    @Ignore
    public User trip_owner;
    public String trip_name;
    public Vehicle vehicle;
    public String start_date;
    public String start_time;
    public float price;
    public double origin_longitude;
    public double origin_latitude;
    public String origin_city;
    public String origin_state;
    public String origin_county;
    public String origin_address;
    public String destination_type;
    @Ignore
    public List<Destination> destinations;
    @Ignore
    public List<Member> members;
    public String trip_status;
    public String description;
    public static class User implements Serializable{
        public String user_id;
        public String full_name;
        public String avatar;
        public int rating;
    }
    public static class Member implements Serializable{
        public String member_id;
        public String user_id;
        public String full_name;
        public String avatar;
        public int rating;
        public String status; //queue | joined | finish
        public Location origin;
        public Location destination;
        public String geometry;
        public long request_at;

        public static class Location implements Serializable{
            public double longitude;
            public double latitude;
            public double time;
            public double distance;
            public String address;
        }
    }

    public static class Vehicle implements Serializable{
        public String id;
        public String type;
        public String license_plates;
        public String front_vehicle;
        public String back_vehicle;
        public String left_vehicle;
        public String right_vehicle;
    }
}
