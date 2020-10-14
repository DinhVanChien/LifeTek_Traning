package com.chiendv.login.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chiendv.login.repository.UserRoleRepository;
import com.chiendv.login.service.UserRoleService;

@Service
public class UserRoleServiceImpl implements UserRoleService {
	
	private UserRoleRepository userRoleRepository;
	
	@Autowired
	public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
		this.userRoleRepository = userRoleRepository;
	}
	
	@Override
	public List<String> findRoleNamesByUserId(int id) {
		return userRoleRepository.findRoleNamesByUserId(id);
	}

}
