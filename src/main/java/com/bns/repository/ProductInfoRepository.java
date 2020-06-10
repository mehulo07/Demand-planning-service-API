package com.bns.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bns.mapper.ProductInfoMapper;
import com.bns.model.ProductInfo;

@PropertySource(value = "classpath:productSetting.properties")
@Repository
public class ProductInfoRepository {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private Environment propSource;
	
	public List<ProductInfo> getProductList(ProductInfo productInfoBean) {
		String tempProdName =  productInfoBean.getProductDesc() + "%";
		return jdbcTemplate.query(propSource.getProperty("getProductList"),new Object[] { productInfoBean.getContract() , productInfoBean.getCategory() , tempProdName , productInfoBean.getLimit() }, new ProductInfoMapper());	
	}
	
}
