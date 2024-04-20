package com.tech.cybercars.data.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "Member")
public class Member implements Serializable{
    @PrimaryKey
    @NonNull
    public String member_id;
    public String trip_id;
    public String user_id;
    public double origin_longitude;
    public double origin_latitude;
    public double destination_longitude;
    public double destination_latitude;
    public String origin_address;
    public String destination_address;
    public String geometry;
    public long request_at;
    public String status;
    public Member(){}

    public Member(@NonNull String member_id, String trip_id, String user_id,
                  double origin_longitude, double origin_latitude, double destination_longitude,
                  double destination_latitude, String origin_address, String destination_address, String geometry, long request_at, String status) {
        this.member_id = member_id;
        this.trip_id = trip_id;
        this.user_id = user_id;
        this.origin_longitude = origin_longitude;
        this.origin_latitude = origin_latitude;
        this.destination_longitude = destination_longitude;
        this.destination_latitude = destination_latitude;
        this.origin_address = origin_address;
        this.destination_address = destination_address;
        this.geometry = geometry;
        this.request_at = request_at;
        this.status = status;
    }
}
