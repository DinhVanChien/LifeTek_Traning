package com.chiendv.login.util;

/**
 * @author ADMIN
 *
 */
public class Common {
	public static enum RoleType {
	    ROLE_ADMIN, ROLE_USER;
	}
	
	public static boolean isNullOrIsEmpty(String input) {
		return (!(input == null) && !input.isEmpty());
	}
}
