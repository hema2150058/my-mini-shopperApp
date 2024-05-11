package com.mini.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtResponse {
	
	private String username;
	
	private String email;
	
    private String jwtAuthToken;

    private long serverCurrentTime;

    private long tokenExpirationTime;

	public JwtResponse(String username, String email, String jwtAuthToken, long serverCurrentTime,
			long tokenExpirationTime) {
		super();
		this.username = email;
		this.email = username;
		this.jwtAuthToken = jwtAuthToken;
		this.serverCurrentTime = serverCurrentTime;
		this.tokenExpirationTime = tokenExpirationTime;
	}
    
}