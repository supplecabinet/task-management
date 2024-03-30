package com.test.tasks.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.tasks.exception.UnAuthorizedException;
import com.test.tasks.model.UserDetails;
import com.test.tasks.model.UserDetailsPojo;
import com.test.tasks.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/")
public class UsersController {

	@Autowired
	private UserRepository userRepository;

	@RequestMapping(value="/auth/login",method= RequestMethod.POST) //Login API
	public void userLogin(@RequestBody Map<String,String> login, HttpServletResponse response) throws JsonProcessingException {
		String username = login.get("username");
		String password = "";
		try {
			password = Arrays.toString(Base64.decodeBase64(login.get("password"))); //Decode Base64 password input
		} catch (Exception e) {
			throw new RuntimeException("Security Breach Attempted!");
		}

		UserDetails userDetails = userRepository.findByUserId(username.toLowerCase()); //Find user details
		if (userDetails == null) {
			throw new UnAuthorizedException("Invalid Username!");
		}
		String userPassword = Arrays.toString(Base64.decodeBase64(userDetails.getPassword())); //Find stored password and decrypt it
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
				Base64.encodeBase64String(
				new ObjectMapper()
						.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
						.writeValueAsString(udp).getBytes()));
	}

	
}
