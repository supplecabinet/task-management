package com.test.tasks.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public interface UserService {
    String getUserFromToken(HttpServletRequest request) ;

    void login(Map<String, String> login, HttpServletResponse response) throws JsonProcessingException;

    void signUp(Map<String, String> signUp, HttpServletResponse response);

    void generateOTP(String userId);

    void validateOTP(Map<String, String> validateBody);

    String activateUser(String verify, HttpServletResponse response);
}
