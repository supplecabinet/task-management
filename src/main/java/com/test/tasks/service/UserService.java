package com.test.tasks.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
    String getUserFromToken(HttpServletRequest request) ;
}
