package com.bns.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bns.dto.ProductEntryInfo;
import com.bns.model.TransactionSeq;
import com.bns.repository.StockLevelRepossitory;
import com.bns.repository.TransactionSeqRepository;
import com.bns.utility.DpsConstant;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class StockLevelService {

	@Autowired
	private StockLevelRepossitory stockLevelRepossitory;
	
	@Autowired
	private TransactionSeqRepository transactionSeqRepository; 
	
	public ResponseEntity<JSONArray> saveProductList(List<ProductEntryInfo> listOfProductEntryInfo) {
		JSONArray returnArr = new JSONArray();
		JSONObject retrunObj = new JSONObject();
				
		try {
			for(ProductEntryInfo productEntryInfo :  listOfProductEntryInfo) {
				stockLevelRepossitory.delete(productEntryInfo.getProductParamMaster());
				//Get transaction id
				TransactionSeq seq = transactionSeqRepository.getNewTransaction(DpsConstant.DPS_COMPANY_PREFIX);
				//Transaction saved.
				stockLevelRepossitory.save(productEntryInfo.getProductParamMaster() , productEntryInfo.getProductInfo());
			}
			retrunObj.put("Message", "Product list is saved.");
			returnArr.add(retrunObj);
			return ResponseEntity.ok(returnArr);
		}catch(Exception e) {
			return ResponseEntity.ok(returnArr);
		}
		
	}
	
}
