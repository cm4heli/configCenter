package com.dcits.cloud.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.dcits.cloud.utils.Utils;

@Repository("sqlModelDAO")
public class SqlModelDAO {

	@Autowired
	private JdbcDAO jdbcDAO;
	
	public List<Map<String,Object>> getSqlModel(){
		
		List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();  
		
		String sql = "select * from SQL_MODEL_CONFIG";
		Map<String,String> paramMap = new HashMap<String,String>();
		return jdbcDAO.getAipJdbcTemplate().queryForList(sql, paramMap);
	}
	
	
	/**
	 * 保存模板
	 * @param config
	 * @author 陈明
	 * 2017年1月4日
	 */
	public int saveSqlModel(Map<String,Object> config){
		
		if(StringUtils.isEmpty(config.get("id"))){
			config.put("id", Utils.generateKey());
			String sql = "insert into SQL_MODEL_CONFIG values(:id,:name,:desc,:info,:type,:bl)";
			return jdbcDAO.getAipJdbcTemplate().update(sql, config);
		}
		
		String sql = "update SQL_MODEL_CONFIG set MODEL_NAME=:name,MODEL_DESC=:desc,MODEL_INFO=:info,type=:type,BL=:blwhere ID=:id";
		int i = jdbcDAO.getAipJdbcTemplate().update(sql, config);
		return i;
	}
}
