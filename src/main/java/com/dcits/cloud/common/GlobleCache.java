package com.dcits.cloud.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;

import com.dcits.cloud.dao.JdbcDAO;
import com.dcits.cloud.model.IRuleConfig;

public class GlobleCache {
  // public static Map<String,Object> databases = new HashMap<String,Object>();
	@Autowired
	private JdbcDAO jdbcDAO;
	//存rowkey
	private static Map<String,String> rowkeyMap = new HashMap<String,String>();
	
	//存放config对象，key为url
	private static Map<String,IRuleConfig> configMap = new HashMap<String,IRuleConfig>();
	
	/**
	 * 根据表名查询rowkey
	 * @param tableName
	 * @return
	 * @author 陈明
	 * 2016年11月23日
	 */
	public static void setRowkey(Map<String,String> rowkeyMap){
		GlobleCache.rowkeyMap = rowkeyMap;
	}
	
	/**
	 * 根据表名查询rowkey
	 * @param tableName
	 * @return
	 * @author 陈明
	 * 2016年11月23日
	 */
	public static String getRowkeyByName(String tableName){
		return rowkeyMap.get(tableName);
	}
	
	/**
	 * 存放config
	 * @param url
	 * @param config
	 * @author 陈明
	 * 2016年11月30日
	 */
	public static void setConfig(String url,IRuleConfig config){
		synchronized (configMap) {
			configMap.put(url, config);
		}
	}
	
	
	public static IRuleConfig getConfig(String url){
		synchronized (configMap) {
		if(configMap.containsKey(url)){
			return configMap.get(url);
		}
		
		return null;
		}
	}
	
	
	public static void removeConfig(String url){
		synchronized (configMap) {
			
			String removeKey = "";
			for(Entry<String,IRuleConfig> entry : configMap.entrySet()){
				String key = entry.getKey();
				if(url.contains(key)){
					removeKey = key;
				}
			}
			configMap.remove(removeKey);
			
		}
	}
	
	
	
}
