package com.mini.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mini.model.Role;
import com.mini.model.User;

@Repository
public interface UserRepo extends JpaRepository<User, String>{


	boolean existsByUserEmail(String email);
	boolean existsByUserName(String userName);
	List<User>  findByRoles(Role role);

	List<User> findByRolesRoleName(String roleName);
	User findByUserEmail(String email);
	User findByUserName(String username);
}
