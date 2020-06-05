package com.bns.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bns.mapper.RoleMasterMapper;
import com.bns.mapper.UserMapper;
import com.bns.model.RoleMaster;
import com.bns.model.User;

@Repository
public class DPSAuthenticationProvider {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public User authenticate(String username) {
		User user  = null;
			try {
				user  = jdbcTemplate.queryForObject("select * from DPS_USER_TAB where username = ? and STATUS = 'active' ",new UserMapper(), username);
			}catch(Exception e) {
				System.out.println("User name or password is invaida");
				e.printStackTrace();
			}
			return user;
	}
	
	public RoleMaster getRoles(String roleId){
		RoleMaster userRole= null;
		userRole = jdbcTemplate.queryForObject("select * from DPS_ROLE_TAB where role_id = ?", new Object[] {roleId} , new RoleMasterMapper());
		return userRole;
	}
	
}
