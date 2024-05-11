package com.mini.shopper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderedProductDetails {
	
	private int productId;
    private String productName;
    private String productDesc;
    private double price;
    private int quantity;

}
