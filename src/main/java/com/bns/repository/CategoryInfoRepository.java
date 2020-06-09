
package com.bns.repository;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bns.mapper.CategoryInfoMapper;
import com.bns.model.CategoryInfo;

@PropertySource(value = "classpath:CategoryInfo.properties")
@Repository
public class CategoryInfoRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private Environment propSource;

	public List<CategoryInfo> getAllCategoryList() {
		return jdbcTemplate.query(propSource.getProperty("getAllCategory"), new CategoryInfoMapper());
	}

	public CategoryInfo getCategoryDetailById(String categoryId) {
		return jdbcTemplate.queryForObject(propSource.getProperty("getCategoryById"), new Object[] { categoryId },
				new CategoryInfoMapper());
	}

	public boolean addCategory(CategoryInfo categoryInfo) {
		return jdbcTemplate.update(propSource.getProperty("addCategory"), categoryInfo.getCategoryId() , categoryInfo.getCategoryName() , categoryInfo.getCategoryDescription()
				,categoryInfo.getStatus()) > 0;
	}

	public boolean inactiveCategory(String categoryId) throws SQLException {
		return jdbcTemplate.update(propSource.getProperty("inactiveCategory"), categoryId) > 0;
	}

	public boolean updateCategory(CategoryInfo categoryInfo, String categoryId) {
		return jdbcTemplate.update(propSource.getProperty("updateCategory"), categoryInfo.getCategoryName() , categoryInfo.getCategoryDescription() , categoryInfo.getStatus() ,categoryId) > 0;
	}
}
