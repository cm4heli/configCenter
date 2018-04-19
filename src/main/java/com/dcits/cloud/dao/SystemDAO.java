package com.dcits.cloud.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dcits.cloud.common.Constants;

@Repository
@Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED)
public class SystemDAO {

	@Autowired
	private JdbcDAO jdbcDAO;
	
	
	/**
	 * 改变IP和端口
	 * @param ip
	 * @param port
	 * @param phone3 
	 * @param phone2 
	 * @param phone1 
	 * @return
	 */
	public void setData(String ip,int port){
		
		Map<String,Object> dataMap = getSystemData();
		
		String sql = "update API_SYSTEM_DATA set ip=:ip,port=:port where 1=1";
		if(null == dataMap ||dataMap.size() == 0){
			
			sql = "insert into API_SYSTEM_DATA values(:ip,:port) ";
		}
		
		//插入新的ip 和端口
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("ip", ip);
		namedParameters.addValue("port", port);
		jdbcDAO.getAipJdbcTemplate().update(sql, namedParameters);
		
		String url = "http://" + ip + ":" + port + "/cloudservice/api/";
		//修改代理地址
		String url_sql = "update ESB_ROUTE_INFO set SRV_URL=concat('" + url +"',PUBLISH_URL) where SEARCH_TYPE is not null";
		MapSqlParameterSource namedParameters2 = new MapSqlParameterSource();
		jdbcDAO.getAipJdbcTemplate().update(url_sql, namedParameters2);
	}
	
	
	/**
	 * 获取系统信息，包括IP和端口
	 * @return
	 */
	public Map<String,Object> getData(){
		
		String sql = "select t.user_tel from FW_USER_INFO t where t.login_name='admin'";
		Map<String,Object> sys_data = null;
		try{
			sys_data = jdbcDAO.getAipJdbcTemplate().queryForMap(sql, new MapSqlParameterSource());
		}
		catch(Exception e){
			return null;
		}
		return sys_data;
	}
	
	
	
//	public String initURL(){
//		String sql = "select * from API_SYSTEM_DATA";
//		Map<String,Object> sys_data = null;
//		try{
//			sys_data = jdbcDAO.getAipJdbcTemplate().queryForMap(sql, new MapSqlParameterSource());
//			
//		}
//		catch(Exception e){
//			return "";
//		}
//		
//		String url = "http://" + sys_data.get("IP") + ":" + sys_data.get("PORT") + "/cloudservice/api";
//		Constants.AIP_SERVICE_URL = url;
//		return url;
//	}


	public Map<String,Object> getSystemData() {
		String sql = "select * from API_SYSTEM_DATA";
		Map<String,Object> sys_data = null;
		try{
			sys_data = jdbcDAO.getAipJdbcTemplate().queryForMap(sql, new MapSqlParameterSource());
		}
		catch(Exception e){
			return null;
		}
		return sys_data;
	}
}
