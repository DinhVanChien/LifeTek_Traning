/**
 * 
 */
package com.chiendv.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chiendv.login.entity.Role;
import com.chiendv.login.util.Common.RoleType;

/**
 * @author ChienDV
 * Class thao tác với DB của object Role
 */

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
	Role findByRoleName(RoleType roleType);
}
