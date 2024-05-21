package com.mini.shopper.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mini.shopper.dto.CartItemReq;
import com.mini.shopper.dto.CartReqDto;
import com.mini.shopper.dto.CartResDto;
import com.mini.shopper.dto.IsItemInCart;
import com.mini.shopper.model.Cart;
import com.mini.shopper.model.Product;
import com.mini.shopper.repo.CartRepo;
import com.mini.shopper.repo.ProductRepo;

@ContextConfiguration(classes = {CartService.class, IsItemInCart.class,CartReqDto.class, CartItemReq.class})
@ExtendWith(SpringExtension.class)
class CartServiceTest {
	
	@Autowired
	private CartItemReq cartItemReq;
	
	@Autowired
	private CartReqDto addUpdateCartDto;
	
	@Autowired
	private CartService cartService;
	
	@MockBean
	private CartRepo cartRepo;
	
	@MockBean
	private ProductRepo productRepo;
	
	@MockBean
	private ProductService productService;
	
	@Test
    void testIsItemInCart() {
        Product product = new Product();
        product.setImageUrl("https://example.org/example");
        product.setPrice(10.0d);
        product.setProductDesc("Product Desc");
        product.setProductId(123);
        product.setProductName("Product Name");
        product.setRatings(1);
        product.setStockQuantity(1);

        Cart cart = new Cart();
        cart.setCartId(123);
        cart.setPrice(10.0d);
        cart.setProductId(product);
        cart.setQuantity(1);
        cart.setUserId("user");
        when(cartRepo.findByProductIdAndUserId((Product) any(), any())).thenReturn(cart);

        Product product1 = new Product();
        product1.setImageUrl("https://example.org/example");
        product1.setPrice(10.0d);
        product1.setProductDesc("Product Desc");
        product1.setProductId(123);
        product1.setProductName("Product Name");
        product1.setRatings(1);
        product1.setStockQuantity(1);
        when(productService.getProductById(anyInt())).thenReturn(product1);
        assertSame(cart, cartService.isItemInCart(cartItemReq));
        verify(cartRepo).findByProductIdAndUserId((Product) any(), any());
        verify(productService).getProductById(anyInt());
    }
	
	@Test
    void testIsItemInCart2() {
        Product product = new Product();
        product.setImageUrl("https://example.org/example");
        product.setPrice(10.0d);
        product.setProductDesc("Product Desc");
        product.setProductId(123);
        product.setProductName("Product Name");
        product.setRatings(1);
        product.setStockQuantity(1);

        Cart cart = new Cart();
        cart.setCartId(123);
        cart.setPrice(10.0d);
        cart.setProductId(product);
        cart.setQuantity(1);
        cart.setUserId("user");
        when(cartRepo.findByProductIdAndUserId((Product) any(), any())).thenReturn(cart);
        when(productService.getProductById(anyInt())).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> cartService.isItemInCart(cartItemReq));
        verify(productService).getProductById(anyInt());
    }
	
	 @Test
	    void testAddProductsToCart() {
	        Product product = new Product();
	        product.setImageUrl("https://example.org/example");
	        product.setPrice(10.0d);
	        product.setProductDesc("Product Desc");
	        product.setProductId(123);
	        product.setProductName("Product Name");
	        product.setRatings(1);
	        product.setStockQuantity(1);

	        Cart cart = new Cart();
	        cart.setCartId(123);
	        cart.setPrice(10.0d);
	        cart.setProductId(product);
	        cart.setQuantity(1);
	        cart.setUserId("user");
	        when(cartRepo.save((Cart) any())).thenReturn(cart);
	        
	        Product product1 = new Product();
	        product1.setImageUrl("https://example.org/example");
	        product1.setPrice(10.0d);
	        product1.setProductDesc("Product Desc");
	        product1.setProductId(123);
	        product1.setProductName("Product Name");
	        product1.setRatings(1);
	        product1.setStockQuantity(1);
	        when(productService.getProductById(anyInt())).thenReturn(product1);
	        assertSame(cart, cartService.addProductsToCart(addUpdateCartDto));
	        verify(cartRepo).save((Cart) any());
	        verify(productService).getProductById(anyInt());
	    }
	 
	 @Test
	    void testAddProductsToCart2() {
	        when(cartRepo.save((Cart) any())).thenThrow(new RuntimeException());

	        Product product = new Product();
	        product.setImageUrl("https://example.org/example");
	        product.setPrice(10.0d);
	        product.setProductDesc("Product Desc");
	        product.setProductId(123);
	        product.setProductName("Product Name");
	        product.setRatings(1);
	        product.setStockQuantity(1);
	        when(productService.getProductById(anyInt())).thenReturn(product);
	        assertThrows(RuntimeException.class, () -> cartService.addProductsToCart(addUpdateCartDto));
	        verify(cartRepo).save((Cart) any());
	        verify(productService).getProductById(anyInt());
	    }

	 @Test
	    void testUpdateCart() {
	        Product product = new Product();
	        product.setImageUrl("https://example.org/example");
	        product.setPrice(10.0d);
	        product.setProductDesc("Product Desc");
	        product.setProductId(123);
	        product.setProductName("Product Name");
	        product.setRatings(1);
	        product.setStockQuantity(1);

	        Cart cart = new Cart();
	        cart.setCartId(123);
	        cart.setPrice(10.0d);
	        cart.setProductId(product);
	        cart.setQuantity(1);
	        cart.setUserId("user");
	        
	        Product product1 = new Product();
	        product1.setImageUrl("https://example.org/example");
	        product1.setPrice(10.0d);
	        product1.setProductDesc("Product Desc");
	        product1.setProductId(123);
	        product1.setProductName("Product Name");
	        product1.setRatings(1);
	        product1.setStockQuantity(1);

	        Cart cart1 = new Cart();
	        cart1.setCartId(123);
	        cart1.setPrice(10.0d);
	        cart1.setProductId(product1);
	        cart1.setQuantity(1);
	        cart1.setUserId("user");
	        when(cartRepo.save((Cart) any())).thenReturn(cart1);
	        when(cartRepo.findByProductIdAndUserId((Product) any(), any())).thenReturn(cart);

	        Product product2 = new Product();
	        product2.setImageUrl("https://example.org/example");
	        product2.setPrice(10.0d);
	        product2.setProductDesc("Product Desc");
	        product2.setProductId(123);
	        product2.setProductName("Product Name");
	        product2.setRatings(1);
	        product2.setStockQuantity(1);
	        when(productService.getProductById(anyInt())).thenReturn(product2);
	        Cart actualUpdateCartResult = cartService.updateCart(addUpdateCartDto);
	        assertSame(cart, actualUpdateCartResult);
	        assertEquals(0, actualUpdateCartResult.getQuantity());
	        verify(cartRepo).findByProductIdAndUserId((Product) any(), any());
	        verify(cartRepo).save((Cart) any());
	        verify(productService).getProductById(anyInt());
	    }
	 
	 @Test
	    void testUpdateCart2() {
	        Product product = new Product();
	        product.setImageUrl("https://example.org/example");
	        product.setPrice(10.0d);
	        product.setProductDesc("Product Desc");
	        product.setProductId(123);
	        product.setProductName("Product Name");
	        product.setRatings(1);
	        product.setStockQuantity(1);

	        Cart cart = new Cart();
	        cart.setCartId(123);
	        cart.setPrice(10.0d);
	        cart.setProductId(product);
	        cart.setQuantity(1);
	        cart.setUserId("user");
	        
	        Product product1 = new Product();
	        product1.setImageUrl("https://example.org/example");
	        product1.setPrice(10.0d);
	        product1.setProductDesc("Product Desc");
	        product1.setProductId(123);
	        product1.setProductName("Product Name");
	        product1.setRatings(1);
	        product1.setStockQuantity(1);

	        Cart cart1 = new Cart();
	        cart1.setCartId(123);
	        cart1.setPrice(10.0d);
	        cart1.setProductId(product1);
	        cart1.setQuantity(1);
	        cart1.setUserId("user");
	        when(cartRepo.save((Cart) any())).thenReturn(cart1);
	        when(cartRepo.findByProductIdAndUserId((Product) any(), any())).thenReturn(cart);
	        when(productService.getProductById(anyInt())).thenThrow(new RuntimeException());
	        assertThrows(RuntimeException.class, () -> cartService.updateCart(addUpdateCartDto));
	        verify(productService).getProductById(anyInt());
	    }
	 
	 @Test
	    void testGetAllItems() {
	        ArrayList<Cart> cartList = new ArrayList<>();
	        when(cartRepo.findAll()).thenReturn(cartList);
	        List<Cart> actualAllItems = cartService.getAllItems();
	        assertSame(cartList, actualAllItems);
	        assertTrue(actualAllItems.isEmpty());
	        verify(cartRepo).findAll();
	    }
	 
	 @Test
	    void testGetAllItems2() {
	        when(cartRepo.findAll()).thenThrow(new RuntimeException());
	        assertThrows(RuntimeException.class, () -> cartService.getAllItems());
	        verify(cartRepo).findAll();
	    }
	 
	 @Test
	    void testGetCart() {
	        when(cartRepo.findByUserId(any())).thenReturn(new ArrayList<>());
	        assertTrue(cartService.getCart("user").isEmpty());
	        verify(cartRepo).findByUserId(any());
	    }
	 
	 @Test
	    void testGetCart2() {
	        Product product = new Product();
	        product.setImageUrl("https://example.org/example");
	        product.setPrice(10.0d);
	        product.setProductDesc("Product Desc");
	        product.setProductId(123);
	        product.setProductName("Product Name");
	        product.setRatings(1);
	        product.setStockQuantity(1);

	        Cart cart = new Cart();
	        cart.setCartId(123);
	        cart.setPrice(10.0d);
	        cart.setProductId(product);
	        cart.setQuantity(1);
	        cart.setUserId("user");
	        
	        ArrayList<Cart> cartList = new ArrayList<>();
	        cartList.add(cart);
	        when(cartRepo.findByUserId(any())).thenReturn(cartList);
	        List<CartResDto> actualCart = cartService.getCart("user");
	        assertEquals(1, actualCart.size());
	        CartResDto getResult = actualCart.get(0);
	        assertEquals(123, getResult.getCartId());
	        assertEquals("user", getResult.getUserId());
	        assertEquals(1, getResult.getQuantity());
	        assertEquals(123, getResult.getProductId());
	        assertEquals(10.0d, getResult.getPrice());
	        verify(cartRepo).findByUserId(any());
	    }
	 
	 @Test
	    void testGetCart3() {
	        when(cartRepo.findByUserId(any())).thenThrow(new RuntimeException());
	        assertThrows(RuntimeException.class, () -> cartService.getCart("user"));
	        verify(cartRepo).findByUserId(any());
	    }
	 
	 @Test
	    void testGetCart4() {
	        Product product = new Product();
	        product.setImageUrl("https://example.org/example");
	        product.setPrice(10.0d);
	        product.setProductDesc("Product Desc");
	        product.setProductId(123);
	        product.setProductName("Product Name");
	        product.setRatings(1);
	        product.setStockQuantity(1);

	        Product product1 = new Product();
	        product1.setImageUrl("https://example.org/example");
	        product1.setPrice(10.0d);
	        product1.setProductDesc("Product Desc");
	        product1.setProductId(123);
	        product1.setProductName("Product Name");
	        product1.setRatings(1);
	        product1.setStockQuantity(1);
	        Cart cart = mock(Cart.class);
	        when(cart.getQuantity()).thenThrow(new RuntimeException());
	        when(cart.getUserId()).thenThrow(new RuntimeException());
	        when(cart.getProductId()).thenReturn(product1);
	        when(cart.getPrice()).thenReturn(10.0d);
	        when(cart.getCartId()).thenReturn(123);
	        doNothing().when(cart).setCartId(anyInt());
	        doNothing().when(cart).setPrice(anyDouble());
	        doNothing().when(cart).setProductId((Product) any());
	        doNothing().when(cart).setQuantity(anyInt());
	        doNothing().when(cart).setUserId(any());
	        cart.setCartId(123);
	        cart.setPrice(10.0d);
	        cart.setProductId(product);
	        cart.setQuantity(1);
	        cart.setUserId("user");
	        
	        ArrayList<Cart> cartList = new ArrayList<>();
	        cartList.add(cart);
	        when(cartRepo.findByUserId(any())).thenReturn(cartList);
	        assertThrows(RuntimeException.class, () -> cartService.getCart("user"));
	        verify(cartRepo).findByUserId(any());
	        verify(cart).getProductId();
	        verify(cart).getPrice();
	        verify(cart).getCartId();
	        verify(cart).getQuantity();
	        verify(cart).setCartId(anyInt());
	        verify(cart).setPrice(anyDouble());
	        verify(cart).setProductId((Product) any());
	        verify(cart).setQuantity(anyInt());
	        verify(cart).setUserId(any());
	    }
	 
	 @Test
	    void testGetCartItems() {
	        when(cartRepo.findByUserId(any())).thenReturn(new ArrayList<>());
	        assertTrue(cartService.getCartItems("user").isEmpty());
	        verify(cartRepo).findByUserId(any());
	    }
	 
	 @Test
	    void testGetCartItems2() {
	        Product product = new Product();
	        product.setImageUrl("https://example.org/example");
	        product.setPrice(10.0d);
	        product.setProductDesc("Product Desc");
	        product.setProductId(123);
	        product.setProductName("Product Name");
	        product.setRatings(1);
	        product.setStockQuantity(1);

	        Cart cart = new Cart();
	        cart.setCartId(123);
	        cart.setPrice(10.0d);
	        cart.setProductId(product);
	        cart.setQuantity(1);
	        cart.setUserId("user");
	        
	        ArrayList<Cart> cartList = new ArrayList<>();
	        cartList.add(cart);
	        when(cartRepo.findByUserId(any())).thenReturn(cartList);
	        assertEquals(1, cartService.getCartItems("user").size());
	        verify(cartRepo).findByUserId(any());
	    }
	 
	 @Test
	    void testGetCartItems3() {
	        when(cartRepo.findByUserId(any())).thenThrow(new RuntimeException());
	        assertThrows(RuntimeException.class, () -> cartService.getCartItems("user"));
	        verify(cartRepo).findByUserId(any());
	    }
	 
	 @Test
	    void testGetCartItems4() {
	        Product product = new Product();
	        product.setImageUrl("https://example.org/example");
	        product.setPrice(10.0d);
	        product.setProductDesc("Product Desc");
	        product.setProductId(123);
	        product.setProductName("Product Name");
	        product.setRatings(1);
	        product.setStockQuantity(1);

	        Product product1 = new Product();
	        product1.setImageUrl("https://example.org/example");
	        product1.setPrice(10.0d);
	        product1.setProductDesc("Product Desc");
	        product1.setProductId(123);
	        product1.setProductName("Product Name");
	        product1.setRatings(1);
	        product1.setStockQuantity(1);
	        
	        Cart cart = mock(Cart.class);
	        when(cart.getPrice()).thenThrow(new RuntimeException());
	        when(cart.getQuantity()).thenThrow(new RuntimeException());
	        when(cart.getProductId()).thenReturn(product1);
	        doNothing().when(cart).setCartId(anyInt());
	        doNothing().when(cart).setPrice(anyDouble());
	        doNothing().when(cart).setProductId((Product) any());
	        doNothing().when(cart).setQuantity(anyInt());
	        doNothing().when(cart).setUserId(any());
	        cart.setCartId(123);
	        cart.setPrice(10.0d);
	        cart.setProductId(product);
	        cart.setQuantity(1);
	        cart.setUserId("user");

	        ArrayList<Cart> cartList = new ArrayList<>();
	        cartList.add(cart);
	        when(cartRepo.findByUserId(any())).thenReturn(cartList);
	        assertThrows(RuntimeException.class, () -> cartService.getCartItems("user"));
	        verify(cartRepo).findByUserId(any());
	        verify(cart, atLeast(1)).getProductId();
	        verify(cart).getPrice();
	        verify(cart).setCartId(anyInt());
	        verify(cart).setPrice(anyDouble());
	        verify(cart).setProductId((Product) any());
	        verify(cart).setQuantity(anyInt());
	        verify(cart).setUserId(any());
	    }
	 
	 @Test
	    void testRemoveProductFromCart() throws RuntimeException, NotFoundException {
	        doNothing().when(cartRepo).deleteByProductIdAndUserId((Product) any(), any());

	        Product product = new Product();
	        product.setImageUrl("https://example.org/example");
	        product.setPrice(10.0d);
	        product.setProductDesc("Product Desc");
	        product.setProductId(123);
	        product.setProductName("Product Name");
	        product.setRatings(1);
	        product.setStockQuantity(1);
	        when(productService.getProductById(anyInt())).thenReturn(product);
	        cartService.removeProductFromCart("user", 123);
	        verify(cartRepo).deleteByProductIdAndUserId((Product) any(), any());
	        verify(productService).getProductById(anyInt());
	        assertTrue(cartService.getAllItems().isEmpty());
	    }
	 
	 @Test
	    void testRemoveProductFromCart2() throws RuntimeException {
	        doNothing().when(cartRepo).deleteByProductIdAndUserId((Product) any(), any());
	        when(productService.getProductById(anyInt())).thenThrow(new RuntimeException());
	        assertThrows(RuntimeException.class, () -> cartService.removeProductFromCart("user", 123));
	        verify(productService).getProductById(anyInt());
	    }
	 
}
