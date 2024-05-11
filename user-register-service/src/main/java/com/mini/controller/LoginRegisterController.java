package com.mini.controller;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


import com.mini.dto.JwtRequest;
import com.mini.dto.JwtResponse;
import com.mini.dto.RegisterDto;
import com.mini.dto.UserDto;
import com.mini.dto.ValidateStatusDto;
import com.mini.exception.UserAlreadyExistException;
import com.mini.exception.UserNotFoundException;
import com.mini.model.Address;
import com.mini.model.Role;
import com.mini.model.User;
import com.mini.repo.AddressRepo;
import com.mini.repo.RoleRepo;
import com.mini.repo.UserRepo;
import com.mini.service.LRService;
import com.mini.service.RoleService;
import com.mini.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class LoginRegisterController {

	//login register and validate code

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private LRService loginRegisterService;

	@Autowired
	private JwtUtil jwtTokenUtil;

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private RoleRepo roleRepo;
	
	@Autowired
	private RoleService roleService;

	private ValidateStatusDto validatingDTO = new ValidateStatusDto();

	@PostMapping(path = "/register")
	public ResponseEntity<Object> registerUser(@RequestBody UserDto userDTO) throws UserAlreadyExistException {
		return new ResponseEntity<>(loginRegisterService.registerUser(userDTO),HttpStatus.CREATED);
	}

	@PostMapping(path= "/signin", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE} )
	public ResponseEntity<Object> createAuthorizationToken(@RequestBody JwtRequest jwtRequest) throws UserNotFoundException {
		return new ResponseEntity<>(loginRegisterService.loginNewUser(jwtRequest),HttpStatus.OK);
	}

	@GetMapping(path = "/validate", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ValidateStatusDto> validatingAuthorizationToken(
			@RequestHeader(name = "Authorization") String tokenDup) {
		String token = tokenDup.substring(7);
		try {
			UserDetails user = loginRegisterService.loadUserByUsername(jwtTokenUtil.extractUsername(token));
			if (Boolean.TRUE.equals(jwtTokenUtil.validateToken(token, user))) {
				validatingDTO.setStatus(true);
				log.info("User is validated using the token");
				return new ResponseEntity<>(validatingDTO, HttpStatus.OK);
			} else {
				log.error("The token is invalid or expired");
				throw new IllegalArgumentException("Invalid token");
			}
		} catch (Exception e) {
			validatingDTO.setStatus(false);
			return new ResponseEntity<>(validatingDTO, HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping(path = "/health-check")
	public ResponseEntity<String> healthCheck() {
		log.info("Just a sample http request checking function");
		return new ResponseEntity<>("OK", HttpStatus.OK);
	}
}



