package com.tech.cybercars.data.remote.base;

import androidx.room.Ignore;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BaseResponse {
    @Ignore
    public int code;
    @Ignore
    public String message;
    @Ignore
    public ArrayList<String> errors;
}
