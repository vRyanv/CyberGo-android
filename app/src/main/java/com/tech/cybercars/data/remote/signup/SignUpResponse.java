package com.tech.cybercars.data.remote.signup;

import com.google.gson.annotations.SerializedName;
import com.tech.cybercars.data.remote.base.BaseResponse;

public class SignUpResponse extends BaseResponse {
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("verify_account")
    private boolean verify_account;
    @SerializedName("is_email_used")
    private boolean is_email_used;
    @SerializedName("is_phone_used")
    private boolean is_phone_used;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public boolean isVerify_account() {
        return verify_account;
    }

    public void setVerify_account(boolean verify_account) {
        this.verify_account = verify_account;
    }

    public boolean isIs_email_used() {
        return is_email_used;
    }

    public void setIs_email_used(boolean is_email_used) {
        this.is_email_used = is_email_used;
    }

    public boolean isIs_phone_used() {
        return is_phone_used;
    }

    public void setIs_phone_used(boolean is_phone_used) {
        this.is_phone_used = is_phone_used;
    }
}
