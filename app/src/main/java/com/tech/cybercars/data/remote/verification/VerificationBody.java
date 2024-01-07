package com.tech.cybercars.data.remote.verification;

import com.google.gson.annotations.SerializedName;

public class VerificationBody {
    @SerializedName("otp_code")
    private String otp_code;
    @SerializedName("email")
    private String email;

    public VerificationBody(String otp_code, String email) {
        this.otp_code = otp_code;
        this.email = email;
    }

    public String getOtpCode() {
        return otp_code;
    }

    public void setOtpCode(String otp) {
        this.otp_code = otp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
