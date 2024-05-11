package com.mini.shopper.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "billingtb")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Billing {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int billingId;
	private String billingName;
	private String billingphoneNum;
	private String billingAddress;
	
		

}
