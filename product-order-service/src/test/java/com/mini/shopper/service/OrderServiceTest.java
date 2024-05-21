package com.mini.shopper.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mini.shopper.dto.OrderDetailsRes;
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

@ContextConfiguration(classes = {OrderService.class, PlaceOrderReq.class})
@ExtendWith(SpringExtension.class)
class OrderServiceTest {

	@Autowired
	private PlaceOrderReq placeOrderReq;
	
	@Autowired
	private OrderService orderService;
	
	@MockBean
	private CartService cartService;
	
	@MockBean
	private BillingRepo billingRepo;
	
	@MockBean
	private CartRepo cartRepo;
	
	@MockBean
	private OrderRepo orderRepo;
	
	@MockBean
	private OrderedProductRepo orderedProdRepo;
	
	@MockBean
	private ProductRepo productRepo;
	
	private Billing billing;
    private Cart cart;
    private Order order;
    private Product product;
    private OrderedProduct orderedProduct;
	
	@BeforeEach
	void setUp() {
		 placeOrderReq = new PlaceOrderReq();
	        placeOrderReq.setUserId("user1");
	        placeOrderReq.setBillingName("John Doe");
	        placeOrderReq.setBillingphoneNum("1234567890");
	        placeOrderReq.setBillingAddress("123 Main St");
	 
	        billing = new Billing();
	        billing.setBillingName("John Doe");
	        billing.setBillingphoneNum("1234567890");
	        billing.setBillingAddress("123 Main St");
	 
	        product = new Product();
	        product.setProductId(1);
	        product.setProductName("Product 1");
	        product.setProductDesc("Description 1");
	        product.setPrice(100.0);
	        product.setStockQuantity(10);
	        
	        cart = new Cart();
	        cart.setCartId(1);
	        cart.setQuantity(2);
	        cart.setProductId(product);
	        cart.setUserId("user1");
	        cart.setPrice(100.0);
	 
	        order = new Order();
	        order.setOrderId(1);
	        order.setBilling(billing);
	        order.setOrderDate(LocalDate.now());
	        order.setOrderNumber(12345L);
	        order.setUserId("user1");
	        order.setOrderStatus("Pending");
	        order.setTotalPrice(200.0);
	        
	        orderedProduct = new OrderedProduct();
	        orderedProduct.setOrderId(order);
	        orderedProduct.setUserId("user1");
	        orderedProduct.setProductId(product);
	        orderedProduct.setPrice(100.0);
	        orderedProduct.setQuantity(2);
	    
	}
	
	@Test
    void testPlaceOrder() {
        when(cartRepo.findByUserId("user1")).thenReturn(List.of(cart));
        when(billingRepo.save(any(Billing.class))).thenReturn(billing);
        when(orderRepo.save(any(Order.class))).thenReturn(order);
        when(orderedProdRepo.save(any(OrderedProduct.class))).thenReturn(orderedProduct);
 
        PlaceOrderRes result = orderService.placeOrder(placeOrderReq);
 
        assertNotNull(result);
        assertEquals("John Doe", result.getCustomerName());
        assertEquals(200.0, result.getTotalPrice());
        assertEquals("Pending", result.getOrderStatus());
        verify(cartRepo, times(1)).deleteAllByUserId("user1");
      
    }
	
	 @Test
	    void testGetOrderDetails() {
	        when(orderRepo.findByOrderNumber(12345L)).thenReturn(List.of(order));
	        when(orderedProdRepo.findByOrderId(order)).thenReturn(List.of(orderedProduct));
	 
	        OrderDetailsRes result = orderService.getOrderDetails(12345L);
	 
	        assertNotNull(result);
	        assertEquals("John Doe", result.getCustomerName());
	        assertEquals(200.0, result.getTotalPrice());
	        assertEquals("Pending", result.getOrderStatus());
	        assertEquals(1, result.getProductList().size());
	    }
	 
	 @Test
	    void testGetPurchaseHistory() {
	        when(orderRepo.findAllByUserId("user1")).thenReturn(List.of(order));
	        when(orderRepo.findByOrderNumber(12345L)).thenReturn(List.of(order));
	        when(orderedProdRepo.findByOrderId(order)).thenReturn(List.of(orderedProduct));
	 
	        List<OrderDetailsRes> result = orderService.getPurchaseHistory("user1");
	 
	        assertNotNull(result);
	        assertFalse(result.isEmpty());
	        assertEquals(1, result.size());
	    }
	 
	 @Test
	    void testGetAllOrders() {
	        when(orderRepo.findAll()).thenReturn(List.of(order));
	        when(orderRepo.findByOrderNumber(12345L)).thenReturn(List.of(order));
	        when(orderedProdRepo.findByOrderId(order)).thenReturn(List.of(orderedProduct));
	 
	        List<OrderDetailsRes> result = orderService.getAllOrders();
	 
	        assertNotNull(result);
	        assertFalse(result.isEmpty());
	        assertEquals(1, result.size());
	    }
	 
	 @Test
	    void testGetAllPendingOrders() {
	        when(orderRepo.findByOrderStatus("Pending")).thenReturn(List.of(order));
	        when(orderRepo.findByOrderNumber(12345L)).thenReturn(List.of(order));
	        when(orderedProdRepo.findByOrderId(order)).thenReturn(List.of(orderedProduct));
	 
	        List<OrderDetailsRes> result = orderService.getAllPendingOrders("Pending");
	 
	        assertNotNull(result);
	        assertFalse(result.isEmpty());
	        assertEquals(1, result.size());
	    }
	 
	 @Test
	    void testChangeOrderStatusToSuccess() {
	        when(orderRepo.findByOrderNumber(12345L)).thenReturn(List.of(order));
	        when(orderedProdRepo.findByOrderId(order)).thenReturn(List.of(orderedProduct));
	        when(productRepo.save(any(Product.class))).thenReturn(product);
	        when(orderRepo.save(any(Order.class))).thenReturn(order);
	 
	        ResponseEntity<String> result = orderService.changeOrderStatusToSuccess(12345L);
	 
	        assertNotNull(result);
	        assertEquals(HttpStatus.OK, result.getStatusCode());
	        assertEquals("Order is accepted successfully", result.getBody());
	    }
	 
	    @Test
	    void testChangeOrderStatusToReview() {
	        when(orderRepo.findOrderByOrderNumber(12345L)).thenReturn(order);
	        when(orderRepo.save(any(Order.class))).thenReturn(order);
	 
	        ResponseEntity<String> result = orderService.changeOrderStatusToReview(12345L);
	 
	        assertNotNull(result);
	        assertEquals(HttpStatus.OK, result.getStatusCode());
	        assertEquals("Order status changed to review", result.getBody());
	    }
	 
	    @Test
	    void testChangeOrderStatusToRejected() {
	        when(orderRepo.findOrderByOrderNumber(12345L)).thenReturn(order);
	        when(orderRepo.save(any(Order.class))).thenReturn(order);
	 
	        ResponseEntity<String> result = orderService.changeOrderStatusToRejected(12345L);
	 
	        assertNotNull(result);
	        assertEquals(HttpStatus.OK, result.getStatusCode());
	        assertEquals("Order Status changed to rejected", result.getBody());
	    }	
	
}
