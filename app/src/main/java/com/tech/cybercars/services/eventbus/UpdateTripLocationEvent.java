package com.tech.cybercars.services.eventbus;

import com.tech.cybercars.data.models.TripManagement;

public class UpdateTripLocationEvent {
    public TripManagement trip_management;

    public UpdateTripLocationEvent(TripManagement trip_management) {
        this.trip_management = trip_management;
    }
}
