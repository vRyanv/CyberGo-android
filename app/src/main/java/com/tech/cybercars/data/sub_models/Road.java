package com.tech.cybercars.data.sub_models;

import java.io.Serializable;

public class Road  implements Serializable {
    public String geometry;
    public double time;
    public double distance;
    public Location location;

    public Road(String geometry, double time, double distance, Location location) {
        this.geometry = geometry;
        this.time = time;
        this.distance = distance;
        this.location = location;
    }
}
