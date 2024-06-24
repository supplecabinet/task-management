package com.test.tasks.repository;

import com.test.tasks.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDetails, Long>{

    UserDetails findByUserId(String userId);
    UserDetails findByUserIdIgnoreCase(String userId);

    UserDetails findByOtp(Integer otp);
}
