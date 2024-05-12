package com.mini.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.mini.dto.UserDto;
import com.mini.model.Address;
import com.mini.model.User;
import com.mini.repo.UserRepo;
import com.mini.service.LRService;
import com.mini.service.UserService;
import com.mini.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class UserServiceController {

	@Autowired
	private LRService lrService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@GetMapping(path=  "/getAllCustomersData")
    public ResponseEntity<List<User>> getAllUserDetails(@RequestHeader(name = "Authorization") String tokenDup) {

        List<User> usersList = userService.getAllCustomerData();
       return new ResponseEntity<>(usersList,HttpStatus.OK);

    }

	@GetMapping(path = "/getCustomerDetails/{userName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getUserDetails(@RequestHeader(name = "Authorization") String tokenDup, @PathVariable String userName) {
		
		User users = userRepo.findByUserName(userName);
		if(users == null) {
			log.error("No user found with username : " +userName);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}else {
		return new ResponseEntity<>(new UserDto(users.getUserEmail(), users.getUserFirstName(), users.getUserLastName(), users.getUserName(), users.getAddress()),HttpStatus.OK);
		}
		
	}
	
	@PutMapping("/updateAddress/{userName}")
	public ResponseEntity<?> updateAddress(@RequestHeader(name = "Authorization") String tokenDup, @PathVariable String userName, @RequestBody Address updatedAddress) {
		try {
			userService.updateUserAddress(userName,updatedAddress);
			return ResponseEntity.ok("Address updated Successfully");
		} catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update Address: "+ e.getMessage());
		}
	}
	
}
