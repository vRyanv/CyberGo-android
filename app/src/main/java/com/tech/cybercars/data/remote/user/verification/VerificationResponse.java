package com.tech.cybercars.data.remote.user.verification;

import com.google.gson.annotations.SerializedName;
import com.tech.cybercars.data.remote.base.BaseResponse;

public class VerificationResponse extends BaseResponse {
    public String user_token;
    public String user_id;
    public String avatar;
    public String full_name;
    public String phone_number;
}
