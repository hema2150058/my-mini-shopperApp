package com.mini.shopper.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mini.shopper.model.Order;

public interface OrderRepo extends JpaRepository<Order, Long> {

	List<Order> findByOrderId(int orderId);

	List<Order> findByOrderNumber(Long orderNumber);
	
	List<Order> findAllByUserId(String userId);
		
	List<Order> findByOrderStatus(String orderStatus);
	
	Order findOrderByOrderNumber(Long orderNumber);
	Order findOrderByOrderId(int orderId);
}
