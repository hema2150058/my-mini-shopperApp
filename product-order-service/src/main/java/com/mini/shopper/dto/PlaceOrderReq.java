package com.mini.shopper.dto;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class PlaceOrderReq {

	private String userId;
	
	private String billingName;
	
	private String billingphoneNum;
	
	private String billingAddress;
	
	//private String deliveryType;

}