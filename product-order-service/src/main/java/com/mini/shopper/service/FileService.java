package com.mini.shopper.service;

import org.apache.pdfbox.contentstream.operator.color.SetColor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.documentinterchange.taggedpdf.PDLayoutAttributeObject;
import org.apache.pdfbox.pdmodel.graphics.color.PDGamma;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.form.PDChoice;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.sl.usermodel.Background;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.mini.shopper.dto.CartReqDto;
import com.mini.shopper.dto.OrderDetailsRes;
import com.mini.shopper.dto.OrderedProductDetails;
import com.mini.shopper.dto.PlaceOrderReq;
import com.mini.shopper.dto.PlaceOrderRes;
import com.mini.shopper.model.Cart;
import com.mini.shopper.repo.CartRepo;
import com.mini.shopper.repo.OrderRepo;

@Service
public class FileService {

	@Autowired
	CartRepo cartRepo;
	
	@Autowired
	OrderRepo orderRepo;
	
	@Autowired 
	CartService cartService;
	
	@Autowired
	OrderService orderService;
	
	 public String uploadToOrder(MultipartFile file) throws IOException {
	        try (InputStream inputStream = file.getInputStream()) {
	            Workbook workbook = new XSSFWorkbook(inputStream);
	            processCartReqSheet(workbook.getSheetAt(0)); // Process sheet 1 (CartReqDto)
	            String orderNumber = processPlaceOrderSheet(workbook.getSheetAt(1)); // Process sheet 2 (PlaceOrderReq)
	            workbook.close();
	            return orderNumber;
	        } catch (IOException e) {
	            e.printStackTrace();
	            throw e; // Rethrow the exception to be handled by the controller
	        }
	    }
	  private void processCartReqSheet(Sheet sheet) {
	        List<CartReqDto> cartReqList = new ArrayList<>();
	        for (int i=1; i<= sheet.getLastRowNum(); i++) {
	        	Row row = sheet.getRow(i);
	            String userId = row.getCell(0).getStringCellValue();
	            int productId = (int) row.getCell(1).getNumericCellValue();
	            int quantity = (int) row.getCell(2).getNumericCellValue();
	            CartReqDto cartReqDto = new CartReqDto(userId, productId, quantity);
	            cartReqList.add(cartReqDto);
	            cartService.addProductsToCart(cartReqDto);
	        }
	        // Call addToCart service method with the list of CartReqDto objects
	    }
	
	  private String processPlaceOrderSheet(Sheet sheet) {
	        Row row = sheet.getRow(1); // Assuming there is only one row in the sheet
	        String userId = row.getCell(0).getStringCellValue();
	        String billingName = row.getCell(1).getStringCellValue();
	        String billingPno = String.valueOf((int)row.getCell(2).getNumericCellValue());
	        String billingAdress = row.getCell(3).getStringCellValue();
	        PlaceOrderReq placeOrderReq = new PlaceOrderReq(userId, billingName, billingPno, billingAdress);
	        PlaceOrderRes placeOrderRes = orderService.placeOrder(placeOrderReq);
	        System.out.println(placeOrderRes.getOrderNumber());
	        return "orderNumber is "+placeOrderRes.getOrderNumber();
	        // Call placeOrder service method with the PlaceOrderReq object
	    }
	 
	
	  
//	----------------------------------------------------------------
	
	  public ByteArrayInputStream generatePDFReceipt(long orderNumber) throws IOException{
	        
		  OrderDetailsRes orderDetailsRes = orderService.getOrderDetails(orderNumber);
		  
	            // Create a new PDF document
	            PDDocument document = new PDDocument();
	            PDPage page = new PDPage(PDRectangle.A4);
	            document.addPage(page);
	            PDPageContentStream contentStream = new PDPageContentStream(document, page);
	 
	            // Add logo
	            PDImageXObject logo = PDImageXObject.createFromFile("C:/Users/2150058/Videos/ReactJs_POCS/Mini-shopper-new/backend/mini-shopper-logo.png", document);
	            contentStream.drawImage(logo, 270, page.getMediaBox().getHeight() - 100, 70,70);
	 
	            // Add heading
	            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 24);
	            contentStream.beginText();
	            contentStream.newLineAtOffset((page.getMediaBox().getWidth()+100)/3,page.getMediaBox().getHeight()-160);
	            contentStream.setNonStrokingColor(0.133f,0.38f,0.612f);
	            contentStream.showText("Money Receipt");
	            contentStream.endText();
	 
	            // Add order details
	            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 15);
	            contentStream.beginText();
	            contentStream.newLineAtOffset(100, page.getMediaBox().getHeight() - 210);
	            contentStream.setNonStrokingColor(Color.BLACK);
	            contentStream.showText("Order Number       :  " + orderDetailsRes.getOrderNumber());
	            contentStream.endText();
	            
	            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 15);
	            contentStream.beginText();
	            contentStream.newLineAtOffset(100, page.getMediaBox().getHeight() - 240);
	            contentStream.setNonStrokingColor(Color.BLACK);
	            contentStream.showText("Order Date            :  " + orderDetailsRes.getOrderDate());
	            contentStream.endText();  
	            
	            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 15);
	            contentStream.beginText();
	            contentStream.newLineAtOffset(100, page.getMediaBox().getHeight() - 270);
	            contentStream.setNonStrokingColor(Color.BLACK);
	            contentStream.showText("Customer Name    :  " + orderDetailsRes.getCustomerName());
	            contentStream.endText();

	            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 15);
	            contentStream.beginText();
	            contentStream.newLineAtOffset(100, page.getMediaBox().getHeight() - 300);
	            contentStream.setNonStrokingColor(Color.BLACK);
	            contentStream.showText("Total Price             :  $" + orderDetailsRes.getTotalPrice());
	            contentStream.endText();

	            contentStream.moveTo(150, 500);
	            contentStream.lineTo(450, 500);
	            contentStream.stroke();
	        
	            
	            //table name
	            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 20);
	            contentStream.beginText();
	            contentStream.newLineAtOffset(100, page.getMediaBox().getHeight() - 390);
	            contentStream.setNonStrokingColor(0.851f,0.42f,0.102f);
	            contentStream.showText("Products:");
	            contentStream.endText();
	            
	           
//	            contentStream.showText("Order Date: " + orderDetailsRes.getOrderDate());
//	            contentStream.newLine();
//	            contentStream.showText("Customer Name: " + orderDetailsRes.getCustomerName());
//	            contentStream.newLine();
//	            contentStream.showText("Total Price: $" + orderDetailsRes.getTotalPrice());
//	            contentStream.endText();
	 
	            // Add table headers
	            contentStream.beginText();
	            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 15);
	            contentStream.newLineAtOffset(100, page.getMediaBox().getHeight() - 420);
	            contentStream.setNonStrokingColor(Color.BLACK);
	            contentStream.showText("Product Name");
	            contentStream.newLineAtOffset(270, 0);
	            contentStream.showText("Price");
	            contentStream.newLineAtOffset(70, 0);
	            contentStream.showText("Quantity");
	            contentStream.endText();
	            
	         // Add table data
	            List<OrderedProductDetails> productList = orderDetailsRes.getProductList();
	            int y = 390;
	            for (OrderedProductDetails product : productList) {
	                contentStream.beginText();
		            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 14);
		            contentStream.setNonStrokingColor(Color.BLACK);
	                contentStream.newLineAtOffset(100, y);
	                contentStream.showText(product.getProductName());
	                contentStream.newLineAtOffset(270, 0);
	                contentStream.showText("$" + product.getPrice());
	                contentStream.newLineAtOffset(100, 0);
	                contentStream.showText(String.valueOf(product.getQuantity()));
	                contentStream.endText();
	                y -= 25; // Adjust Y position for next row
	            }
	            
	            
	            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 16);
	            contentStream.beginText();
	            contentStream.newLineAtOffset(360, page.getMediaBox().getHeight() - 700);
	            contentStream.setNonStrokingColor(0.851f,0.42f,0.102f);
	            contentStream.showText("Thank  you for Choosing ");
	            contentStream.endText();
	            contentStream.beginText();
	            contentStream.newLineAtOffset(400, page.getMediaBox().getHeight() - 725);
	            contentStream.showText("MiniShoppeer");
	            contentStream.endText();
	            contentStream.beginText();
	            contentStream.newLineAtOffset(390, page.getMediaBox().getHeight() - 750);
	            contentStream.setNonStrokingColor(0.133f,0.38f,0.612f);
	            contentStream.showText("Happy Shopping!");
	            contentStream.endText();
	            
	         // Close the content stream
	            contentStream.close();
	     
	            // Save the document to a ByteArrayOutputStream
	            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	            document.save(outputStream);
	            document.close();
	            
	         // Convert ByteArrayOutputStream to ByteArrayInputStream
	            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
	     
	            return inputStream;
	        }	
	
}	