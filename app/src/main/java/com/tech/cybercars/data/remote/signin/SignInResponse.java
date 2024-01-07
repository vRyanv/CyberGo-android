package com.tech.cybercars.data.remote.signin;

import com.google.gson.annotations.SerializedName;
import com.tech.cybercars.data.remote.base.BaseResponse;

public class SignInResponse extends BaseResponse {
    @SerializedName("token")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
