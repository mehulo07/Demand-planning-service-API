package com.bns.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bns.model.ProductInfo;
import com.bns.repository.ProductInfoRepository;

@Service
public class ProductInfoService {
	
	@Autowired
	private ProductInfoRepository productInfoRepository;
	
	public ResponseEntity<List<ProductInfo>> getProductList(ProductInfo productInfoBean ) {
		List<ProductInfo> searchProdList = null;
		try {
			searchProdList = productInfoRepository.getProductList(productInfoBean);
			if(searchProdList != null)
				return ResponseEntity.ok().body(searchProdList);
			else
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	
}