package com.tech.cybercars.data.local.trip;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tech.cybercars.data.models.trip.Trip;
import java.util.List;

@Dao
public interface TripDAO {
    @Insert
    void InsertTrip(Trip trip);
    @Update
    void UpdateTrip(Trip trip);

    @Query("SELECT * FROM trip")
    List<Trip> FindAll();
}
