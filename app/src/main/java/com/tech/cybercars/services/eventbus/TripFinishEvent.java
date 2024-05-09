package com.tech.cybercars.services.eventbus;

public class TripFinishEvent {
    public String trip_id;
    public String status;

    public TripFinishEvent() {
    }

    public TripFinishEvent(String trip_id, String status) {
        this.trip_id = trip_id;
        this.status = status;
    }
}
