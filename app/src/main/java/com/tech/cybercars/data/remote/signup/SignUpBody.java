package com.tech.cybercars.data.remote.signup;

import com.google.gson.annotations.SerializedName;

public class SignUpBody {
    @SerializedName("email")
    private String email;
    @SerializedName("full_name")
    private String full_name;
    @SerializedName("phone_number")
    private String phone_number;
    @SerializedName("number_prefix")
    private String number_prefix;
    @SerializedName("gender")
    private int gender;
    @SerializedName("password")
    private String password;
    @SerializedName("confirm_password")
    private String confirm_password;

    public SignUpBody(String email, String full_name, String phone_number, String number_prefix, int gender, String password, String confirm_password) {
        this.email = email;
        this.full_name = full_name;
        this.phone_number = phone_number;
        this.number_prefix = number_prefix;
        this.gender = gender;
        this.password = password;
        this.confirm_password = confirm_password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getNumber_prefix() {
        return number_prefix;
    }

    public void setNumber_prefix(String number_prefix) {
        this.number_prefix = number_prefix;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }
}
