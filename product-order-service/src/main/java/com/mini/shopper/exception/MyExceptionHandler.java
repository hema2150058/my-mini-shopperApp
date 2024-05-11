package com.mini.shopper.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class MyExceptionHandler {

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(ProductNotFoundException.class)
	public Map<String, String> handleProductNotFoundException(ProductNotFoundException ex) {
		Map<String, String> errorMap = new HashMap<>();
		errorMap.put("ErrorMessage", ex.getMessage());
		return errorMap;
	}
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(CartNotFoundException.class)
	public Map<String, String> handleCartNotFoundException(CartNotFoundException ex) {
		Map<String, String> errorMap = new HashMap<>();
		errorMap.put("ErrorMessage", ex.getMessage());
		return errorMap;
	}
	
}

