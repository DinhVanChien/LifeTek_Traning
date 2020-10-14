package com.chiendv.login.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chiendv.login.dto.UserDTO;
import com.chiendv.login.entity.User;
import com.chiendv.login.mapper.UserMapper;
import com.chiendv.login.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {
	
	private UserService userService;
	private PasswordEncoder passwordEncoder;
	private RedisTemplate<String, String> redisTemplate;
	
	private static final Logger logger = Logger.getLogger(UserController.class);
	
	@Autowired
	public UserController(UserService userService, PasswordEncoder passwordEncoder,
			RedisTemplate<String, String> redisTemplate) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
		this.redisTemplate = redisTemplate;
	}
	
	@GetMapping
	public List<UserDTO> findAll() {
		List<User> users = userService.findAll();
		try {
			List<UserDTO> userDTOs = new ArrayList<UserDTO>();
			for(User u : users) {
				userDTOs.add(new UserDTO(u));
			}
			return userDTOs;
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return null;
	}
	
	@PostMapping
	public String create(@Valid @RequestBody UserDTO userDTO) {
		UserMapper userMapper = new UserMapper();
		String result = "";
		try {
			if(userDTO != null) {
				User user = userMapper.userDtoConvertUser(userDTO);
				//mã hóa mật khẩu người dùng khi lưu vào csdl
				user.setPassword(passwordEncoder.encode(user.getPassword()));
				result = userService.create(user);
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return result;
	}
	
}
