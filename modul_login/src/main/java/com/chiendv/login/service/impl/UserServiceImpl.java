package com.chiendv.login.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.chiendv.login.entity.Role;
import com.chiendv.login.entity.User;
import com.chiendv.login.entity.UserRole;
import com.chiendv.login.exception.NotFoundException;
import com.chiendv.login.repository.RoleRepository;
import com.chiendv.login.repository.UserRepository;
import com.chiendv.login.repository.UserRoleRepository;
import com.chiendv.login.service.UserService;
import com.chiendv.login.util.Common;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private UserRoleRepository userRoleRepository;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
			UserRoleRepository userRoleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.userRoleRepository = userRoleRepository;
	}

	@Override
	public User findByUsername(String username) {
		User user = userRepository.findByUsername(username);
		if(user != null) {
			return user;
		}
		throw new NotFoundException("Không tồn tại username: "+username);
	}

	@Override
	public List<User> findAll() {
		List<User> users = userRepository.findAll();
		if(users != null) {
			return users;
		}
		throw new NotFoundException("Không có user nào");
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String create(User user) {
		String result = "";
		try {
			Integer user_id = userRepository.save(user).getId();
			Role role = roleRepository.findByRoleName(Common.RoleType.ROLE_USER);
			if (role != null && user_id != null) {
				UserRole userRole = new UserRole();
				userRole.setRole(role);
				userRole.setUser(user);
				if (userRoleRepository.save(userRole) != null) {
					result = "Add user thành công";
				}
			}
		} catch (Exception e) {
			result = "Add user thất bại công";
		}
		return result;
	}

}
