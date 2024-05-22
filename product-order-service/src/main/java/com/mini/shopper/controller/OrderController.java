package com.mini.shopper.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mini.shopper.dto.OrderDetailsRes;
import com.mini.shopper.dto.PlaceOrderReq;
import com.mini.shopper.dto.PlaceOrderRes;
import com.mini.shopper.service.FileService;
import com.mini.shopper.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class OrderController {

	@Autowired
	OrderService orderService;
	
	@Autowired
	FileService fileService;
	
	@PostMapping("/placeOrder") //Customer
	public ResponseEntity<PlaceOrderRes> placeOrder(@RequestBody PlaceOrderReq placeorderreq) {
		PlaceOrderRes placeorderres = orderService.placeOrder(placeorderreq);
		return new ResponseEntity<>(placeorderres,HttpStatus.CREATED);
	}	
	
	@GetMapping("getOrderDetails/{orderNumber}") //Customer
	public ResponseEntity<OrderDetailsRes> getOrderDetails(@PathVariable long orderNumber) {
		OrderDetailsRes orderdetails = orderService.getOrderDetails(orderNumber);
		return ResponseEntity.ok(orderdetails);
	}
	
	@GetMapping("/purchaseHistory/{userId}") //Customer
	public ResponseEntity<List<OrderDetailsRes>> getPurchaseHistory(@PathVariable String userId) {
		List<OrderDetailsRes> orderDtos = orderService.getPurchaseHistory(userId);
		return ResponseEntity.ok(orderDtos);
	}
	
	@GetMapping("/orderHistory") //Shopper
	public ResponseEntity<List<OrderDetailsRes>> getAllOrders() {
		List<OrderDetailsRes> orderDtos = orderService.getAllOrders();
		return ResponseEntity.ok(orderDtos);
	}
	
	@GetMapping("/pendingOrders") //Shopper
	public ResponseEntity<List<OrderDetailsRes>> getAllPendingOrders() {
		List<OrderDetailsRes> orderDtos = orderService.getAllPendingOrders("Pending");
		return ResponseEntity.ok(orderDtos);
	}
	
	@PatchMapping("/statusChangeToReview/{orderNumber}") //Shopper
	public ResponseEntity<String> orderStatusChangeToReview(@PathVariable Long orderNumber) {
		return orderService.changeOrderStatusToReview(orderNumber);
	}
	
	@PatchMapping("/statusChangeToRejected/{orderNumber}") //Shopper
	public ResponseEntity<String> orderStatusChangeToRejected(@PathVariable Long orderNumber) {
		return orderService.changeOrderStatusToRejected(orderNumber);
	}
	
	@PatchMapping("statusChangeToSuccess/{orderNumber}")
	public ResponseEntity<String> statusChangeToSuccess(@PathVariable Long orderNumber) {
	return orderService.changeOrderStatusToSuccess(orderNumber);
	}
	
	@PostMapping("/uploadfile")
	public ResponseEntity<String> uploadFileToOrder(@RequestParam("file") MultipartFile file){
		try {
			String s= fileService.uploadToOrder(file);
			log.info(s);
			return ResponseEntity.ok(s);
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error uploading file");
		}
	}
	
	@GetMapping("/receipt/{orderNumber}")
    public ResponseEntity<byte[]> downloadPDFReceipt(@PathVariable long orderNumber) {
        try {
            // Generate PDF receipt
            ByteArrayInputStream inputStream = fileService.generatePDFReceipt(orderNumber);
 
            // Set response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "receipt.pdf");
 
            // Convert InputStream to byte array
            byte[] bytes = inputStream.readAllBytes();
 
            // Return response entity with PDF content and headers
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
	
}

