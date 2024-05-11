package com.mini.shopper.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mini.shopper.model.Order;
import com.mini.shopper.model.OrderedProduct;
import com.mini.shopper.model.Product;

public interface OrderedProductRepo extends JpaRepository<OrderedProduct, Long>{

	List<OrderedProduct> findByOrderId(Order orderId);
	OrderedProduct findProductByOrderId(Order orderId);
	//OrderedProduct findByOrderNumber(Order orderNumber);
	//OrderedProduct findByOrderedProductsId
}
