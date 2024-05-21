package com.mini.shopper.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
 
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;
 
import com.mini.shopper.dto.*;
import com.mini.shopper.repo.*;
 
@ContextConfiguration(classes = {FileService.class})
@ExtendWith(SpringExtension.class)
class FileServiceTest {
 
    @MockBean
    private CartRepo cartRepo;
    
    @MockBean
    private OrderRepo orderRepo;
    
    @MockBean
    private CartService cartService;
    
    @MockBean
    private OrderService orderService;
 
    @Autowired
    private FileService fileService;
    
    private Workbook workbook;
    private Sheet cartReqSheet;
    private Sheet placeOrderSheet;
    
    @BeforeEach
    void setUp() {
        fileService = new FileService();
        MockitoAnnotations.openMocks(this);
        fileService.cartRepo = cartRepo;
        fileService.orderRepo = orderRepo;
        fileService.cartService = cartService;
        fileService.orderService = orderService;
        
        workbook = new XSSFWorkbook();
        cartReqSheet = workbook.createSheet("CartReq");
        placeOrderSheet = workbook.createSheet("PlaceOrder");
    }
 
    @Test
    void testUploadToOrder() throws IOException {
        createCartReqSheet();
        createPlaceOrderSheet();
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        
        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenReturn(in);
        
        PlaceOrderRes placeOrderRes = new PlaceOrderRes();
        placeOrderRes.setOrderNumber(12345L);
        when(orderService.placeOrder(any(PlaceOrderReq.class))).thenReturn(placeOrderRes);
        
        String orderNumber = fileService.uploadToOrder(file);
        
        assertEquals("12345", orderNumber);
        verify(cartService, times(1)).addProductsToCart(any(CartReqDto.class));
    }
    
    void createCartReqSheet() {
        Row row = cartReqSheet.createRow(0);
        row.createCell(0).setCellValue("UserId");
        row.createCell(1).setCellValue("ProductId");
        row.createCell(2).setCellValue("Quantity");
        
        row = cartReqSheet.createRow(1);
        row.createCell(0).setCellValue("user1");
        row.createCell(1).setCellValue(1);
        row.createCell(2).setCellValue(2);
    }
    
    void createPlaceOrderSheet() {
        Row row = placeOrderSheet.createRow(0);
        row.createCell(0).setCellValue("UserId");
        row.createCell(1).setCellValue("BillingName");
        row.createCell(2).setCellValue("BillingPno");
        row.createCell(3).setCellValue("BillingAddress");
        
        row = placeOrderSheet.createRow(1);
        row.createCell(0).setCellValue("user1");
        row.createCell(1).setCellValue("John Doe");
        row.createCell(2).setCellValue(1234567890);
        row.createCell(3).setCellValue("123 Main St");
    }
 
    @Test
    void testGeneratePDFReceipt() throws IOException {
        long orderNumber = 12345L;
 
        // Creating mock data for order details
        OrderDetailsRes orderDetails = new OrderDetailsRes();
        orderDetails.setOrderNumber(orderNumber);
        orderDetails.setOrderDate(LocalDate.now());
        orderDetails.setCustomerName("John Doe");
        orderDetails.setTotalPrice(100.0);
        orderDetails.setOrderStatus("Pending");
 
        // Creating mock data for ordered product details
        OrderedProductDetails productDetails = new OrderedProductDetails();
        productDetails.setProductId(1);
        productDetails.setProductName("Product A");
        productDetails.setProductDesc("Description A");
        productDetails.setPrice(50.0);
        productDetails.setQuantity(2);
        
        List<OrderedProductDetails> productList = new ArrayList<>();
        productList.add(productDetails);
        orderDetails.setProductList(productList);
 
        when(orderService.getOrderDetails(orderNumber)).thenReturn(orderDetails);
 
        ByteArrayInputStream pdfStream = fileService.generatePDFReceipt(orderNumber);
 
        assertNotNull(pdfStream);
        assertTrue(pdfStream.available() > 0);
        
        pdfStream.close();
    }

}
