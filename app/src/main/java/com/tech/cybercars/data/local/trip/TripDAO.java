package com.tech.cybercars.data.local.trip;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.tech.cybercars.data.models.TripManagement;
import com.tech.cybercars.data.models.trip.Trip;

import java.util.List;

@Dao
public interface TripDAO {
    @Insert
    void InsertTrip(Trip trip);
    @Update
    void UpdateTrip(Trip trip);

//    @Query("SELECT * FROM trip as t, user as u, vehicle as v WHERE u.user_id == t.trip_owner AND trip_owner == :user_id AND v.driver_id == u.user_id")
//    List<TripManagement> FindMyTrip(String user_id);
//    @Query("SELECT * FROM trip as t, user as u, vehicle as v WHERE u.user_id == t.trip_owner AND trip_owner != :user_id AND v.driver_id == u.user_id")
//    List<TripManagement> FindTripJoin(String user_id);
    @Query("DELETE FROM trip")
    public void ClearTable();
}
