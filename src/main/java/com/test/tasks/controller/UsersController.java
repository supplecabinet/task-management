package com.test.tasks.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.test.tasks.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/")
public class UsersController {

	@Autowired
	private UserService userService;

	@RequestMapping(value="/auth/login",method= RequestMethod.POST) //Login API
	public void userLogin(@RequestBody Map<String,String> login, HttpServletResponse response) throws JsonProcessingException {
		userService.login(login, response);
	}
}
