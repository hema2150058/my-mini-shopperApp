package com.mini.shopper.exception;

public class OrderNotFoundException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	private OrderNotFoundException(String msg) {
		super(msg);
	}
	
}
