package com.tech.cybercars.data.local.vehicle;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tech.cybercars.data.models.Notification;
import com.tech.cybercars.data.models.Vehicle;

import java.util.List;

@Dao
public interface VehicleDAO {
    @Insert
    void InsertVehicle(Vehicle vehicle);
    @Insert
    void InsertManyVehicle(List<Vehicle> vehicles);
    @Query("DELETE FROM vehicle")
    public void ClearTable();

    @Query("SELECT * FROM vehicle WHERE id = :id")
    Vehicle FindVehicleById(String id);

    @Query("SELECT * FROM vehicle ORDER BY registration_date DESC")
    List<Vehicle> GetVehicleList();

    @Query("SELECT * FROM vehicle WHERE status == :status ORDER BY registration_date DESC")
    List<Vehicle> GetVehicleListByStatus(String status);
}
