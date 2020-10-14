package com.chiendv.login.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "tbl_user", //uniqueConstraints nghĩa là giá trị trong 1 cot là duy nhất
        uniqueConstraints = { @UniqueConstraint(name = "user_uk", columnNames = "user_internal_id") })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_internal_id", length = 10)
    private Integer id;
    
    @Column(name = "username", nullable = false, length = 15)
    private String username;
    @Column(name = "password", nullable = false, length = 32)
    private String password;
    
	public User(Integer id, String username, String password) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
	}
	
	public User() {
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
