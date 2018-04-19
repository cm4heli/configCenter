package com.dcits.cloud.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.cloud.common.Constants;
import com.dcits.cloud.dao.CacheDAO;

@Service("cacheService")
public class CacheService {
	@Autowired
	private CacheDAO cacheDAO;
	
	public List<Map<String,Object>> getAttribute(){
		List<Map<String,Object>> attrList = new ArrayList<>();
		List<String> searchModes = cacheDAO.getSearchModes();
		
		Map<String,Object> attrMap = new HashMap<String,Object>();
		for(String mode:searchModes){
//			Map<String,Object> attrMap = new HashMap<String, Object>();
			if(String.valueOf(Constants.search_type_hbase).equals(mode)){
				//cacheDAO.getHbaseTableNames()
				attrMap.put("hbase", mode);
				List<String> hbaseTableList = cacheDAO.getHbaseTableNames();
				List<Map<String,Object>> tableList = new ArrayList<Map<String,Object>>();
				for(String tableName : hbaseTableList){
					Map<String,Object> tableMap = new HashMap<String,Object>();
					tableMap.put("tableName", tableName);
					
//					tableMap.put("column", cacheDAO.getTableColumn("", tableName));
					
					tableList.add(tableMap);
				}
				attrMap.put("hbase", tableList);
 			}else{
				attrMap.put("inceptor", mode);
				List<String> dataBase= cacheDAO.getInceptorDatabase();
				List<Map<String,Object>> databaseList = new ArrayList<Map<String,Object>>();
				for(String dataBaseName : dataBase){
					Map<String,Object> databaseMap = new HashMap<String,Object>();
					databaseMap.put("database", dataBaseName);
					List<Map<String,Object>> tableList = cacheDAO.getInceptorTables(dataBaseName);
					for(Map<String,Object> tableMap : tableList){
						Map<String,Object> subtableMap = new HashMap<String,Object>();
						String tab_name = tableMap.get("tab_name").toString();
						subtableMap.put("tab_name", tab_name);
						List<Map<String, Object>> columnList = cacheDAO.getTableColumn(dataBaseName, tab_name);
					}
					databaseMap.put("tablelist", tableList);
					databaseList.add(databaseMap);
					int i = 0;
				}
			}
			
			attrList.add(attrMap);
			
		}
		
		return attrList;
	}
}
