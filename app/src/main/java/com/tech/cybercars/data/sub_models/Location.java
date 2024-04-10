package com.tech.cybercars.data.sub_models;

import java.io.Serializable;

public class Location implements Serializable {
    public double destination_longitude;
    public double destination_latitude;
    public String destination_address;

    public Location(double destination_longitude, double destination_latitude, String destination_address) {
        this.destination_longitude = destination_longitude;
        this.destination_latitude = destination_latitude;
        this.destination_address = destination_address;
    }
}
