package com.bns.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bns.model.UserInfo;
import com.bns.service.UserInfoService;

import io.swagger.annotations.ApiModelProperty;

@RestController
@RequestMapping(value = "DPS/V1/userInfo")
public class UserInfoController {
		
	@Autowired
	private UserInfoService userInfoService;
	
	@GetMapping("/")
	@ApiModelProperty(value = "Get All user info",notes = "Retrive all active user info list to client.")
	public List<UserInfo> getAllUserInfo() {
		return userInfoService.getAllUserInfoList();
	}
	
	@GetMapping("/{id}")
	public UserInfo getUserInfoById(@PathVariable(value="id") String userId ) {
		return userInfoService.getUserInfoById(userId);
	}
	
	@GetMapping("department/{id}")
	public List<UserInfo> getUserInfoByDepartment(@PathVariable(value="id") String deptId ) {
		return userInfoService.getUserInfoByDepartment(deptId);
	}
	
	@PostMapping("/")
	public ResponseEntity<String> addUserInfo(@RequestBody UserInfo userInfo) {
		if(userInfoService.addUserInfo(userInfo)) 
			return ResponseEntity.ok().body("New user info added to list");
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("New user info is not addded to List.");
	}

	@PutMapping("/{id}")
	public ResponseEntity<String> updateUserInfo(@PathVariable(value="id") String userId  , @RequestBody UserInfo userInfo) {
		if(userInfoService.updateUserInfo(userInfo,userId)) 
			return ResponseEntity.ok().body("User info updated with new value");
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Exception while update User info.");
	}
	
}
