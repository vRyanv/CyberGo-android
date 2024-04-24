package com.tech.cybercars.data.models;

import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.tech.cybercars.data.models.trip.Destination;
import com.tech.cybercars.data.models.trip.Trip;

import java.util.List;

public class TripManagement {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String trip_id;
    public String trip_owner_id;
    @Ignore
    public User trip_owner;
    public String trip_name;
    public String vehicle_id;
    public String vehicle_type;
    public String start_date;
    public String start_time;
    public float price;
    public double origin_longitude;
    public double origin_latitude;
    public String origin_city;
    public String origin_state;
    public String origin_county;
    public String origin_address;
    @Ignore
    public List<Destination> destinations;
    @Ignore
    public List<Member> members;
    public String trip_status;
    public static class User {
        public String user_id;
        public String full_name;
        public String avatar;
        public int rating;
    }
    public static class Member {
        public String member_id;
        public String user_id;
        public String full_name;
        public String avatar;
        public int rating;
        public String status; //queue | joined | finish
        public Location origin;
        public Location destination;
        public long request_at;

        public static class Location {
            public double longitude;
            public double latitude;
            public String origin_address;
        }
    }
}
