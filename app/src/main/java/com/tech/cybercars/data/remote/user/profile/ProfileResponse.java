package com.tech.cybercars.data.remote.user.profile;

import com.tech.cybercars.data.remote.base.BaseResponse;

public class ProfileResponse extends BaseResponse {
    public String id;
    public String role;
    public String email;
    public String gender;
    public String full_name;
    public String phone_number;
    public Country country;
    public String avatar;
    public String address;
    public String front_id_card;
    public String back_id_card;
    public String id_number;
    public class Country{
        public String name;
        public String code;
        public String prefix;
    }
}
