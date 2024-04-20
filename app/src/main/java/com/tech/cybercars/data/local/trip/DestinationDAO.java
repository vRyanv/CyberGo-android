package com.tech.cybercars.data.local.trip;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tech.cybercars.data.models.trip.Destination;

import java.util.List;

@Dao
public interface DestinationDAO {
    @Query("SELECT * FROM destination WHERE trip_id = :trip_id")
    List<Destination> FindByTripId(String trip_id);
    @Insert
    void InsertDestinations(List<Destination> destinations);
    @Query("DELETE FROM destination")
    public void ClearTable();
}
