package com.tech.cybercars.data.remote.trip;

public class UpdateTripInformationBody {
    public String trip_id;
    public String trip_name;
    public String start_date;
    public String start_time;
    public String price;
    public String description;

    public UpdateTripInformationBody(String trip_id, String trip_name, String start_date, String start_time, String price, String description) {
        this.trip_id = trip_id;
        this.trip_name = trip_name;
        this.start_date = start_date;
        this.start_time = start_time;
        this.price = price;
        this.description = description;
    }
}
