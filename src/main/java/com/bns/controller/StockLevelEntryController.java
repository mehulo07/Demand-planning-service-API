package com.bns.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bns.dto.CategoryEntryInfo;
import com.bns.dto.FilterParam;
import com.bns.dto.ProductEntryInfo;
import com.bns.dto.StockCalculationRequest;
import com.bns.model.ProductInfo;
import com.bns.service.ProductInfoService;
import com.bns.service.StockLevelService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RestController
@RequestMapping(value = "DPS/V1/stockEntry")
public class StockLevelEntryController {
	
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
	
	@PostMapping("/categoryEntry")
	public ResponseEntity<JSONArray> saveCategoryParam(@RequestBody CategoryEntryInfo categoryEntryInfo){
		return stockLevelService.saveCategoryParam(categoryEntryInfo);
	}
	
	@PostMapping("/getParamsByCategory")
	public String getParamsByCategory(@RequestBody FilterParam productSettingSearch ,@RequestBody StockCalculationRequest stockCalculationRequest) {
		JSONObject mainObj = new JSONObject();
		JSONArray jarr = null;
		int totalPages = 0;
		String sortBy = stockCalculationRequest.getSortBy();
		int currentPage = stockCalculationRequest.getCurrentPage();
		String sortOrder = stockCalculationRequest.getSortOrder();
		int recordSize = stockCalculationRequest.getRowSize();
		List<Integer> monthInt = new ArrayList<Integer>();
	
		
		productSettingSearch.setMonth(monthInt);
		if(productSettingSearch.getCatalog_no().get(0).equalsIgnoreCase("")) {
			productSettingSearch.setCatalog_no(null);
		}
		try {
			jarr = productInfoService.getParamsByCategoryAndProduct(currentPage, recordSize,productSettingSearch,true);
			Double noOfPages = Double
					.valueOf(Double.valueOf(1000).doubleValue() / Double.valueOf(recordSize).doubleValue());
			
			if ((noOfPages.doubleValue() > 0.0D) && (noOfPages.doubleValue() < 1.0D)) {
				totalPages = 1;
			} else if (noOfPages.doubleValue() % 1.0D > 0.0D) {
				totalPages = noOfPages.intValue() + 1;
			} else {
				totalPages = noOfPages.intValue();
			}
			
			mainObj.put("page", Integer.valueOf(currentPage));
			mainObj.put("total", Integer.valueOf(totalPages));
			mainObj.put("records", 1);
			mainObj.put("rows", jarr);
			
		} catch (Exception e) {
			mainObj.put("status", e.getLocalizedMessage());
		}
		return mainObj.toString();
	}	
	
	
	@PostMapping("/getParamsByProduct")
	public String getParamsByProduct(@RequestBody FilterParam productSettingSearch ,@RequestBody StockCalculationRequest stockCalculationRequest) {
		JSONObject mainObj = new JSONObject();
		
		JSONArray jarr = null;
		int totalPages = 0;
		String sortBy = stockCalculationRequest.getSortBy();
		int currentPage = stockCalculationRequest.getCurrentPage();
		String sortOrder = stockCalculationRequest.getSortOrder();
		int recordSize = stockCalculationRequest.getRowSize();
		List<Integer> monthInt = new ArrayList<Integer>();
	
		
		productSettingSearch.setMonth(monthInt);
		if(productSettingSearch.getCatalog_no().get(0).equalsIgnoreCase("")) {
			productSettingSearch.setCatalog_no(null);
		}
		
		try {
			jarr = productInfoService.getParamsByCategoryAndProduct(currentPage, recordSize,productSettingSearch,false);
			Double noOfPages = Double
					.valueOf(Double.valueOf(1000).doubleValue() / Double.valueOf(recordSize).doubleValue());
			
			if ((noOfPages.doubleValue() > 0.0D) && (noOfPages.doubleValue() < 1.0D)) {
				totalPages = 1;
			} else if (noOfPages.doubleValue() % 1.0D > 0.0D) {
				totalPages = noOfPages.intValue() + 1;
			} else {
				totalPages = noOfPages.intValue();
			}
			
			mainObj.put("page", Integer.valueOf(currentPage));
			mainObj.put("total", Integer.valueOf(totalPages));
			mainObj.put("records", 1);
			mainObj.put("rows", jarr);

		} catch (Exception e) {
			mainObj.put("status", e.getLocalizedMessage());
		}
		return mainObj.toString();
	}
	
	
	
}
