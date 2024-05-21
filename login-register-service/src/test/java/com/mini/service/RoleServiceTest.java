package com.mini.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mini.model.Role;
import com.mini.repo.RoleRepo;

@ExtendWith(SpringExtension.class)
class RoleServiceTest {

	@Mock
    private RoleRepo roleRepo;
	
	@InjectMocks
	private RoleService roleService;
	
	@BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
	
	@Test
	void testCreateNewRole() {
		Role role= new Role();
		role.setRoleName("user");
		when(roleRepo.save(any(Role.class))).thenReturn(role);
		Role createdRole = roleService.createNewRole(role);
		assertEquals("user", createdRole.getRoleName());
		verify(roleRepo, times(1)).save(any(Role.class));
	}
}
