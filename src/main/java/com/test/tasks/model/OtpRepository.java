package com.test.tasks.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpRepository extends JpaRepository<OtpStore, Integer> {
    OtpStore findByOtpId(Integer otpId);
}
