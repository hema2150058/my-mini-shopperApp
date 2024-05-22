package com.mini.shopper.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mini.shopper.dto.OrderDetailsRes;
import com.mini.shopper.dto.OrderedProductDetails;
import com.mini.shopper.dto.PlaceOrderReq;
import com.mini.shopper.dto.PlaceOrderRes;
import com.mini.shopper.model.Billing;
import com.mini.shopper.model.Cart;
import com.mini.shopper.model.Order;
import com.mini.shopper.model.OrderedProduct;
import com.mini.shopper.model.Product;
import com.mini.shopper.repo.BillingRepo;
import com.mini.shopper.repo.CartRepo;
import com.mini.shopper.repo.OrderRepo;
import com.mini.shopper.repo.OrderedProductRepo;
import com.mini.shopper.repo.ProductRepo;

@Service
@Slf4j
public class OrderService {

	@Autowired
	CartRepo cartRepo;
	
	@Autowired
	OrderedProductRepo orderProductRepo;
	
	@Autowired
	BillingRepo billingRepo;
	
	@Autowired
	ProductRepo productRepo;
	
	@Autowired
	OrderRepo orderRepo;
	
	@Transactional
	public PlaceOrderRes placeOrder(PlaceOrderReq placeorderreq) {
		List<Cart> cart = cartRepo.findByUserId(placeorderreq.getUserId());

		Billing billing = new Billing();
		billing.setBillingName(placeorderreq.getBillingName());
		billing.setBillingphoneNum(placeorderreq.getBillingphoneNum());
		billing.setBillingAddress(placeorderreq.getBillingAddress());
		Billing savedBilling = billingRepo.save(billing);

		long orderNumber=(long)(Math.random()*100000);
		
		Order newOrder = new Order();
		newOrder.setBilling(savedBilling);
		newOrder.setOrderDate(LocalDate.now());
		newOrder.setOrderNumber(orderNumber);
		newOrder.setUserId(placeorderreq.getUserId());
		newOrder.setOrderStatus("Pending");
		
		double totalPrice=0;
		
		for (Cart i : cart) {
			totalPrice += (i.getProductId().getPrice())* (i.getQuantity());
		}
		
		log.info(""+totalPrice);
		newOrder.setTotalPrice(totalPrice);
		
		log.info(""+newOrder.getTotalPrice());
		orderRepo.save(newOrder);
		
		for (Cart i : cart) {
			OrderedProduct orderedProduct = new OrderedProduct();
			orderedProduct.setOrderId(newOrder);
			orderedProduct.setUserId(placeorderreq.getUserId());
			orderedProduct.setProductId(i.getProductId());
			orderedProduct.setPrice(i.getProductId().getPrice());
			orderedProduct.setQuantity(i.getQuantity());
			orderProductRepo.save(orderedProduct);
		}
		
		PlaceOrderRes placeorderres=new PlaceOrderRes();
		placeorderres.setCustomerName(placeorderreq.getBillingName());
		placeorderres.setOrderDate(newOrder.getOrderDate());
		placeorderres.setOrderNumber(newOrder.getOrderNumber());
		placeorderres.setTotalPrice(newOrder.getTotalPrice());
		placeorderres.setOrderStatus(newOrder.getOrderStatus());
		
		cartRepo.deleteAllByUserId(placeorderreq.getUserId());
		
		return placeorderres; //user
	}
	
	public OrderDetailsRes getOrderDetails(Long orderNumber) {
		
		List<Order> order = orderRepo.findByOrderNumber(orderNumber);
		List<OrderedProduct> orderedProducts = orderProductRepo.findByOrderId(order.get(0));
		
		OrderDetailsRes orderDetails =new OrderDetailsRes();
		
		orderDetails.setCustomerName(order.get(0).getBilling().getBillingName());
		orderDetails.setOrderDate(order.get(0).getOrderDate());
		orderDetails.setOrderNumber(orderNumber);
		orderDetails.setTotalPrice(order.get(0).getTotalPrice());
		orderDetails.setOrderStatus(order.get(0).getOrderStatus());
		
		List<OrderedProductDetails> orderedProductsRes=new ArrayList<>();
		
		for (OrderedProduct i : orderedProducts) {	
			OrderedProductDetails product=new OrderedProductDetails();
			product.setProductId(i.getProductId().getProductId());
			product.setPrice(i.getPrice());
			product.setProductName(i.getProductId().getProductName());
			product.setProductDesc(i.getProductId().getProductDesc());
			product.setImageUrl(i.getProductId().getImageUrl());
			product.setQuantity(i.getQuantity());
			orderedProductsRes.add(product);
			
		}
		orderDetails.setProductList(orderedProductsRes);
		
		return orderDetails;  //user
	}
	
	public List<OrderDetailsRes> getPurchaseHistory(String userId) {
		
		List<Order> orders = orderRepo.findAllByUserId(userId);
		log.info(""+orders);
		
	    List<OrderDetailsRes> purchaseHistory = new ArrayList<>();
	    
	    for (Order order : orders) {
	    	OrderDetailsRes orderDetails = this.getOrderDetails(order.getOrderNumber());
	        purchaseHistory.add(orderDetails);
	    }
	    
	    return purchaseHistory; //user
	}
	
	public List<OrderDetailsRes> getAllOrders(){
		
		List<Order> orders = orderRepo.findAll();
		List<OrderDetailsRes> allOrdersHistory = new ArrayList<>();
		
		for(Order order: orders) {
			OrderDetailsRes orderDetails = this.getOrderDetails(order.getOrderNumber());
			allOrdersHistory.add(orderDetails);
		}
		
		return allOrdersHistory; //admin gets all orders from all users
	}
	
	public List<OrderDetailsRes> getAllPendingOrders(String orderStatus){
		List<Order> orders = orderRepo.findByOrderStatus(orderStatus);
		
		List<OrderDetailsRes> allPendingOrders = new ArrayList<>();
		
		for(Order order: orders) {
			OrderDetailsRes orderDetails = this.getOrderDetails(order.getOrderNumber());
			allPendingOrders.add(orderDetails);
		}
		
		//by findbyOrderStatus; admin
		return allPendingOrders;
	}

	public ResponseEntity<String> changeOrderStatusToSuccess(Long orderNumber){
		
		List<Order> order = orderRepo.findByOrderNumber(orderNumber);
		log.info("orderId is: "+ order.get(0).getOrderId());
		
		List<OrderedProduct> orderpro = orderProductRepo.findByOrderId(order.get(0));
		log.info(""+orderpro);
		ResponseEntity<String> res = null;
		for(OrderedProduct i : orderpro) {
			Product product = i.getProductId();
			if(i.getProductId().getStockQuantity()>i.getQuantity() ) {
			product.setStockQuantity(i.getProductId().getStockQuantity()-i.getQuantity());
			productRepo.save(product);
			order.get(0).setOrderStatus("Success");
			orderRepo.save(order.get(0));
			res = ResponseEntity.ok("Order is accepted successfully");
			}
			else {
			res= ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Ordered Prodouct quantity is more than the stock quantity");
			}
		}
		return res;
	}

	public ResponseEntity<String> changeOrderStatusToReview(Long orderNumber){
			
			Order order = orderRepo.findOrderByOrderNumber(orderNumber);
			
			if(!"Success".equals(order.getOrderStatus())) {
			order.setOrderStatus("Need reviewing");
			orderRepo.save(order);
			return ResponseEntity.ok("Order status changed to review");
			}
			return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
					.body("Can't change the order status. Its already a successful order");
			//by findbyOrderNumber; admin
		}
	
	public ResponseEntity<String> changeOrderStatusToRejected(Long orderNumber){
		
		Order order = orderRepo.findOrderByOrderNumber(orderNumber);
		
		if(!"Success".equals(order.getOrderStatus())) {
		order.setOrderStatus("Rejected");
		orderRepo.save(order);
		return ResponseEntity.ok("Order Status changed to rejected");
		}
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
				.body("Can't change the order status. Its already a successful order");
		//admin
	}
	
}
