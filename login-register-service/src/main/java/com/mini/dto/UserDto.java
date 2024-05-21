package com.mini.dto;

import java.sql.Timestamp;
import java.util.Set;

import com.mini.model.Address;
import com.mini.model.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {

	private String userName;
    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private String userPassword;
	private Timestamp createdDate;
	private Address address;
    
	public UserDto(String userName, String userFirstName, String userLastName, String userEmail, Address address)
	{
		this.userName = userName;
		this.userFirstName = userFirstName;
		this.userLastName = userLastName;
		this.userEmail = userEmail;
		this.address = address;
	}
}
