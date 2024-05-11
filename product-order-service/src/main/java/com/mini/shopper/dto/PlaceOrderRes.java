package com.mini.shopper.dto;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceOrderRes {
		
		private long orderNumber;
		private LocalDate orderDate;
		private String customerName;
		private double totalPrice;
		private String orderStatus;
		
	}
