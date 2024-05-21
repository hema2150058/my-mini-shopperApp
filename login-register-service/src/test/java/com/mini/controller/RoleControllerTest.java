package com.mini.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mini.model.Role;
import com.mini.repo.RoleRepo;
import com.mini.service.RoleService;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
 

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RoleControllerTest.TestConfig.class)
@AutoConfigureMockMvc
class RoleControllerTest {
	
	@Mock
    private RoleService roleService;
	
	@MockBean
	private RoleRepo roleRepo;
 
    @InjectMocks
    private RoleController roleController;
 
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    
    @Configuration
    @Import(RoleController.class)
    static class TestConfig {
        @Bean
        public RoleService roleService() {
            return mock(RoleService.class);
        }
    }
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(roleController).build();
        objectMapper = new ObjectMapper();
    }
    
    @Test
    void testCreateNewRole_Success() throws Exception {
        Role role = new Role();
        role.setRoleName("ROLE_USER");
 
        when(roleService.createNewRole(any(Role.class))).thenReturn(role);
 
        mockMvc.perform(MockMvcRequestBuilders.post("/createNewRole")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(role)))
                .andExpect(MockMvcResultMatchers.status().isOk());
 
        verify(roleService, times(1)).createNewRole(any(Role.class));
    }
}
