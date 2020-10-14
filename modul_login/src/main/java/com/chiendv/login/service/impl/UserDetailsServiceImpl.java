package com.chiendv.login.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.chiendv.login.dto.UserDTO;
import com.chiendv.login.service.UserRoleService;
import com.chiendv.login.service.UserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private UserService userService;
	private UserRoleService userRoleService;

	@Autowired
	public UserDetailsServiceImpl(UserService userService, UserRoleService userRoleService) {
		this.userService = userService;
		this.userRoleService = userRoleService;
	}

	// Spring Security sẽ cần lấy các thông tin UserDetails hiện có để kiểm tra
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDTO userDto = new UserDTO(userService.findByUsername(username));
		if (userDto != null) {
			// [ROLE_USER, ROLE_ADMIN,..]
			List<String> roleUsers = userRoleService.findRoleNamesByUserId(userDto.getId());
			List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
			if (roleUsers != null) {
				for (String role : roleUsers) {
					// ROLE_USER, ROLE_ADMIN,..
					GrantedAuthority authority = new SimpleGrantedAuthority(role);
					grantList.add(authority);
				}
			}

			UserDetails userDetails = new User(userDto.getUsername(), userDto.getPassword(), grantList);
			return userDetails;
		} else {
			throw new UsernameNotFoundException("User " + userDto.getUsername() + " was not found in the database");
		}
	}
}
