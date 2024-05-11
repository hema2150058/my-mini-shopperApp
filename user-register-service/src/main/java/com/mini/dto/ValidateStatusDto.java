package com.mini.dto;

import org.springframework.stereotype.Component;

import lombok.*;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidateStatusDto {
	
	private boolean status;

}