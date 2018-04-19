package com.dcits.cloud.config;

import java.util.HashMap;
import java.util.Map;


public class OracleDbFactory {

	Map<String, Object> dbMap = new HashMap<String, Object>();

	public Map<String, Object> getDbMap() {
		return dbMap;
	}

	public void setDbMap(Map<String, Object> dbMap) {
		this.dbMap = dbMap;
	}
}
