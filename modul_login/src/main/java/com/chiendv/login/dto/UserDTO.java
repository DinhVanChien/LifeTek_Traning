package com.chiendv.login.dto;

import javax.validation.constraints.Size;

import com.chiendv.login.entity.User;

public class UserDTO {
	private Integer id;
	@Size(min =3, max = 20)
    private String username;
	@Size(min =3, max = 10)
    private String password;
    
	public UserDTO(Integer id, String username, String password) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
	}
	public UserDTO(User user) {
		super();
		this.id = user.getId();
		this.username = user.getUsername();
		this.password = user.getPassword();
	}
	public UserDTO() {
		super();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
    
    
}
