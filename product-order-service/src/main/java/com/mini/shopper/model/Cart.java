package com.mini.shopper.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cartId;

	private String userId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="productId")
	private Product productId;

	private int quantity;
	
	private double price;
}
