package com.mini.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mini.model.Role;
import com.mini.repo.RoleRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RoleService {

	@Autowired
	private RoleRepo rolerepo;
	
	public Role createNewRole(Role role) {
		log.info("Roles created and added to the database");
		return rolerepo.save(role);
	}
}
