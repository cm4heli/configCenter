package com.dcits.cloud.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.pool.DruidDataSource;
import com.dcits.cloud.common.Constants;
import com.dcits.cloud.config.DBFactory;

@Repository
@Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED)
public class JdbcDAO {
	public static Map<String,DataSource> source_map = new HashMap<String, DataSource>();
	public static Map<String,Map<String,String>> source_data = new HashMap<String,Map<String,String>>();
	private Map<String, NamedParameterJdbcTemplate> inceptorJdbcTemplateMap = new HashMap<String, NamedParameterJdbcTemplate>();
	private Map<String, NamedParameterJdbcTemplate> oracleJdbcTemplateMap = new HashMap<String, NamedParameterJdbcTemplate>();

	
	@Autowired
	private NamedParameterJdbcTemplate aipJdbcTemplate;
	
	@Autowired
	private JdbcTemplateImpl jdbcTemplate  ;

	@Autowired
	public void setInceptorDataSource(DBFactory dbFactory) {
		Set<String> keys = dbFactory.getDbMap().keySet();
		Iterator<String> it = keys.iterator();
		while (it.hasNext()) {
			String key = it.next();
			DataSource ds = (DataSource) dbFactory.getDbMap().get(key);
			NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(ds);
			inceptorJdbcTemplateMap.put(key, namedParameterJdbcTemplate);
		}

	}

	// @Autowired
	// public void setOracleDataSource(OracleDbFactory oracleDbFactory) {
	// Set<String> keys = oracleDbFactory.getDbMap().keySet();
	// Iterator<String> it = keys.iterator();
	// while (it.hasNext()) {
	// String key = it.next();
	// DataSource ds = (DataSource) oracleDbFactory.getDbMap().get(key);
	// NamedParameterJdbcTemplate namedParameterJdbcTemplate = new
	// NamedParameterJdbcTemplate(ds);
	// oracleJdbcTemplateMap.put(key, namedParameterJdbcTemplate);
	// }
	//
	// }
 
	public NamedParameterJdbcTemplate getInceptorJdbcTemplate(String key) {
		return inceptorJdbcTemplateMap.get(key);
	}

	public NamedParameterJdbcTemplate getAipJdbcTemplate() {
		return aipJdbcTemplate;
	}

	public Map<String, NamedParameterJdbcTemplate> getInceptorJdbcTemplateMap() {
		return inceptorJdbcTemplateMap;
	}

	public NamedParameterJdbcTemplate getDbmsConnection(String identify) throws Exception {
		return new NamedParameterJdbcTemplate(reLoadDataSource(identify));
		// }
	}

	public void closeDbmsConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private DataSource reLoadDataSource(String identify) throws Exception {

		DruidDataSource datasource = new DruidDataSource();
		Map<String, String> map = new HashMap<String, String>();
		Connection connect = null;
		Statement state = null;
		String sql = "select local_data.*,d.dict_value from (select a.info_code,a.info_name,b.value_attr_id,b.value_text,c.attr_name,c.attr_code from (select m.* from md_info m,mdm_model mdm  where m.info_model_id=mdm.model_id and mdm.model_code='Schema') a,md_attr_value b,(select ma.* from mdm_attr ma,mdm_model mdm where ma.attr_model_id=mdm.model_id and  mdm.model_code='Schema' ) c where a.info_id=b.value_info_id and c.attr_id = b.value_attr_id and a.info_code='"
				+ identify + "') local_data left join fw_dict_info d on d.dict_id=local_data.value_text";
		try {
			connect = jdbcTemplate.getDataSource().getConnection();
			state = connect.createStatement();
			ResultSet rs = state.executeQuery(sql);
			while (rs.next()) {

				if ("dbtype".equals(rs.getString("ATTR_CODE"))) {
					map.put(rs.getString("ATTR_CODE"), rs.getString("DICT_VALUE"));
				} else {
					map.put(rs.getString("ATTR_CODE"), rs.getString("VALUE_TEXT"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if(null != connect){
				connect.close();
			}
		}
		if (map.size() == 0) {
			throw new Exception("数据库无法连接【未获取到数据库信息】，请维护元数据");
		}
		if (map.get("sid") == null) {
			throw new Exception("数据库无法连接连接【数据库标识缺失】");
		}
		
		if(source_map.containsKey(identify)){
			if(this.compareDataSource(source_data.get(identify),map)){
				return source_map.get(identify);
			}
		}
		String url = "jdbc:oracle:" + "thin:@" + map.get("ip") + ":" + map.get("port") + ":" + map.get("sid");
		datasource.setUrl(url);
		datasource.setUsername(map.get("account"));
		datasource.setPassword(map.get("password"));
		datasource.setInitialSize(Constants.initialSize);
		datasource.setMinIdle(Constants.minIdle);
		datasource.setMaxActive(Constants.maxActive);
		datasource.setMaxWait(Constants.setMaxWait);
		datasource.setTimeBetweenConnectErrorMillis(Constants.timeBetweenEvictionRunsMillis);
		datasource.setMinEvictableIdleTimeMillis(Constants.minEvictableIdleTimeMillis);
		datasource.setValidationQuery(Constants.validationQuery);
		datasource.setTestWhileIdle(Constants.testWhileIdle);
		datasource.setTestOnBorrow(Constants.testOnBorrow);
		datasource.setTestOnReturn(Constants.testOnReturn);
		datasource.setPoolPreparedStatements(Constants.poolPreparedStatements);
//		datasource.setMaxStatements(0);
//		datasource.setJdbcUrl(url);
//		datasource.setUser(map.get("account"));
//		datasource.setPassword(map.get("password"));
//		datasource.setDriverClass("oracle.jdbc.driver.OracleDriver");
//		datasource.setMaxPoolSize(5);
//		datasource.setMinPoolSize(2);
		source_map.put(identify, datasource);
		source_data.put(identify, map);
		return datasource;
	}

	private Boolean compareDataSource(Map<String, String> sourceMap, Map<String, String> map) {
		
		for(String key : sourceMap.keySet()){
			
			if(!map.containsKey(key)){
				return false;
			}
			if(null == sourceMap.get(key)){
				if(null != map.get(key) ){
					return false;
				}
			}
			if(null != sourceMap.get(key) && !sourceMap.get(key).equals(map.get(key))){
				return false;
			}
		}
		
		return true;
	}

}
