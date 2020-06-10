package com.bns.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bns.dto.ProductEntryInfo;
import com.bns.model.ProductInfo;
import com.bns.service.ProductInfoService;
import com.bns.service.StockLevelService;

import net.sf.json.JSONArray;

@RestController
@RequestMapping(value = "DPS/V1/stockParam")
public class StockLevelController {
	
	@Autowired
	private ProductInfoService productInfoService; 
	
	@Autowired
	private StockLevelService stockLevelService;
	
	@GetMapping("/searchProduct")
	public ResponseEntity<List<ProductInfo>> getProductList(@RequestBody ProductInfo productInfo){
		System.out.println("productInfo is :"+productInfo);
		return productInfoService.getProductList(productInfo);
	}
	
	@PostMapping("/productEntry")
	public ResponseEntity<JSONArray> saveMultipleProductParam(@RequestBody List<ProductEntryInfo> productEntryInfo){
		return stockLevelService.saveProductList(productEntryInfo);
	}
	
}
