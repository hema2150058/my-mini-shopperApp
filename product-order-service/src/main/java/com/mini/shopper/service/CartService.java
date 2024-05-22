package com.mini.shopper.service;

import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.mini.shopper.dto.CartItemReq;
import com.mini.shopper.dto.CartItemsResponse;
import com.mini.shopper.dto.CartReqDto;
import com.mini.shopper.dto.CartResDto;
import com.mini.shopper.model.Cart;
import com.mini.shopper.model.Product;
import com.mini.shopper.repo.CartRepo;

@Service
@Slf4j
public class CartService {

	@Autowired
	CartRepo cartRepo;
	
	@Autowired
	ProductService productService;
	
	public Cart isItemInCart(CartItemReq isitemincart) {
		Product product = productService.getProductById(isitemincart.getProductId());
		return cartRepo.findByProductIdAndUserId(product, isitemincart.getUserId());
	}
	
	public Cart addProductsToCart(CartReqDto addtocart) {

		log.info("Product id is: "+ addtocart.getProductId());
		Product product = productService.getProductById(addtocart.getProductId());
		log.info("products in the cart are: "+ product);
		if (product == null) {
			return null;
		} else {
			Cart cart = new Cart();
			cart.setQuantity(addtocart.getQuantity());
			cart.setProductId(product);
			cart.setUserId(addtocart.getUserId());
			cart.setPrice(product.getPrice());
			return cartRepo.save(cart);
		}
	}
	
	public Cart updateCart(CartReqDto updatedCart) {

		Product product = productService.getProductById(updatedCart.getProductId());
		log.info("products in cart are: " + product);
		Cart cart = cartRepo.findByProductIdAndUserId(product, updatedCart.getUserId());
		cart.setQuantity(updatedCart.getQuantity());
		cartRepo.save(cart);
		return cart;
	}
	
	public List<Cart> getAllItems() {
		return cartRepo.findAll();
	}

	public List<CartResDto> getCart(String userId) {

		List<Cart> cart = cartRepo.findByUserId(userId);
		List<CartResDto> getcartitemsres = new ArrayList<>();
		for (Cart i : cart) {
			CartResDto temp = new CartResDto();
			temp.setCartId(i.getCartId());
			temp.setPrice(i.getPrice());
			temp.setProductId(i.getProductId().getProductId());
			temp.setQuantity(i.getQuantity());
			temp.setUserId(i.getUserId());
			getcartitemsres.add(temp);
		}
		return getcartitemsres;
	}

	public List<CartItemsResponse> getCartItems(String userId) {

		List<Cart> cart = cartRepo.findByUserId(userId);
		List<CartItemsResponse> getcartitemsres = new ArrayList<>();
		
		for (Cart i : cart) {
			CartItemsResponse temp = new CartItemsResponse();
			
			temp.setProductId(i.getProductId().getProductId());
			temp.setProductName(i.getProductId().getProductName());
			temp.setProductDesc(i.getProductId().getProductDesc());
			temp.setPrice(i.getPrice());
			temp.setQuantity(i.getQuantity());
			getcartitemsres.add(temp);
		}
		
		return getcartitemsres;
	}
	
	@Transactional
	public void removeProductFromCart(String userId, int productId) throws NotFoundException {

		Product product = productService.getProductById(productId);
		log.info(product.toString());
		cartRepo.deleteByProductIdAndUserId(product, userId);
		
		
	}
	
}
