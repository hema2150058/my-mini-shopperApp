package com.mini.shopper.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mini.shopper.dto.OrderDetailsRes;
import com.mini.shopper.dto.PlaceOrderReq;
import com.mini.shopper.dto.PlaceOrderRes;
import com.mini.shopper.service.FileService;
import com.mini.shopper.service.OrderService;

@ContextConfiguration(classes = {OrderController.class,
        PlaceOrderReq.class})
@ExtendWith(SpringExtension.class)
class OrderControllerTest {

	@Autowired
	private OrderController orderController;
	
	@MockBean
	private OrderService orderService;
	
	@MockBean
	private FileService fileService;
	
	 private MockMvc mockMvc;
	 @BeforeEach
	    public void setUp() {
	        MockitoAnnotations.openMocks(this);
	        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
	        new ObjectMapper();
	    }
	
	@Test
    void testPlaceOrder() throws Exception {
        when(orderService.placeOrder((PlaceOrderReq) any())).thenReturn(new PlaceOrderRes());

        PlaceOrderReq placeOrderReq1 = new PlaceOrderReq();
        placeOrderReq1.setBillingAddress("42 Main St");
        placeOrderReq1.setBillingName("Billing Name");
        placeOrderReq1.setBillingphoneNum("4105551212");
        placeOrderReq1.setUserId("user");
        String content = (new ObjectMapper()).writeValueAsString(placeOrderReq1);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/placeOrder")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(orderController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"orderNumber\":0,\"orderDate\":null,\"customerName\":null,\"totalPrice\":0.0,\"orderStatus\":null}"));
    }
	
	@Test
    void testGetOrderDetails() throws Exception {
        when(orderService.getOrderDetails((Long) any())).thenReturn(new OrderDetailsRes());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/getOrderDetails/{orderNumber}", 1L);
        MockMvcBuilders.standaloneSetup(orderController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"orderNumber\":0,\"orderDate\":null,\"customerName\":null,\"totalPrice\":0.0,\"orderStatus\":null,\"productList\":null}"));
    }

    @Test
    void testGetPurchaseHistory() throws Exception {
        when(orderService.getPurchaseHistory(any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/purchaseHistory/{userId}", "user");
        MockMvcBuilders.standaloneSetup(orderController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testGetPurchaseHistory2() throws Exception {
        when(orderService.getPurchaseHistory(any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/purchaseHistory/{userId}", "user");
        getResult.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(orderController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }
    
    @Test
    void testGetAllOrders() throws Exception {
    	when(orderService.getAllOrders()).thenReturn(new ArrayList<>());
    	MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/orderHistory");
    	getResult.characterEncoding("Encoding");
    	MockMvcBuilders.standaloneSetup(orderController)
		    	.build()
		        .perform(getResult)
		        .andExpect(MockMvcResultMatchers.status().isOk())
		        .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
		        .andExpect(MockMvcResultMatchers.content().string("[]"));
    }
    
    @Test
    void testGetAllPendingOrders() throws Exception {
    	when(orderService.getAllPendingOrders("pending")).thenReturn(new ArrayList<>());
    	MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/pendingOrders");
    	getResult.characterEncoding("Encoding");
    	MockMvcBuilders.standaloneSetup(orderController)
		    	.build()
		        .perform(getResult)
		        .andExpect(MockMvcResultMatchers.status().isOk())
		        .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
		        .andExpect(MockMvcResultMatchers.content().string("[]"));
    }
    
    @Test
    void testStatusChangeToReview() throws Exception {
    	when(orderService.changeOrderStatusToReview(anyLong())).thenReturn(ResponseEntity.ok("Order status changed to review"));
    	MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.patch("/statusChangeToReview/{orderNumber}", 1L);
    	getResult.characterEncoding("Encoding");
    	MockMvcBuilders.standaloneSetup(orderController)
    			.build()
    			.perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Order status changed to review"));
 
        verify(orderService, times(1)).changeOrderStatusToReview(anyLong());
    }
    
    @Test
    void testStatusChangeToRejected() throws Exception {
    	when(orderService.changeOrderStatusToRejected(anyLong())).thenReturn(ResponseEntity.ok("Order status changed to rejected"));
    	MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.patch("/statusChangeToRejected/{orderNumber}", 1L);
    	getResult.characterEncoding("Encoding");
    	MockMvcBuilders.standaloneSetup(orderController)
    			.build()
    			.perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Order status changed to rejected"));
 
        verify(orderService, times(1)).changeOrderStatusToRejected(anyLong());
    }
    
    @Test
    void testStatusChangeToSuccess() throws Exception {
    	when(orderService.changeOrderStatusToSuccess(anyLong())).thenReturn(ResponseEntity.ok("Order status changed to success"));
    	MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.patch("/statusChangeToSuccess/{orderNumber}", 1L);
    	getResult.characterEncoding("Encoding");
    	MockMvcBuilders.standaloneSetup(orderController)
    			.build()
    			.perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Order status changed to success"));
 
        verify(orderService, times(1)).changeOrderStatusToSuccess(anyLong());
    }
    
    @Test
    void testUploadFileToOrder() throws Exception {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("testfile.txt");
        when(fileService.uploadToOrder(any(MultipartFile.class))).thenReturn("File uploaded successfully");
//        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.multipart("/uploadfile");
//    	getResult.characterEncoding("Encoding");
//    	MockMvcBuilders.standaloneSetup(orderController)
//    			.build()
//    			.perform(getResult)
        mockMvc.perform(MockMvcRequestBuilders.multipart("/uploadfile")
                .file("file", "test content".getBytes()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("File uploaded successfully"));
 
        verify(fileService, times(1)).uploadToOrder(any(MultipartFile.class));
    }
    
    @Test
    void testDownloadPDFReceipt() throws Exception {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("PDF content".getBytes());
 
        when(fileService.generatePDFReceipt(anyLong())).thenReturn(byteArrayInputStream);
 
        mockMvc.perform(MockMvcRequestBuilders.get("/receipt/{orderNumber}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_DISPOSITION, "form-data; name=\"attachment\"; filename=\"receipt.pdf\""))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PDF));
 
        verify(fileService, times(1)).generatePDFReceipt(anyLong());
    }
}
