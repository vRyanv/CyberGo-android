package com.tech.cybercars.data.remote.trip.find_trip;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MemberBody implements Serializable {
    @SerializedName("_id")
    public String member_id;
    public String trip_id;
    @SerializedName("user")
    public String user_id;
    public Location origin;
    public Location destination;
    public String geometry;
    @SerializedName("createdAt")
    public long request_at;
    public String status;
    public static class Location implements Serializable {
        public double longitude;
        public double latitude;
        public String address;
    }
    public MemberBody(){}
    public MemberBody(String trip_id, Location origin, Location destination, String geometry) {
        this.trip_id = trip_id;
        this.origin = origin;
        this.destination = destination;
        this.geometry = geometry;
    }
}
