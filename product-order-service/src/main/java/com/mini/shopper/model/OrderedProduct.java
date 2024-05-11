package com.mini.shopper.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "OrderedProducts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderedProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int orderedProductsId;
	
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinColumn(name="productId")
	private Product productId;
	
	private double price;
	
	private int quantity;
	
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinColumn(name="orderId")
    private Order orderId;
	
	private String userId;
	

}