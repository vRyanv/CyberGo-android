package com.tech.cybercars.data.models;

import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

public class Rating {
    @SerializedName("_id")
    public String rating_id;
    public User user_send;
    public int star;
    public String comment;
    @SerializedName("createdAt")
    public long date;
    public static class User{
        public String full_name;
        public String avatar;
    }


}
