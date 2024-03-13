package com.tech.cybercars.data.remote.user.verification;

import com.google.gson.annotations.SerializedName;
import com.tech.cybercars.data.remote.base.BaseResponse;

public class VerificationResponse extends BaseResponse {
    @SerializedName("user_token")
    private String user_token;
    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }

}
