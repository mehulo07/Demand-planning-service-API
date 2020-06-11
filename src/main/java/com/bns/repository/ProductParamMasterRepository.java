package com.bns.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bns.dto.FilterParam;
import com.bns.mapper.ProductParamDetailMapper;
import com.bns.mapper.ProductParamMasterMapper;
import com.bns.model.ProductParamDetail;
import com.bns.model.ProductParamMaster;

@PropertySource(value = "productSetting.properties")
@Repository
public class ProductParamMasterRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private Environment propSource;
	
	
	public ProductParamMaster getStockEntryMasterByTransactionId(String transactionId, String status) {
		ProductParamMaster productParamMaster = null;
			productParamMaster = jdbcTemplate.queryForObject(propSource.getProperty("getProductParamMasterByTransactionId"), new Object[] {transactionId}, new ProductParamMasterMapper());
		return productParamMaster;
	}
	
	
	public ProductParamMaster getStockEntryMasterByCategoryandCatalog(FilterParam productSeattingSearch, String catalog_no ,Date from , Date to) {
		ProductParamMaster productParamMaster = null;
			productParamMaster = jdbcTemplate.queryForObject(propSource.getProperty("getProductParamHead"), 
						new Object[] { productSeattingSearch.getCategoryId().get(0), catalog_no == null ? "-" : catalog_no
								, productSeattingSearch.getStatus() , String.valueOf(from) , String.valueOf(to) }, new ProductParamMasterMapper());	
		return productParamMaster;
	}
	
	public List<ProductParamDetail> getStockDetailByTransactionId(String transactionId) {
		List<ProductParamDetail> roductParamDetail = null;
		try {
			roductParamDetail= jdbcTemplate.query(propSource.getProperty("getProductDetailByTransactionId"),
					new Object[] { transactionId }, new ProductParamDetailMapper());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return roductParamDetail;
	}
}
