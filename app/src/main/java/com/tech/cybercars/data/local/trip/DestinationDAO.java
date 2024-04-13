package com.tech.cybercars.data.local.trip;

import androidx.room.Dao;
import androidx.room.Query;

import com.tech.cybercars.data.models.trip.Destination;

import java.util.List;

@Dao
public interface DestinationDAO {
    @Query("SELECT * FROM Destination WHERE trip_id = :trip_id")
    List<Destination> FindByTripId(String trip_id);
}
