package com.dcits.cloud.dao.transwarp;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.dcits.cloud.common.Constants;
import com.dcits.cloud.dao.JdbcDAO;

@Repository("inceptorDAO")
public class InceptorDAO {

	@Autowired
	private JdbcDAO jdbcDAO;

	/**
	 * 根据配置ID查询唯一RuleConfig
	 * 
	 * @param ruleConfigId
	 * @return
	 */
	public List<Map<String, Object>> list(String nameSpace,String sql) {
		MapSqlParameterSource configParameters = new MapSqlParameterSource();
		if(sql.contains("contains")){
			jdbcDAO.getInceptorJdbcTemplate(nameSpace).update("set ngmr.exec.mode=local",configParameters);
		}
		return jdbcDAO.getInceptorJdbcTemplate(nameSpace).queryForList(sql, configParameters);
	}
	
	public List<Map<String, Object>> list(Integer searchType,String nameSpace,String sql,MapSqlParameterSource configParameters ) {
		if(Constants.search_type_es ==  searchType ){
			jdbcDAO.getInceptorJdbcTemplate(nameSpace).update("set ngmr.exec.mode=local",configParameters);
		}
		return jdbcDAO.getInceptorJdbcTemplate(nameSpace).queryForList(sql, configParameters);
	}
	
	
}
