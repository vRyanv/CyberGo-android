package com.tech.cybercars.data.remote.trip;

public class RequestToJoinBody {
    public String trip_id;
    public Location origin;
    public Location destination;
    public String geometry;
    public static class Location {
        public Double longitude;
        public Double latitude;
        public String address;
    }
}
