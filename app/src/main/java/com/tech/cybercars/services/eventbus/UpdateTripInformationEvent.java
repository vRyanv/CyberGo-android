package com.tech.cybercars.services.eventbus;

import com.tech.cybercars.data.models.TripManagement;

public class UpdateTripInformationEvent {
    public TripManagement trip_management;
    public UpdateTripInformationEvent(TripManagement trip_management) {
        this.trip_management = trip_management;
    }
}
