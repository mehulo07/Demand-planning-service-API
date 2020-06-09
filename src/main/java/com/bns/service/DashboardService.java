package com.bns.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bns.dto.DashboardClientRequest;
import com.bns.dto.StockCalculationRequest;
import com.bns.repository.DashboardRepository;

import net.sf.json.JSONArray;

@Service
public class DashboardService {
	
	@Autowired
	private DashboardRepository dashboardRepository;
	
	public JSONArray getStockDataYearWiseService(DashboardClientRequest dashboardClientRequest) {
		
		if(!dashboardClientRequest.isSearch()) {
			return dashboardRepository.getStockDataYearWiseRepository(dashboardClientRequest);
		}else {
			//Get for multiple category and for month wise
		}
		return null;
	}
	
	public JSONArray getProductCategoryWiseData(StockCalculationRequest stockCalculationRequest) {
		
		return null;
	}
	
	
}
