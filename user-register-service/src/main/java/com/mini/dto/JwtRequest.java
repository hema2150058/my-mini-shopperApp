package com.mini.dto;

import lombok.Data;

@Data
public class JwtRequest {

	private String emailId;
    private String userPassword;
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
    
    
}
