package com.tech.cybercars.data.local.user;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "user")
public class User implements Serializable {
    @PrimaryKey
    @NonNull
    public String id;
    public String role;
    public String email;
    public String full_name;
    public String gender;
    public String avatar;
    public String id_number;
    public String address;

    public String phone_number;
    public String country_prefix;
    public String country_name_code;
    public String front_id_card;
    public String back_id_card;

    public User(@NonNull String id, String role, String email, String full_name, String gender, String avatar, String id_number, String address, String phone_number, String country_prefix, String country_name_code, String front_id_card, String back_id_card) {
        this.id = id;
        this.role = role;
        this.email = email;
        this.full_name = full_name;
        this.gender = gender;
        this.avatar = avatar;
        this.id_number = id_number;
        this.address = address;
        this.phone_number = phone_number;
        this.country_prefix = country_prefix;
        this.country_name_code = country_name_code;
        this.front_id_card = front_id_card;
        this.back_id_card = back_id_card;
    }
}
