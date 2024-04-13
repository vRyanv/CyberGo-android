package com.tech.cybercars.data.models.trip;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
@Entity(tableName = "destination")
public class Destination implements Serializable {
    @PrimaryKey
    @NonNull
    @SerializedName("_id")
    public String destination_id;
    @SerializedName("trip")
    public String trip_id;
    public String geometry;
    public double time;
    public double distance;
    public String city;
    public String state;
    public String county;
    public String address;
    public double longitude;
    public double latitude;
    public Destination(){}
    public Destination(@NonNull String destination_id, String trip_id, String geometry, double time, double distance, String city,
                       String state, String county, String address, double longitude, double latitude) {
        this.destination_id = destination_id;
        this.trip_id = trip_id;
        this.geometry = geometry;
        this.time = time;
        this.distance = distance;
        this.city = city;
        this.state = state;
        this.county = county;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
