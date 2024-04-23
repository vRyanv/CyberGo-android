package com.tech.cybercars.data.models;

import androidx.room.Embedded;

import com.tech.cybercars.data.models.trip.Trip;

import java.util.List;

public class TripAndUserAndVehicle {
    @Embedded
    public Trip trip;

    @Embedded
    public User user;
    @Embedded
    public Vehicle vehicle;

    public List<User> members;
}
