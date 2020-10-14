package com.chiendv.login.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.chiendv.login.entity.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
	@Query(value = "SELECT r.role_name FROM tbl_role r JOIN user_role u " +
            " ON r.role_id = u.role_id " +
            " WHERE u.user_id = ?1",
            nativeQuery = true)
	List<String> findRoleNamesByUserId(int id);
	UserRole save(UserRole userRole);
	
}
