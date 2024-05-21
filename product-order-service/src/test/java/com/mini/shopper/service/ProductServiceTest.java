package com.mini.shopper.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mini.shopper.model.Product;
import com.mini.shopper.repo.CartRepo;
import com.mini.shopper.repo.ProductRepo;

@ContextConfiguration(classes = {ProductService.class})
@ExtendWith(SpringExtension.class)
class ProductServiceTest {

	@MockBean
	private CartRepo cartRepo;
	
	@MockBean
	private ProductRepo productRepo;
	
	@Autowired
	private ProductService productService;
	
	@Test
	void testGetAllProducts() {
		ArrayList<Product> productList = new ArrayList<>();
		when(productRepo.findAll()).thenReturn(productList);
        List<Product> actualAllProducts = productService.getAllProducts();
        assertSame(productList, actualAllProducts);
        assertTrue(actualAllProducts.isEmpty());
        verify(productRepo).findAll();
		}
	
	@Test
    void testCreateProduct() {
        Product product = new Product();
        product.setImageUrl("https://example.org/example");
        product.setPrice(10.0d);
        product.setProductDesc("Product Desc");
        product.setProductId(123);
        product.setProductName("Product Name");
        product.setRatings(1);
        product.setStockQuantity(1);
        when(productRepo.save((Product) any())).thenReturn(product);

        Product product1 = new Product();
        product1.setImageUrl("https://example.org/example");
        product1.setPrice(10.0d);
        product1.setProductDesc("Product Desc");
        product1.setProductId(123);
        product1.setProductName("Product Name");
        product1.setRatings(1);
        product1.setStockQuantity(1);
        assertSame(product, productService.createProduct(product1));
        verify(productRepo).save((Product) any());
    }
	
	@Test
    void testGetProductById() {
        Product product = new Product();
        product.setImageUrl("https://example.org/example");
        product.setPrice(10.0d);
        product.setProductDesc("Product Desc");
        product.setProductId(123);
        product.setProductName("Product Name");
        product.setRatings(1);
        product.setStockQuantity(1);
        when(productRepo.findByProductId(anyInt())).thenReturn(product);
        assertSame(product, productService.getProductById(123));
        verify(productRepo).findByProductId(anyInt());
    }
}
