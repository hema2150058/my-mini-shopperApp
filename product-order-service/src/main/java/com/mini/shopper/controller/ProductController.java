package com.mini.shopper.controller;

import java.util.List;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mini.shopper.dto.ProductDto;
import com.mini.shopper.exception.ProductNotFoundException;
import com.mini.shopper.model.Product;
import com.mini.shopper.service.ProductService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

	@Autowired
	ProductService productService;
	
	@GetMapping("/getAllProducts")
	public ResponseEntity<List<Product>> getAllProdoucts(){
		return new ResponseEntity<>(productService.getAllProducts(),HttpStatus.OK);
	}
	
	@PostMapping("/addProducts")
	public ResponseEntity<Product> createProduct(@RequestBody Product product) throws Exception{
		return new ResponseEntity<>(productService.createProduct(product),HttpStatus.OK);
	}
	
	@GetMapping("/getProductById/{productId}")
	public ResponseEntity<Product> getProductById(@PathVariable int productId) throws ProductNotFoundException{
		return new ResponseEntity<>(productService.getProductById(productId),HttpStatus.OK);
	}
	
}
