package com.test.tasks.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.tasks.exception.UnAuthorizedException;
import com.test.tasks.model.UserDetails;
import com.test.tasks.model.UserDetailsPojo;
import com.test.tasks.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
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

    @Override
    public void login(Map<String, String> login, HttpServletResponse response) throws JsonProcessingException {
        String username = login.get("username");
        String password = "";
        try {
            password = Arrays.toString(org.apache.tomcat.util.codec.binary.Base64.decodeBase64(login.get("password"))); //Decode Base64 password input
        } catch (Exception e) {
            throw new RuntimeException("Security Breach Attempted!");
        }

        UserDetails userDetails = userRepository.findByUserIdIgnoreCase(username.toLowerCase()); //Find user details
        if (userDetails == null) {
            throw new UnAuthorizedException("Invalid Username!");
        }
        String userPassword = Arrays.toString(org.apache.tomcat.util.codec.binary.Base64.decodeBase64(userDetails.getPassword())); //Find stored password and decrypt it
        if (!password.equals(userPassword)) {  //Check decrypted password from DB and user input
            throw new UnAuthorizedException("Invalid Credentials!");
        }
        userDetails.setLastLoginDate(new Date()); //Set current Login DateTime after successful authentication
        userRepository.save(userDetails);
        UserDetailsPojo udp = new UserDetailsPojo();
        BeanUtils.copyProperties(userDetails,udp);
        udp.setPassword(null); //Hide password from Auth Object for header
        response.setHeader("Access-Control-Expose-Headers", "X-AUTH-TOKEN"); //Exposes Auth Header
        response.setHeader("X-AUTH-TOKEN",		//Adding Auth Token Header
                org.apache.tomcat.util.codec.binary.Base64.encodeBase64String(
                        new ObjectMapper()
                                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                                .writeValueAsString(udp).getBytes()));
    }

    @Override
    public void signUp(Map<String, String> signUp, HttpServletResponse response) {
        String username = signUp.get("username");
        String password = "";
        try {
            password = Arrays.toString(org.apache.tomcat.util.codec.binary.Base64.decodeBase64(signUp.get("password"))); //Decode Base64 password input
        } catch (Exception e) {
            throw new RuntimeException("Security Breach Attempted!");
        }
        UserDetails ud = userRepository.findByUserIdIgnoreCase(username.toLowerCase()); //Find user details
        if (ud != null) {
            throw new RuntimeException("User already exists!");
        }
        UserDetails userDetails = new UserDetails();
        userDetails.setUserId(username);
        userDetails.setPassword(signUp.get("password"));
        userRepository.save(userDetails);
    }

}
