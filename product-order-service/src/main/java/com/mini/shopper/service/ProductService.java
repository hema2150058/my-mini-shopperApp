package com.mini.shopper.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mini.shopper.model.Product;
import com.mini.shopper.repo.ProductRepo;

@Service
public class ProductService {
	
	@Autowired
	ProductRepo productRepo;
	
	public List<Product> getAllProducts() {
		return productRepo.findAll();
		
	}
	
	public Product createProduct(Product newProduct) {
		return productRepo.save(newProduct);

	}

	public Product getProductById(int productId) {
		return productRepo.findByProductId(productId);
	}

}
