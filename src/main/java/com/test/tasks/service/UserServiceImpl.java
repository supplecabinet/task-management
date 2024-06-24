package com.test.tasks.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.tasks.exception.UnAuthorizedException;
import com.test.tasks.model.*;
import com.test.tasks.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OtpRepository otpRepository;

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
        if (userDetails.getUserStatus().equals(2)) {
            throw new RuntimeException("User not activated!");
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
        userDetails.setAddDate(new Date());
        userDetails.setEmail(signUp.get("email"));
        userDetails.setUserStatus(2);
        userDetails.setOtp(new Random().nextInt(999999 - 100000 + 1) + 100000);
        userRepository.save(userDetails);
        String coded = "https://sudhanshu.pro/api/v1/activate?verify=" + Base64.getEncoder().encodeToString(userDetails.getOtp().toString().getBytes());
        String body = "Hi "+ userDetails.getUserId() + ", please " + "<a href=\"" + coded + "\">Click Here</a> to activate your account. Happy Tasking :)";
        String subject = "Account Verification - Task Mgmt";
        EmailPojo email = new EmailPojo(userDetails.getEmail(), body, subject, null);
        String status = emailService.sendSimpleMail(email);
        System.out.println("Email Sent Status:{}"+status);
    }

    @Override
    public void generateOTP(String userId) {
        String decodedUserId = new String(Base64.getDecoder().decode(userId), StandardCharsets.UTF_8);
        UserDetails ud = userRepository.findByUserId(decodedUserId);
        if (ud == null) {
            throw new RuntimeException("Invalid userId or token already exists!");
        }
        if (ud.getOtp() != null) {
            OtpStore otp = otpRepository.findByOtpId(ud.getOtp());
            long prev = Long.parseLong(new String(Base64.getDecoder().decode(otp.getOtpFull()), StandardCharsets.UTF_8).split(":")[1]);
            if (prev > System.currentTimeMillis()) {
                throw new RuntimeException("OTP already sent!");
            }
        }
        long valid = System.currentTimeMillis() + (1000 * 10 * 60); //Token valid for 10 minutes
        String otp = Base64.getEncoder().encodeToString((decodedUserId + ":" + valid).getBytes());
        OtpStore newOtp = new OtpStore();
        newOtp.setOtpId(new Random().nextInt(999999 - 100000 + 1) + 100000);
        newOtp.setOtpFull(otp);
        otpRepository.save(newOtp);
        ud.setOtp(newOtp.getOtpId());
        userRepository.save(ud);
        String body = "Your OTP for resetting the password is: " + newOtp.getOtpId() + " . Valid for 10 minutes only!";
        String subject = "Reset Password - Task Mgmt";
        EmailPojo email = new EmailPojo(ud.getEmail(), body, subject, null);
        String status = emailService.sendSimpleMail(email);
        System.out.println("Email Sent Status:{}"+status);
    }

    @Override
    public void validateOTP(Map<String, String> validateBody) {
        String otp = validateBody.get("otp");
        OtpStore store = otpRepository.findByOtpId(Integer.valueOf(otp));
        if (store == null) {
            throw new RuntimeException("Invalid OTP entered!");
        }
        String decoded = new String(Base64.getDecoder().decode(store.getOtpFull()), StandardCharsets.UTF_8);
        String userId = decoded.split(":")[0];
        UserDetails ud = userRepository.findByUserIdIgnoreCase(userId);
        if (ud == null) {
            throw new RuntimeException("Invalid username!");
        }
        if (!store.getOtpId().equals(ud.getOtp())) {
            throw new RuntimeException("Invalid OTP entered!");
        }
        long validity = Long.parseLong(decoded.split(":")[1]);
        if (validity < System.currentTimeMillis()) {
            otpRepository.delete(store);
            throw new RuntimeException("OTP Expired!");
        }
        String newPass = new String(Base64.getDecoder().decode(validateBody.get("password")), StandardCharsets.UTF_8);
        ud.setOtp(null);
        ud.setPassword(Base64.getEncoder().encodeToString(newPass.getBytes()));
        ud.setLastPwdChangeDate(new Date());
        userRepository.save(ud);
        otpRepository.delete(store);
    }

    @Override
    public String activateUser(String verify, HttpServletResponse response) {
        Integer otp = Integer.valueOf(new String(Base64.getDecoder().decode(verify.getBytes()), StandardCharsets.UTF_8));
        UserDetails ud = userRepository.findByOtp(otp);
        if (ud == null) {
            return "Invalid activation link!";
        }
        response.setContentType("text/html");
        ud.setUserStatus(3);
        ud.setOtp(null);
        userRepository.save(ud);
        return "<HTML><HEAD><TITLE>Verification Success</TITLE></HEAD><BODY>Your account has been verified successfully! Click here to " + "<a href=\"https://sudhanshu.pro\">Login</a></BODY></HTML>";
    }

}
