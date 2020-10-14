package com.chiendv.login.service;

import java.util.List;

import com.chiendv.login.entity.User;

public interface UserService {
	User findByUsername(String username);
	List<User> findAll();
	String create(User user);
}

