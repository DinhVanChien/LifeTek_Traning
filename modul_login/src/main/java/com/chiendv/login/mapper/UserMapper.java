package com.chiendv.login.mapper;

import com.chiendv.login.dto.UserDTO;
import com.chiendv.login.entity.User;

public class UserMapper {
	public User userDtoConvertUser(UserDTO userDTO) {
		User user = new User();
		//user.setId(userDTO.getId());
		user.setUsername(userDTO.getUsername());
		user.setPassword(userDTO.getPassword());
		return user;
	}
}
