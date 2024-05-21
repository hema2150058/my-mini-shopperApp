package com.mini.shopper.controller;

import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.List;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mini.shopper.model.Product;
import com.mini.shopper.service.ProductService;
 
@WebMvcTest(ProductController.class)
class ProductControllerTest {
 
    @Autowired
    private MockMvc mockMvc;
 
    @MockBean
    private ProductService productService;
 
    private ObjectMapper objectMapper;
 
    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }
 
    @Test
    void testGetAllProducts() throws Exception {
        List<Product> products = Arrays.asList(
                new Product(1, "Product 1", "Description 1", 100, 100.0, 4, "https://example.org/example"),
                new Product(2, "Product 2", "Description 2", 200, 200.0, 5, "https://example.org/example2")
        );
 
        when(productService.getAllProducts()).thenReturn(products);
 
        mockMvc.perform(MockMvcRequestBuilders.get("/getAllProducts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].productId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].productName").value("Product 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].productId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].productName").value("Product 2"));
        
        verify(productService, times(1)).getAllProducts();
    }
 
    @Test
    void testCreateProduct() throws Exception {
        Product product = new Product(1, "Product 1", "Description 1", 100, 100.0, 4, "https://example.org/example");
        when(productService.createProduct(any(Product.class))).thenReturn(product);
 
        mockMvc.perform(MockMvcRequestBuilders.post("/addProducts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.productId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productName").value("Product 1"));
        
        verify(productService, times(1)).createProduct(any(Product.class));
    }
 
    @Test
    void testGetProductById() throws Exception {
        int productId = 1;
        Product product = new Product(1, "Product 1", "Description 1", 100, 100.0, 4, "https://example.org/example");
 
        when(productService.getProductById(productId)).thenReturn(product);
 
        mockMvc.perform(MockMvcRequestBuilders.get("/getProductById/{productId}", productId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.productId").value(productId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productName").value("Product 1"));
        
        verify(productService, times(1)).getProductById(productId);
    }
 
}
