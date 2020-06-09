package com.bns.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bns.dto.DashboardClientRequest;
import com.bns.dto.StockCalculationRequest;
import com.bns.service.DashboardService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RestController
@RequestMapping(value = "DPS/V1/dashboard")
public class DashboardController {
	
	@Autowired
	private DashboardService dashboardService;
	
	@GetMapping("/stockData")
	public JSONArray getStockDataYearWise(@RequestBody DashboardClientRequest dashboardClientRequest) {
		System.out.println("inside called");
		System.out.println("clientRequest is :"+dashboardClientRequest.toString());
		return dashboardService.getStockDataYearWiseService(dashboardClientRequest);
	}
	
	
	@GetMapping(value = "/productCategoryWiseData")
	public String getProductCategoryWiseData(@RequestBody StockCalculationRequest stockCalculationRequest) {
		
		System.out.println("product Category Value is :"+stockCalculationRequest.getCategoryList().get(0));
		System.out.println("Stock Param is :"+stockCalculationRequest.getStockId());
		
		JSONObject obj = new JSONObject();
		Double noOfPages = 1.0;
		int totalPages = 0;
		
		try {

			//JSONArray objArray = dashboardService.getProductCategoryWiseData(productCategoryValue,productCategory, currentPage, recordSize , isSearch ,searchString );
			JSONArray objArray = dashboardService.getProductCategoryWiseData(stockCalculationRequest);

			/*
			 * if(!stockCalculationRequest.isSearch()) { noOfPages =
			 * Double.valueOf(Double.valueOf(dashboardService.
			 * getTotalAllProductCategoryWiseData(productCategoryValue,productCategory)).
			 * doubleValue() / Double.valueOf(recordSize).doubleValue()); if
			 * ((noOfPages.doubleValue() > 0.0D) && (noOfPages.doubleValue() < 1.0D)) {
			 * totalPages = 1; } else if (noOfPages.doubleValue() % 1.0D > 0.0D) {
			 * totalPages = noOfPages.intValue() + 1; } else { totalPages =
			 * noOfPages.intValue(); } }else { totalPages = 1; }
			 * 
			 * obj.put("page", Integer.valueOf(currentPage)); obj.put("total",
			 * Integer.valueOf(totalPages)); obj.put("records", noOfPages); obj.put("rows",
			 * objArray);
			 */
		} catch (Exception e) {
			System.out.println("exception while get dashboard data");
			e.printStackTrace();
			obj.put("error", e.getLocalizedMessage());
		}
		return obj.toString();
	}
	
}
