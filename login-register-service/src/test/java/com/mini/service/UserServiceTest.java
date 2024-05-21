package com.mini.service;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mini.model.Address;
import com.mini.model.User;
import com.mini.repo.AddressRepo;
import com.mini.repo.RoleRepo;
import com.mini.repo.UserRepo;

@ExtendWith(SpringExtension.class)
class UserServiceTest {
	
	@Mock
	private UserRepo userRepo;
	
	@Mock
	private RoleRepo roleRepo;
	
	@Mock
	private AddressRepo addressRepo;
	
	@InjectMocks
	private UserService userService;
	
	@BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
	
	@Test
    void testGetAllCustomerData_Success() {
        User user1 = new User();
        user1.setUserName("user1");
        User user2 = new User();
        user2.setUserName("user2");
 
        when(userRepo.findByRolesRoleName("CUSTOMER")).thenReturn(Arrays.asList(user1, user2));
 
        List<User> result = userService.getAllCustomerData();
 
        assertEquals(2, result.size());
        verify(userRepo, times(2)).findByRolesRoleName("CUSTOMER");
    }
 
    @Test
    void testGetAllCustomerData_NoData() {
        when(userRepo.findByRolesRoleName("CUSTOMER")).thenReturn(null);
 
        assertThrows(NullPointerException.class, () -> {
            userService.getAllCustomerData();
        });
 
        verify(userRepo, times(1)).findByRolesRoleName("CUSTOMER");
    }
 
    @Test
    void testUpdateUserAddress_Success() throws Exception {
        User user = new User();
        user.setUserName("user1");
        Address address = new Address();
        user.setAddress(address);
 
        Address updatedAddress = new Address();
        updatedAddress.setAddressLine("New Line");
        updatedAddress.setStreet("New Street");
        updatedAddress.setCity("New City");
        updatedAddress.setState("New State");
        updatedAddress.setPincode(123456);
 
        when(userRepo.findByUserName("user1")).thenReturn(user);
 
        userService.updateUserAddress("user1", updatedAddress);
 
        assertEquals("New Line", user.getAddress().getAddressLine());
        assertEquals("New Street", user.getAddress().getStreet());
        assertEquals("New City", user.getAddress().getCity());
        assertEquals("New State", user.getAddress().getState());
        assertEquals(123456, user.getAddress().getPincode());
 
        verify(userRepo, times(1)).findByUserName("user1");
        verify(userRepo, times(1)).save(user);
    }
 
    @Test
    void testUpdateUserAddress_UserNotFound() {
        when(userRepo.findByUserName("user1")).thenReturn(null);
 
        assertThrows(NullPointerException.class, () -> userService.updateUserAddress("user1", new Address()));
 
        verify(userRepo, times(1)).findByUserName("user1");
        verify(userRepo, never()).save(any(User.class));
    }

}
