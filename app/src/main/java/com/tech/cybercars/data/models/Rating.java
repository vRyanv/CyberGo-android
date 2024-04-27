package com.tech.cybercars.data.models;

import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

public class Rating {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @SerializedName("_id")
    public String rating_id;
    public String user_rating_id;
    @Ignore
    public User user_rating;
    public String driver_id;
    public int star;
    public String comment;
    public String rating_date;
    public static class User{
        public String full_name;
        public String avatar;
    }


}
