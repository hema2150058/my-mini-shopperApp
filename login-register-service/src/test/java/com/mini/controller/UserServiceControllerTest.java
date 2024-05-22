package com.mini.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mini.model.Address;
import com.mini.model.Role;
import com.mini.model.User;
import com.mini.repo.AddressRepo;
import com.mini.repo.RoleRepo;
import com.mini.repo.UserRepo;
import com.mini.service.LRService;
import com.mini.service.RoleService;
import com.mini.service.UserService;
import com.mini.util.JwtUtil;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
 
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { UserServiceController.class, LRService.class, RoleService.class, UserService.class})
@AutoConfigureMockMvc
class UserServiceControllerTest {
 
    @MockBean
    private LRService lrService;
 
    @MockBean
    private UserService userService;
    
    @MockBean
    private RoleService roleService;
 
    @MockBean
    private UserRepo userRepo;
    
    @MockBean
    private RoleRepo roleRepo;
    
    @MockBean
    private AddressRepo addressRepo;
 
    @MockBean
    private JwtUtil jwtUtil;
 
    @Autowired
    private UserServiceController userServiceController;
    
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
 
    @Configuration
    @Import(UserServiceController.class)
    static class TestConfig {
        @Bean
        public LRService lrService() {
            return mock(LRService.class);
        }
 
        @Bean
        public UserService userService() {
            return mock(UserService.class);
        }
        
        @Bean
        public UserRepo userRepo() {
            return mock(UserRepo.class);
        }
 
        @Bean
        public JwtUtil jwtUtil() {
            return mock(JwtUtil.class);
        }
    }
 
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userServiceController).build();
        objectMapper = new ObjectMapper();
    }
 
    @Test
    void testGetAllUserDetails_Success() throws Exception {
        List<User> usersList = new ArrayList<>();
        usersList.add(new User(1,"user1@example.com", "FirstName1", "LastName1", "user1","password1", new Timestamp(0),Collections.singleton(new Role()), new Address()));
        usersList.add(new User(2,"user2@example.com", "FirstName2", "LastName2", "user2", "password2", new Timestamp(0), Collections.singleton(new Role()),new Address()));
 
        when(userService.getAllCustomerData()).thenReturn(usersList);
 
        mockMvc.perform(MockMvcRequestBuilders.get("/getAllCustomersData")
                .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
 
        verify(userService, times(1)).getAllCustomerData();
    }
    
    @Test
    void testGetUserDetails_Success() throws Exception {
        User user = new User(1,"user1@example.com", "FirstName", "LastName", "user1","password1", new Timestamp(0),Collections.singleton(new Role()), new Address());
        when(userRepo.findByUserName(anyString())).thenReturn(user);
 
        mockMvc.perform(get("/getCustomerDetails/user1")
                .header("Authorization", "Bearer token")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("user1"));
 
        verify(userRepo, times(1)).findByUserName(anyString());
    }
    
    @Test
    void testGetUserDetails_NotFound() throws Exception {
        when(userRepo.findByUserName(anyString())).thenReturn(null);
 
        mockMvc.perform(get("/getCustomerDetails/user1")
                .header("Authorization", "Bearer token")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
 
        verify(userRepo, times(1)).findByUserName(anyString());
    }
 
    @Test
    void testUpdateAddress_Success() throws Exception {
        Address address = new Address();
        address.setAddressLine("New Address Line");
        address.setCity("New City");
        address.setPincode(123456);
 
        doNothing().when(userService).updateUserAddress(anyString(), any(Address.class));
 
        mockMvc.perform(put("/updateAddress/user1")
                .header("Authorization", "Bearer token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isOk())
                .andExpect(content().string("Address updated Successfully"));
        verify(userService, times(1)).updateUserAddress(anyString(), any(Address.class));
    }
    
    @Test
    void testUpdateAddress_Failure() throws Exception {
        Address address = new Address();
        address.setAddressLine("New Address Line");
        address.setCity("New City");
        address.setPincode(123456);
 
        doThrow(new Exception("Update failed")).when(userService).updateUserAddress(anyString(), any(Address.class));
 
        mockMvc.perform(put("/updateAddress/user1")
                .header("Authorization", "Bearer token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to update Address: Update failed"));
        verify(userService, times(1)).updateUserAddress(anyString(), any(Address.class));
    }

}
