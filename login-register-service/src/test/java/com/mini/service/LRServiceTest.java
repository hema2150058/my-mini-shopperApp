package com.mini.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
 
import java.util.Collections;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
 
import com.mini.dto.*;
import com.mini.exception.*;
import com.mini.model.*;
import com.mini.repo.*;
import com.mini.util.JwtUtil;
 
@ExtendWith(SpringExtension.class)
class LRServiceTest {
 
    @Mock
    private RoleRepo roleRepo;
 
    @Mock
    private AddressRepo addressRepo;
 
    @Mock
    private UserRepo userRepo;
 
    @Mock
    private JwtUtil jwtTokenUtil;
 
    @Mock
    private PasswordEncoder passwordEncoder;
 
    @InjectMocks
    private LRService lrService;
 
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @AfterEach
    void tearDown() throws Exception{
    	
    }
 
    @Test
    void testLoadUserByUsername_UserExists() {
        User user = new User();
        user.setUserEmail("test@example.com");
        user.setUserPassword("password");
        user.setRole(Collections.singleton(new Role()));
 
        when(userRepo.findByUserEmail(anyString())).thenReturn(user);
 
        UserDetails userDetails = lrService.loadUserByUsername("test@example.com");
 
        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        verify(userRepo, times(1)).findByUserEmail(anyString());
    }
 
    @Test
    void testLoadUserByUsername_UserNotExists() {
        when(userRepo.findByUserEmail(anyString())).thenReturn(null);
 
        assertThrows(UsernameNotFoundException.class, () -> lrService.loadUserByUsername("nonexistent@example.com"));
        verify(userRepo, times(1)).findByUserEmail(anyString());
    }
 
    @Test
    void testSaveUsers_Success() throws UserAlreadyExistException {
        User user = new User();
        when(userRepo.save(any(User.class))).thenReturn(user);
 
        User savedUser = lrService.saveUsers(user);
 
        assertNotNull(savedUser);
        verify(userRepo, times(1)).save(any(User.class));
    }
 
    @Test
    void testSaveUsers_Failure() {
        User user = new User();
        when(userRepo.save(any(User.class))).thenThrow(new RuntimeException());
 
        assertThrows(RuntimeException.class, () -> lrService.saveUsers(user));
        verify(userRepo, times(1)).save(any(User.class));
    }
 
    @Test
    void testExistsByUserEmail_UserExists() {
        when(userRepo.existsByUserEmail(anyString())).thenReturn(true);
 
        boolean exists = lrService.existsByUserEmail("test@example.com");
 
        assertTrue(exists);
        verify(userRepo, times(2)).existsByUserEmail(anyString());
    }
 
    @Test
    void testExistsByUserEmail_UserNotExists() {
        when(userRepo.existsByUserEmail(anyString())).thenReturn(false);
 
        boolean exists = lrService.existsByUserEmail("nonexistent@example.com");
 
        assertFalse(exists);
        verify(userRepo, times(1)).existsByUserEmail(anyString());
    }
 
    @Test
    void testExistsByUserName_UserExists() {
        when(userRepo.existsByUserName(anyString())).thenReturn(true);
 
        boolean exists = lrService.existsByUserName("username");
 
        assertTrue(exists);
        verify(userRepo, times(2)).existsByUserName(anyString());
    }
 
    @Test
    void testExistsByUserName_UserNotExists() {
        when(userRepo.existsByUserName(anyString())).thenReturn(false);
 
        boolean exists = lrService.existsByUserName("nonexistentUsername");
 
        assertFalse(exists);
        verify(userRepo, times(1)).existsByUserName(anyString());
    }
 
    @Test
    void testRegisterUser_UserAlreadyExists() {
        UserDto userDto = new UserDto();
        userDto.setUserEmail("test@example.com");
        userDto.setUserName("username");
 
        when(userRepo.existsByUserEmail(anyString())).thenReturn(true);
        when(userRepo.existsByUserName(anyString())).thenReturn(true);
 
        assertThrows(UserAlreadyExistException.class, () -> lrService.registerUser(userDto));
        verify(userRepo, times(4)).existsByUserEmail(anyString());
        verify(userRepo, times(2)).existsByUserName(anyString());
    }
 
    @Test
    void testRegisterUser_Success() throws UserAlreadyExistException {
        UserDto userDto = new UserDto();
        userDto.setUserEmail("test@example.com");
        userDto.setUserName("username");
        userDto.setAddress(new Address());
        
 
        when(userRepo.existsByUserEmail(anyString())).thenReturn(false);
        when(userRepo.existsByUserName(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepo.save(any(User.class))).thenReturn(new User());
 
        RegisterDto registerDto = lrService.registerUser(userDto);
 
        assertNotNull(registerDto);
        assertEquals("test@example.com", registerDto.getEmail());
        verify(userRepo, times(2)).existsByUserEmail(anyString());
        verify(userRepo, times(1)).existsByUserName(anyString());
        verify(userRepo, times(1)).save(any(User.class));
    }
 
    @Test
    void testLoginNewUser_UserNotFound() {
        JwtRequest jwtRequest = new JwtRequest();
        jwtRequest.setEmailId("nonexistent@example.com");
 
        when(userRepo.existsByUserEmail(anyString())).thenReturn(false);
 
        assertThrows(UserNotFoundException.class, () -> lrService.loginNewUser(jwtRequest));
        verify(userRepo, times(2)).existsByUserEmail(anyString());
    }
 
    @Test
    void testLoginNewUser_Success() throws UserNotFoundException {
        JwtRequest jwtRequest = new JwtRequest();
        jwtRequest.setEmailId("test@example.com");
        jwtRequest.setUserPassword("password");
 
        User user = new User();
        user.setUserEmail("test@example.com");
        user.setUserPassword("encodedPassword");
        user.setUserName("username");
        user.setRole(Collections.singleton(new Role()));
        
        JwtResponse jwtResponse = new JwtResponse("test@example.com","username","jwtToken", 0L, 0L);
 
        assertNotNull(jwtResponse);
        assertEquals("test@example.com", jwtResponse.getEmail());
        assertEquals("username", jwtResponse.getUsername());
        assertEquals("jwtToken", jwtResponse.getJwtAuthToken());

    }
    
}

