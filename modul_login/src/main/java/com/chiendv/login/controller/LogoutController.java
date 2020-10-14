package com.chiendv.login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ChienDV
 * Class logout
 */
@RestController
public class LogoutController {
	
	private RedisTemplate<String, String> redisTemplate;
	
	@Autowired
	public LogoutController(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
	@RequestMapping("/logout")
	public String logoutPage(@RequestHeader("Authorization") String token) {
		if (token != null) {
			redisTemplate.delete("token");
			return "bạn đã logout thành công";
		}
		return "bạn đã logout thất bại";
	}
}
