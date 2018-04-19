package com.dcits.cloud.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dcits.cloud.common.Constants;
import com.dcits.cloud.dao.hbase.HbaseDAO;

@Repository("cacheDAO")
public class CacheDAO {
	@Autowired
	private JdbcDAO jdbcDAO;
	@Autowired
	private HbaseDAO hbaseDAO;
	
	public List<String> getSearchModes(){
		List<String>  searchList = new ArrayList<>();
		searchList.add(String.valueOf(Constants.search_type_hbase));
		searchList.add(String.valueOf(Constants.search_type_inceptor));
		return  searchList;
	}
	
	/**
	 * 获取hbase 表
	 * @return
	 */
	public List<String> getHbaseTableNames(){
		return hbaseDAO.getTabesList();
	}
	
	/**
	 * 获取inceptor数据库
	 * @return
	 */
	public List<String>  getInceptorDatabase(){
		List<String> databaseList = new ArrayList<String>();
		Set<String> databaseSet = jdbcDAO.getInceptorJdbcTemplateMap().keySet();
		Iterator it = databaseSet.iterator();
		while(it.hasNext()){
			databaseList.add(it.next().toString());
		}
		return databaseList;
	} 
	/**
	 * 获取inceptor库对应的表
	 * @param key : 数据名称
	 * @return
	 * tab_name
	 */
	public List<Map<String,Object>> getInceptorTables(String databaseName){
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		NamedParameterJdbcTemplate  jdbc = jdbcDAO.getInceptorJdbcTemplate(databaseName);
		List<Map<String,Object>> tableList = jdbc.queryForList(" show tables ", namedParameters);
		//tab_name
		return tableList;
	}
	/**
	 * 获取inceptor 表对应的列名称
	 * @param key：数据库名称
	 * @param tableName：表名称
	 * col_name
	 * @return
	 */
	public List<Map<String,Object>> getTableColumn(String databaseName,String tableName){
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		NamedParameterJdbcTemplate  jdbc = jdbcDAO.getInceptorJdbcTemplate(databaseName);
		List<Map<String,Object>> columnList = jdbc.queryForList(" desc "+ tableName , namedParameters);
		//col_name
		return columnList;
	}
}
