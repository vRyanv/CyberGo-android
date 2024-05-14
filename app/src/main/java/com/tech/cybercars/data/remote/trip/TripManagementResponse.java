package com.tech.cybercars.data.remote.trip;

import com.tech.cybercars.data.models.TripManagement;
import com.tech.cybercars.data.remote.base.BaseResponse;

import java.util.List;

public class TripManagementResponse extends BaseResponse {
    public List<TripManagement> shared_trip_list;
    public List<TripManagement> join_trip_list;
}
