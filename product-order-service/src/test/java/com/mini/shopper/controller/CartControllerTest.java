package com.mini.shopper.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mini.shopper.dto.CartItemReq;
import com.mini.shopper.dto.CartItemsResponse;
import com.mini.shopper.dto.CartReqDto;
import com.mini.shopper.dto.CartResDto;
import com.mini.shopper.dto.IsItemInCart;
import com.mini.shopper.model.Cart;
import com.mini.shopper.model.Product;
import com.mini.shopper.service.CartService;
import com.mini.shopper.service.ProductService;


@ContextConfiguration(classes = {CartController.class, CartReqDto.class, IsItemInCart.class, CartItemReq.class} )
@ExtendWith(SpringExtension.class)
class CartControllerTest {

	@Autowired
	private CartController cartController;
	
	@MockBean
	private CartService cartService;
	
	@MockBean
	private ProductService productService;
	
	@BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        MockMvcBuilders.standaloneSetup(cartController).build();
        new ObjectMapper();
    }
	
	@Test
    void testAddProductsToCart() throws Exception {
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
        when(cartService.addProductsToCart((CartReqDto) any())).thenReturn(cart);

        CartReqDto addToCart1 = new CartReqDto();
        addToCart1.setProductId(123);
        addToCart1.setQuantity(2);
        addToCart1.setUserId("user");
        String content = (new ObjectMapper()).writeValueAsString(addToCart1);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/addToCart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(cartController).build().perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"cartId\":123,\"userId\":\"user\",\"productId\":{\"productId\":123,\"productName\":\"Product Name\",\"productDesc\":\"Product Desc\",\"stockQuantity\":1,\"price\":10.0,\"ratings\":1,\"imageUrl\":\"https://example.org/example\"},\"quantity\":1,\"price\":10.0}"));
    }
	
	@Test
    void testIsItemInCart() throws Exception {
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
        when(cartService.isItemInCart((CartItemReq) any())).thenReturn(cart);

        IsItemInCart isItemInCart1 = new IsItemInCart();
        isItemInCart1.setProductId(123);
        isItemInCart1.setUserId("user");
        String content = (new ObjectMapper()).writeValueAsString(isItemInCart1);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/isItemInCart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(cartController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("true"));
    }

    @Test
    void testUpdateCart() throws Exception {
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
        when(cartService.updateCart((CartReqDto) any())).thenReturn(cart);

        CartReqDto updatedCart1 = new CartReqDto();
        updatedCart1.setProductId(123);
        updatedCart1.setQuantity(1);
        updatedCart1.setUserId("user");
        String content = (new ObjectMapper()).writeValueAsString(updatedCart1);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/updateCart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(cartController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"cartId\":123,\"userId\":\"user\",\"productId\":{\"productId\":123,\"productName\":\"Product Name\",\"productDesc\":\"Product Desc\",\"stockQuantity\":1,\"price\":10.0,\"ratings\":1,\"imageUrl\":\"https://example.org/example\"},\"quantity\":1,\"price\":10.0}"));
    }

    
    @Test
    void testGetCart() throws Exception {
        ArrayList<CartResDto> getCartResList = new ArrayList<>();
        getCartResList.add(new CartResDto(123, "user", 123, 1, 10.0d));
        when(cartService.getCart(any())).thenReturn(getCartResList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/getCart/{userId}", "user");
        MockMvcBuilders.standaloneSetup(cartController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("[{\"cartId\":123,\"userId\":\"user\",\"productId\":123,\"quantity\":1,\"price\":10.0}]"));
    }


    @Test
    void testGetCartItems() throws Exception {
        ArrayList<CartItemsResponse> getCartItemsResList = new ArrayList<>();
        getCartItemsResList.add(new CartItemsResponse(123, "?", "?", 1, 10.0d));
        when(cartService.getCartItems(any())).thenReturn(getCartItemsResList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/getCartItems/{userId}", "user");
        MockMvcBuilders.standaloneSetup(cartController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"productId\":123,\"productName\":\"?\",\"productDesc\":\"?\",\"quantity\":1,\"price\":10.0}]"));
    }

    @Test
    void testRemoveProductFromCart() throws Exception {
        doNothing().when(cartService).removeProductFromCart(any(), anyInt());

        CartItemReq removeFromCartReq1 = new CartItemReq();
        removeFromCartReq1.setProductId(123);
        removeFromCartReq1.setUserId("user");
        String content = (new ObjectMapper()).writeValueAsString(removeFromCartReq1);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/removeFromCart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(cartController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Product removed from cart successfully"));
    }

    @Test
    void testRemoveProductFromCart2() throws Exception {
        doThrow(new RuntimeException("user")).when(cartService).removeProductFromCart(any(), anyInt());

        CartItemReq removeFromCartReq1 = new CartItemReq();
        removeFromCartReq1.setProductId(123);
        removeFromCartReq1.setUserId("user");
        String content = (new ObjectMapper()).writeValueAsString(removeFromCartReq1);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/removeFromCart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(cartController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500))
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("Error removing product from cart: user"));
    }

    @Test
    void testRemoveProductFromCart3() throws Exception {
        doThrow(new RuntimeException()).when(cartService).removeProductFromCart(any(), anyInt());

        CartItemReq removeFromCartReq1 = new CartItemReq();
        removeFromCartReq1.setProductId(123);
        removeFromCartReq1.setUserId("user");
        String content = (new ObjectMapper()).writeValueAsString(removeFromCartReq1);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/removeFromCart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(cartController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }
	
}
