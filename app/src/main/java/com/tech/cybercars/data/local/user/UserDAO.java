package com.tech.cybercars.data.local.user;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tech.cybercars.data.models.User;

@Dao
public interface UserDAO {
    @Insert
    void InsertUser(User user);
    @Update
    void UpdateUser(User user);
    @Query("SELECT * FROM user WHERE user_id = :id")
    User FindUserById(String id);
    @Query("DELETE FROM user WHERE user_id = :user_id")
    public void DeleteById(String user_id);
    @Query("DELETE FROM user")
    public void ClearTable();
}
