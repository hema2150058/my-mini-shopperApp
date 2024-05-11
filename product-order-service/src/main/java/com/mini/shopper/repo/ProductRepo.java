package com.mini.shopper.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mini.shopper.model.Product;

public interface ProductRepo extends JpaRepository<Product, Long> {

	Product findByProductId(int productId);

}
