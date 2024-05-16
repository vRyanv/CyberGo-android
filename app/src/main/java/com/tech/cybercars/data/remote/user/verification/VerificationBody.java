package com.tech.cybercars.data.remote.user.verification;

import com.google.gson.annotations.SerializedName;

public class VerificationBody {
    public String otp_code;
    public String number_prefix;
    public String phone_number;

    public VerificationBody(String otp_code, String number_prefix, String phone_number) {
        this.otp_code = otp_code;
        this.number_prefix = number_prefix;
        this.phone_number = phone_number;
    }
}
