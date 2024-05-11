package com.mini.service;

import java.util.List;
import java.util.Set;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mini.model.Address;
import com.mini.model.Role;
import com.mini.model.User;
import com.mini.repo.UserRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
	
	@Autowired
	private UserRepo userRepo;
	
	public List<User> getAllCustomerData(){
		List<User> allcustomersData = userRepo.findByRolesRoleName("CUSTOMER");
		if(allcustomersData == null) {
			log.error("No customers data present in the database with role 'CUSTOMER'");
			throw new NullPointerException("No customers data present in the data.");
		}
		log.info("Retreiving all customer details");
		return userRepo.findByRolesRoleName("CUSTOMER");
	}
	
	public void updateUserAddress(String userName, Address updatedAddress) throws Exception {
		
		User user = userRepo.findByUserName(userName);
		if(user == null) {
			log.error("User not found with Username: "+ userName);
			throw new NullPointerException();

		}
		
		user.getAddress().setAddressLine(updatedAddress.getAddressLine());
		user.getAddress().setStreet(updatedAddress.getStreet());
		user.getAddress().setCity(updatedAddress.getCity());
		user.getAddress().setState(updatedAddress.getState());
		user.getAddress().setPincode(updatedAddress.getPincode());
		userRepo.save(user);
		log.info("Updated address added to the database");
		
	}
}
