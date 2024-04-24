package com.tech.cybercars.data.models.trip;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.tech.cybercars.data.remote.trip.find_trip.MemberBody;

import java.io.Serializable;
import java.util.ArrayList;

@Entity(tableName = "trip")
public class Trip implements Serializable {
    @PrimaryKey
    @NonNull
    @SerializedName("_id")
    public String trip_id;
    public String trip_owner;
    public String name;
    public String origin_city;
    public String origin_state;
    public String origin_county;
    public String origin_address;
    @Ignore
    public ArrayList<Destination> destinations;
    public double origin_longitude;
    public double origin_latitude;
    public String destination_type;
    @SerializedName("vehicle")
    @ColumnInfo(name = "vehicle_id_trip")
    public String vehicle_id;
    @Ignore
    public ArrayList<MemberBody> members;
    public String start_date;
    public String start_time;
    public double price;
    public String description;
    @ColumnInfo(name = "trip_status")
    public String status;
    public Trip(){}
    public Trip(@NonNull String trip_id, String name, String origin_city, String origin_state, String origin_county,
                String origin_address, double origin_longitude, double origin_latitude,
                String destination_type, String vehicle_id, String start_date, String start_time, double price) {
        this.name = name;
        this.origin_city = origin_city;
        this.origin_state = origin_state;
        this.origin_county = origin_county;
        this.origin_longitude = origin_longitude;
        this.origin_latitude = origin_latitude;
        this.origin_address = origin_address;
        this.destination_type = destination_type;
        this.vehicle_id = vehicle_id;
        this.start_date = start_date;
        this.start_time = start_time;
        this.price = price;
        this.trip_id = trip_id;
    }
}
