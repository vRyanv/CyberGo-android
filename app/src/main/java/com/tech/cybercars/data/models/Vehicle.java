package com.tech.cybercars.data.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "vehicle")
public class Vehicle implements Serializable {
    @PrimaryKey
    @NonNull
    @SerializedName("_id")
    public String id;
    public String driver_id;
    public String vehicle_name;
    public String vehicle_type;
    public String license_plates;
    public Long registration_date;
    public String front_vehicle_registration_certificate;
    public String back_vehicle_registration_certificate;
    public String front_vehicle;
    public String back_vehicle;
    public String right_vehicle;
    public String left_vehicle;
    public String status;
    public String back_driving_license;
    public String front_driving_license;

    public Vehicle(@NonNull String id, String vehicle_name, String vehicle_type, String license_plates, Long registration_date,
                   String front_vehicle_registration_certificate, String back_vehicle_registration_certificate,
                   String front_vehicle, String back_vehicle, String right_vehicle, String left_vehicle, String status,
                   String back_driving_license, String front_driving_license) {
        this.id = id;
        this.vehicle_name = vehicle_name;
        this.vehicle_type = vehicle_type;
        this.license_plates = license_plates;
        this.registration_date = registration_date;
        this.front_vehicle_registration_certificate = front_vehicle_registration_certificate;
        this.back_vehicle_registration_certificate = back_vehicle_registration_certificate;
        this.front_vehicle = front_vehicle;
        this.back_vehicle = back_vehicle;
        this.right_vehicle = right_vehicle;
        this.left_vehicle = left_vehicle;
        this.status = status;
        this.back_driving_license = back_driving_license;
        this.front_driving_license = front_driving_license;
    }
}
