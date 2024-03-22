package com.tech.cybercars.data.local.user;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDAO {
    @Insert
    void InsertUser(User user);
    @Update
    void UpdateUser(User user);

    @Query("SELECT * FROM user WHERE id = :id")
    User FindUserById(String id);

}
