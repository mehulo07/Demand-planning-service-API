package com.bns.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bns.dto.DashboardClientRequest;
import com.bns.dto.StockCalculationRequest;
import com.bns.mapper.DashboardCalculationMapper;
import com.bns.mapper.ProductCategoryActionMapper;
import com.bns.model.ProductCategoryAction;

import net.sf.json.JSONArray;

@PropertySource(value = "classpath:dashboard.properties" ,ignoreResourceNotFound=true)
@Repository
public class DashboardRepository {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private Environment propSource;
	
	@Autowired
	private NamedParameterJdbcTemplate namejdbcTemplate;
	
	
	public JSONArray getStockDataYearWiseRepository(DashboardClientRequest dashboardClientRequest) {
		return  jdbcTemplate.queryForObject(propSource.getProperty("getCalculatedDataForDashBoard"), new Object[] { dashboardClientRequest.getYear(), dashboardClientRequest.getCategoryList().get(0)} , new DashboardCalculationMapper());
	}
	
	public List<ProductCategoryAction> getProductCategoryWiseDataList(int startIndex,int endIndex, StockCalculationRequest stockCalculationRequest) {

		String query  =  propSource.getProperty("getProductCategoryWiseDataList") ;
		StringBuilder categoryBuilder = new StringBuilder();
		int categoryListLength = stockCalculationRequest.getCategoryList().size();
		List<String> categoryList = stockCalculationRequest.getCategoryList();
		if(stockCalculationRequest.getCategoryList().size() > 1) {
	        
	        for(int i = 0 ; i < categoryListLength ; i++) {
	        	if(i == categoryListLength) {
	        		categoryBuilder.append(categoryList.get(i));
	        	}else {
	        		categoryBuilder.append(categoryList.get(i)+",");
	        	}
	        }
	        query = query.replace("@1",categoryBuilder);
	        
		}else {
			query = query.replace("@1",categoryList.get(0));
		}
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("productCategory",stockCalculationRequest.getStockId());
		paramMap.put("startIndex", startIndex);
		paramMap.put("endIndex", endIndex);
		
		if(stockCalculationRequest.isSearch()) {
			paramMap.put("catalogNo", stockCalculationRequest.getSearchString());
			query = query.replace("@", " AND   CATALOG_NO = :catalogNo ");
		}else {
			query = query.replace("@", " ");
		}
		
		RowMapper<ProductCategoryAction> rowMapper = new ProductCategoryActionMapper();
		return namejdbcTemplate.query(query, paramMap, rowMapper);
	}
	
	public Long getTotalAllProductCategoryWiseData(String productCategoryValue, String productCategory) {
		String query = propSource.getProperty("getTotalAllProductCategoryWiseData") ;
		return jdbcTemplate.queryForObject(query, new Object[] { productCategory , productCategoryValue}, Long.class);
	}
	
	public boolean getEditBNLeadTime(String id, String leadTime) {
		String query = propSource.getProperty("getEditBNLeadTime") ;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("BN_LEAD_TIME", Integer.parseInt(( (leadTime == null  ||  leadTime.trim().equals("")) ? "0" : leadTime)));
		paramMap.put("id", Integer.parseInt(id));
		
		return namejdbcTemplate.update(query,paramMap) > 0;
	}
}
