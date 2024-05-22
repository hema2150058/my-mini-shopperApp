package com.mini.controller;
 
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
 
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mini.dto.JwtRequest;
import com.mini.dto.JwtResponse;
import com.mini.dto.RegisterDto;
import com.mini.dto.UserDto;
import com.mini.service.LRService;
import com.mini.util.JwtUtil;
 
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = { LoginRegisterController.class })
class LoginRegisterControllerTest {
 
    @Mock
    private LRService loginRegisterService;
 
    @Mock
    private JwtUtil jwtTokenUtil;
 
    @InjectMocks
    private LoginRegisterController controller;
 
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
 
    private UserDto userDto;
    private JwtRequest jwtRequest;
 
    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
 
        userDto = new UserDto();
        userDto.setUserName("testuser");
        userDto.setUserEmail("user@gmail.com");
        userDto.setUserPassword("testpassword");
 
        jwtRequest = new JwtRequest();
        jwtRequest.setEmailId("user@gmail.com");
        jwtRequest.setUserPassword("testpassword");
    }
 
    @Test
    void testRegisterUser() throws Exception {
        when(loginRegisterService.registerUser(any(UserDto.class))).thenReturn(new RegisterDto());
 
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        verify(loginRegisterService, times(1)).registerUser(any());
    }
 
    @Test
    void testCreateAuthorizationToken() throws Exception {
        when(loginRegisterService.loginNewUser(any(JwtRequest.class))).thenReturn(new JwtResponse());
 
        mockMvc.perform(post("/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(jwtRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        verify(loginRegisterService, times(2)).loginNewUser(any());
    }
 
    @Test
    void testValidatingAuthorizationToken() throws Exception {
        when(jwtTokenUtil.extractUsername(any(String.class))).thenReturn("testuser");
        when(jwtTokenUtil.validateToken(any(String.class), any())).thenReturn(true);
 
        mockMvc.perform(get("/validate")
                .header("Authorization", "Bearer testtoken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true));
    }
 
    @Test
    void testInvalidToken() throws Exception {
        when(jwtTokenUtil.extractUsername(any(String.class))).thenThrow(new IllegalArgumentException("Invalid token"));
 
        mockMvc.perform(get("/validate")
                .header("Authorization", "Bearer testtoken"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(false));
    }
 
    @Test
    void testHealthCheck() throws Exception {
        mockMvc.perform(get("/health-check"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }
}