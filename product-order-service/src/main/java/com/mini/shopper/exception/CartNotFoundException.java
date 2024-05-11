package com.mini.shopper.exception;

public class CartNotFoundException extends Exception{

	private static final long serialVersionUID = 1L;
	public CartNotFoundException(String msg) {
		super(msg);
	}
	
}
