package com.mini.shopper.dto;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemsResponse {
	
	private int productId;

	private String productName;

	private String productDesc;

	private int quantity;

	private double price;

		
}

