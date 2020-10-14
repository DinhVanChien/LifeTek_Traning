package com.chiendv.login.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.chiendv.login.dto.UserDTO;
import com.chiendv.login.service.impl.JwtService;
import com.chiendv.login.util.Constant;

@RestController
public class LoginController {

	private static final Logger logger = Logger.getLogger(LoginController.class);
	
	private JwtService jwtService;
	private RedisTemplate<String, String> redisTemplate;
	
	@Autowired
	public LoginController(JwtService jwtService, RedisTemplate<String, String> redisTemplate) {
		this.jwtService = jwtService;
		this.redisTemplate = redisTemplate;
	}

	@PostMapping(value = Constant.LOGIN_ANNOTATION)
	public ResponseEntity<String> getAllUser(@Valid @RequestBody UserDTO user, HttpServletRequest httpRequest) {
		String token = "";
		try {
			token = jwtService.generateTokenLogin(user.getUsername());
			if(token != null) {
				redisTemplate.opsForValue().set("token", token);
			}
		} catch (Exception ex) {
			logger.info(ex.getMessage());
		}
		return ResponseEntity.ok(token);
	}
}
