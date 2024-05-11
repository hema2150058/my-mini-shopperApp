package com.mini.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mini.model.Role;

@Repository
public interface RoleRepo extends JpaRepository<Role, String> {
	
	Role findByRoleName(String roleName);
}
