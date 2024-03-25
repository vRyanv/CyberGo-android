package com.tech.cybercars.data.remote.user.signin;

import com.google.gson.annotations.SerializedName;
import com.tech.cybercars.data.remote.base.BaseResponse;

public class SignInResponse extends BaseResponse {
    public String token;
    public String user_id;
    public String avatar;
    public String full_name;
    public String phone_number;
}
