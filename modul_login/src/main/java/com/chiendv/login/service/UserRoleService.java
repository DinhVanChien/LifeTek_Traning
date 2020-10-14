package com.chiendv.login.service;

import java.util.List;

public interface UserRoleService {
	List<String> findRoleNamesByUserId(int id);
}
