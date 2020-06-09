package com.bns.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bns.dto.DashboardClientRequest;
import com.bns.mapper.DashboardCalculationMapper;

import net.sf.json.JSONArray;

@PropertySource(value = "classpath:dashboard.properties" ,ignoreResourceNotFound=true)
@Repository
public class DashboardRepository {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private Environment propSource;
	
	public JSONArray getStockDataYearWiseRepository(DashboardClientRequest dashboardClientRequest) {
		return  jdbcTemplate.queryForObject(propSource.getProperty("getCalculatedDataForDashBoard"), new Object[] { dashboardClientRequest.getYear(), dashboardClientRequest.getCategoryList().get(0)} , new DashboardCalculationMapper());
	}
	
}
