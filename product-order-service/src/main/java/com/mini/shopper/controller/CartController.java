package com.mini.shopper.controller;

import java.rmi.ServerException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mini.shopper.dto.CartItemReq;
import com.mini.shopper.dto.CartItemsResponse;
import com.mini.shopper.dto.CartReqDto;
import com.mini.shopper.dto.CartResDto;
import com.mini.shopper.exception.CartNotFoundException;
import com.mini.shopper.model.Cart;
import com.mini.shopper.service.CartService;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class CartController {

	@Autowired
	CartService cartService;
	

	@PostMapping(path = "/addToCart")
	public ResponseEntity<Cart> addProductsToCart(@RequestBody CartReqDto addtocart) throws ServerException, CartNotFoundException {
		Cart cartItem = cartService.addProductsToCart(addtocart);
		{
			if (cartItem == null) {
				log.error("The product you are trying to add in the cart is invalid");
				throw new CartNotFoundException("The product you are trying to add in the cart is invalid");
			} else {
				log.info("Product is added to the cart");
				return new ResponseEntity<>(cartItem, HttpStatus.CREATED);
			}
		}
	}

	@PostMapping(path = "/isItemInCart")
	public boolean isItemInCart(@RequestBody CartItemReq isitemincart) throws ServerException {
		Cart cartItem = cartService.isItemInCart(isitemincart);
		{
			if (cartItem == null) {
				log.error("Product not found in the cart");
				return false;
			} else {
				log.info("Product is in the cart");
				return true;
			}
		}
	}

	@PutMapping(path = "/updateCart")
	public ResponseEntity<Cart> updateCart(@RequestBody CartReqDto updatedcart) throws CartNotFoundException {

		Cart updatedCart = cartService.updateCart(updatedcart);
		{
			if (updatedCart == null) {
				log.error("Cart not found or No Items in Cart");
				throw new CartNotFoundException("Cart not found or No Items in Cart");
			} else {
				log.info("Cart updated with revised product(s) quantity successfully");
				return new ResponseEntity<>(updatedCart, HttpStatus.OK);
			}
		}

	}

	@GetMapping(path = "/getCart/{userId}")
	public ResponseEntity<List<CartResDto>> getCart(@PathVariable String userId) throws CartNotFoundException {
		List<CartResDto> cart = cartService.getCart(userId);
		if (cart.isEmpty()) {
			log.error("Cart with userId: "+userId+ " not found");
			return new ResponseEntity<>(cart, HttpStatus.OK);
		} else {
			log.info("Successfully retrieved Cart Items of user with userId: "+userId);
			return new ResponseEntity<>(cart, HttpStatus.OK);
		}
	}

	@GetMapping(path = "/getCartItems/{userId}")
	public ResponseEntity<List<CartItemsResponse>> getCartItems(@PathVariable String userId) throws CartNotFoundException {
		List<CartItemsResponse> cartItems = cartService.getCartItems(userId);
		if (cartItems.isEmpty()) {
			log.error("No Items in the cart for the userId: "+userId);
			throw new CartNotFoundException("No Items in the cart for the userId: "+userId);
		} else {
			log.info("Succesfully retrieved Cart with Items details of user with userId: "+ userId);
			return new ResponseEntity<>(cartItems, HttpStatus.OK);
		}

	}

	@DeleteMapping("/removeFromCart")
	public ResponseEntity<String> removeProductFromCart(@RequestBody CartItemReq removefromcartreq) {
		System.out.println(removefromcartreq);
		try {
			cartService.removeProductFromCart(removefromcartreq.getUserId(), removefromcartreq.getProductId());
			log.info("Product removed from cart successfully");
			return ResponseEntity.ok("Product removed from cart successfully");
		} catch (NotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error removing product from cart: " + e.getMessage());
		}

	}

}
