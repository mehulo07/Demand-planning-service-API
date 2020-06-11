package com.bns.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bns.dto.FilterParam;
import com.bns.model.ProductInfo;
import com.bns.model.ProductParamDetail;
import com.bns.model.ProductParamMaster;
import com.bns.model.TransactionSeq;
import com.bns.repository.ProductInfoRepository;
import com.bns.repository.ProductParamMasterRepository;
import com.bns.repository.StockLevelRepossitory;
import com.bns.utility.DateUtility;
import com.bns.utility.DpsConstant;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class ProductInfoService {
	
	@Autowired
	private ProductInfoRepository productInfoRepository;
	
	@Autowired
	private ProductParamMasterRepository productParamMasterRepository;
	
	@Autowired
	private StockLevelRepossitory stockLevelRepossitory;
	
	@Autowired
	private DateUtility dateUtility;
	
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
	
	
	public JSONArray getParamsByCategoryAndProduct(Integer currentPage, Integer recordSize,FilterParam productSeattingSearch , Boolean byCategory) {
			
		JSONArray jarr = new JSONArray();
		currentPage--;
		int startIndex = currentPage * recordSize + 1;
		int endIndex = startIndex + recordSize - 1;
		ProductParamMaster categoryProductParamMaster = null;
		List<Integer> month = null;
		java.sql.Date firstDay , lastDay;
		List<TransactionSeq> transactions = null;
		List<ProductParamMaster> productParamMasterList = new ArrayList();
		int year;
		try {
			year = productSeattingSearch.getYear();
			month = productSeattingSearch.getMonth();
			firstDay = dateUtility.getFirstDateInSQLFormate( year, month.get(0) );
			lastDay = dateUtility.getLastDateInSQLFormate( year , month.get(0) );
			
			if(productSeattingSearch.getCatalog_no() == null || productSeattingSearch.getCatalog_no().get(0).equalsIgnoreCase("")) {
				categoryProductParamMaster = productParamMasterRepository.getStockEntryMasterByCategoryandCatalog(productSeattingSearch,null,firstDay,lastDay);
				
				if(categoryProductParamMaster != null) {
					categoryProductParamMaster.setCatalog_Desc(productInfoRepository.getProductNameByCatalogNo(categoryProductParamMaster.getCatalog_no(), DpsConstant.DPS_PRODUCT_CONTRACT));
					categoryProductParamMaster.setProductParamDetailBeanList(productParamMasterRepository.getStockDetailByTransactionId(categoryProductParamMaster.getTransaction_id()));
					
					//getAllTransaction
					transactions = stockLevelRepossitory.getTransactionListByCategoryId(productSeattingSearch.getCategoryId().get(0),firstDay,lastDay);
					//iterate transactions
					for (TransactionSeq tempTransactionSeq: transactions) {
						ProductParamMaster tempProductParamMaster = null;
						tempProductParamMaster = productParamMasterRepository.getStockEntryMasterByTransactionId(tempTransactionSeq.getTransaction_id(), DpsConstant.STATUS_ACTIVE);
						tempProductParamMaster.setCatalog_Desc(productInfoRepository.getProductNameByCatalogNo(categoryProductParamMaster.getCatalog_no(), DpsConstant.DPS_PRODUCT_CONTRACT));
						if(tempProductParamMaster != null) {
							tempProductParamMaster.setProductParamDetailBeanList(productParamMasterRepository.getStockDetailByTransactionId(tempProductParamMaster.getTransaction_id()));
							productParamMasterList.add(tempProductParamMaster);
						}
					}
				}
			}else {
				categoryProductParamMaster = productParamMasterRepository.getStockEntryMasterByCategoryandCatalog(productSeattingSearch, null,firstDay,lastDay);
				if(categoryProductParamMaster != null) {
					categoryProductParamMaster.setCatalog_Desc(productInfoRepository.getProductNameByCatalogNo(categoryProductParamMaster.getCatalog_no(), DpsConstant.DPS_PRODUCT_CONTRACT));
					categoryProductParamMaster.setProductParamDetailBeanList(productParamMasterRepository.getStockDetailByTransactionId(categoryProductParamMaster.getTransaction_id()));
				}
				
				ProductParamMaster tempProductParamMaster = null;
				tempProductParamMaster = productParamMasterRepository.getStockEntryMasterByCategoryandCatalog(productSeattingSearch, productSeattingSearch.getCatalog_no().get(0),firstDay,lastDay);
				if(tempProductParamMaster  != null) {
					tempProductParamMaster.setCatalog_Desc(productInfoRepository.getProductNameByCatalogNo(categoryProductParamMaster.getCatalog_no(), DpsConstant.DPS_PRODUCT_CONTRACT));
					tempProductParamMaster .setProductParamDetailBeanList(productParamMasterRepository.getStockDetailByTransactionId(categoryProductParamMaster.getTransaction_id()));
					productParamMasterList.add(tempProductParamMaster);
				}
			}
			int rowId = 0;
			
			if(byCategory) {
				if(categoryProductParamMaster!=null) {
					for(ProductParamDetail bean : categoryProductParamMaster.getProductParamDetailBeanList()) {
						JSONObject obj = new JSONObject();
						JSONArray arr = new JSONArray();
						
						arr.add(bean.getStk_cat_name());
						arr.add(bean.getStk_cat_to());
						arr.add(bean.getStk_cat_id());
						arr.add(categoryProductParamMaster.getTransaction_id());
						
						obj.put("id", "catParam"+rowId++);
						obj.put("cell", arr);
						jarr.add(obj);
					}
				}
			}else {
				for(ProductParamMaster list : productParamMasterList) {
					for(ProductParamDetail bean : list.getProductParamDetailBeanList()) {
						JSONObject obj = new JSONObject();
						JSONArray arr = new JSONArray();
						
						arr.add(list.getCatalog_Desc());
						arr.add(bean.getStk_cat_name());
						arr.add(bean.getStk_cat_to());
						arr.add(list.getTransaction_id());
						
						obj.put("id", "prodParam"+rowId++);
						obj.put("cell", arr);
						jarr.add(obj);
					}
				}
			}
			
		} finally {
		}
		return jarr;
	}
	
	/*
	 * public JSONArray getParamsByProduct(Integer currentPage, Integer
	 * recordSize,FilterParam productSeattingSearch) { JSONArray jarr = new
	 * JSONArray(); currentPage--; int startIndex = currentPage * recordSize + 1;
	 * int endIndex = startIndex + recordSize - 1; ProductParamMaster
	 * categoryProductParamMaster = null; List<Integer> month = null; JSONObject
	 * jsonObject = new JSONObject(); java.sql.Date firstDay , lastDay;
	 * List<TransactionSeq> transactions = null; List<ProductParamMaster>
	 * productParamMasterList = new ArrayList<>(); int year; try { year =
	 * productSeattingSearch.getYear(); month = productSeattingSearch.getMonth();
	 * firstDay = dateUtility.getFirstDateInSQLFormate( year, month.get(0) );
	 * lastDay = dateUtility.getLastDateInSQLFormate( year , month.get(0) );
	 * 
	 * if(productSeattingSearch.getCatalog_no() == null ||
	 * productSeattingSearch.getCatalog_no().get(0).equalsIgnoreCase("")) {
	 * categoryProductParamMaster =
	 * productParamMasterRepository.getStockEntryMasterByCategoryandCatalog(
	 * productSeattingSearch,null,firstDay,lastDay); if(categoryProductParamMaster
	 * != null) { categoryProductParamMaster.setCatalog_Desc(productInfoRepository.
	 * getProductNameByCatalogNo(categoryProductParamMaster.getCatalog_no(),
	 * DpsConstant.DPS_PRODUCT_CONTRACT));
	 * categoryProductParamMaster.setProductParamDetailBeanList(
	 * productParamMasterRepository.getStockDetailByTransactionId(
	 * categoryProductParamMaster.getTransaction_id()));
	 * 
	 * //getAllTransaction transactions =
	 * stockLevelRepossitory.getTransactionListByCategoryId(productSeattingSearch.
	 * getCategoryId().get(0),firstDay,lastDay); //iterate transactions for
	 * (TransactionSeq tempTransactionSeq: transactions) { ProductParamMaster
	 * tempProductParamMaster = null; tempProductParamMaster =
	 * productParamMasterRepository.getStockEntryMasterByTransactionId(
	 * tempTransactionSeq.getTransaction_id(), DpsConstant.STATUS_ACTIVE);
	 * tempProductParamMaster.setCatalog_Desc(productInfoRepository.
	 * getProductNameByCatalogNo(categoryProductParamMaster.getCatalog_no(),
	 * DpsConstant.DPS_PRODUCT_CONTRACT)); if(tempProductParamMaster != null) {
	 * tempProductParamMaster.setProductParamDetailBeanList(
	 * productParamMasterRepository.getStockDetailByTransactionId(
	 * tempProductParamMaster.getTransaction_id()));
	 * productParamMasterList.add(tempProductParamMaster); } } } }else {
	 * categoryProductParamMaster =
	 * productParamMasterRepository.getStockEntryMasterByCategoryandCatalog(
	 * productSeattingSearch, null,firstDay,lastDay); if(categoryProductParamMaster
	 * != null) { categoryProductParamMaster.setCatalog_Desc(productInfoRepository.
	 * getProductNameByCatalogNo(categoryProductParamMaster.getCatalog_no(),
	 * DpsConstant.DPS_PRODUCT_CONTRACT));
	 * categoryProductParamMaster.setProductParamDetailBeanList(
	 * productParamMasterRepository.getStockDetailByTransactionId(
	 * categoryProductParamMaster.getTransaction_id())); } ProductParamMaster
	 * tempProductParamMaster = null; tempProductParamMaster =
	 * productParamMasterRepository.getStockEntryMasterByCategoryandCatalog(
	 * productSeattingSearch,
	 * productSeattingSearch.getCatalog_no().get(0),firstDay,lastDay);
	 * if(tempProductParamMaster != null) {
	 * tempProductParamMaster.setCatalog_Desc(productInfoRepository.
	 * getProductNameByCatalogNo(categoryProductParamMaster.getCatalog_no(),
	 * DpsConstant.DPS_PRODUCT_CONTRACT)); tempProductParamMaster
	 * .setProductParamDetailBeanList(productParamMasterRepository.
	 * getStockDetailByTransactionId(categoryProductParamMaster.getTransaction_id())
	 * ); productParamMasterList.add(tempProductParamMaster); } } int rowId = 0;
	 * for(ProductParamMaster list : productParamMasterList) {
	 * for(ProductParamDetail bean : list.getProductParamDetailBeanList()) {
	 * JSONObject obj = new JSONObject(); JSONArray arr = new JSONArray();
	 * 
	 * arr.add(list.getCatalog_Desc()); arr.add(bean.getStk_cat_name());
	 * arr.add(bean.getStk_cat_to()); arr.add(list.getTransaction_id());
	 * 
	 * obj.put("id", "prodParam"+rowId++); obj.put("cell", arr); jarr.add(obj); } }
	 * } finally { } return jarr; }
	 */
	
}