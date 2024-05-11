package com.mini.shopper.dto;

import org.springframework.stereotype.Component;

import lombok.*;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResDto {
	
		//for getting cart res when there are already Items in cart
		private int cartId;

		private String userId;
		
		private int productId;

		private int quantity;
		
		private double price;


}
