package com.test.tasks.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="otp_store")
public class OtpStore {

    @Id
    private Integer otpId;
    private String otpFull;

    public Integer getOtpId() {
        return otpId;
    }

    public void setOtpId(Integer otpId) {
        this.otpId = otpId;
    }

    public String getOtpFull() {
        return otpFull;
    }

    public void setOtpFull(String otpFull) {
        this.otpFull = otpFull;
    }
}
