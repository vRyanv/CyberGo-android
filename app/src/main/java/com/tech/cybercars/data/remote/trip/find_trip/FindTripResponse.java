package com.tech.cybercars.data.remote.trip.find_trip;

import com.tech.cybercars.data.models.TripFound;
import com.tech.cybercars.data.remote.base.BaseResponse;

import java.util.List;

public class FindTripResponse extends BaseResponse {
    public List<TripFound> trip_found_list;
}
