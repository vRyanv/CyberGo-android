package com.tech.cybercars.data.remote.trip;

import com.tech.cybercars.data.models.trip.Destination;
import com.tech.cybercars.data.models.trip.Trip;
import com.tech.cybercars.data.remote.base.BaseResponse;

import java.util.ArrayList;

public class TripBodyAndResponse extends BaseResponse {
    public Trip trip;
    public ArrayList<Destination> destinations;
}
