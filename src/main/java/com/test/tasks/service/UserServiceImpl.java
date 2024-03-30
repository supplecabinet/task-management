package com.test.tasks.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.tasks.exception.UnAuthorizedException;
import com.test.tasks.model.UserDetailsPojo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class UserServiceImpl implements UserService{
    @Override
    public String getUserFromToken(HttpServletRequest request)  {
        String token = request.getHeader("X-AUTH-TOKEN");
        if (token == null) {
            throw new UnAuthorizedException("User Not Authorized!");
        }
        byte[] decodedBytes = Base64.getDecoder().decode(token);
        String decoded = new String(decodedBytes);
        try {
            return new ObjectMapper()
                    .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                    .readValue(decoded, UserDetailsPojo.class)
                    .getUserId();
        } catch(JsonProcessingException e) {
            throw new RuntimeException("Invalid Token!");
        }

    }
}
