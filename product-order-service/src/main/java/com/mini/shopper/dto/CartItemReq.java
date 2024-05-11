package com.mini.shopper.dto;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Component
public class CartItemReq {
	//for api calls "IsItemInCart and RemoveItemFromCart
	private String userId;
	
	private int productId;
	
}
