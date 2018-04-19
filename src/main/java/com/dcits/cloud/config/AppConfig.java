package com.dcits.cloud.config;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

//import org.apache.ibatis.session.SqlSessionFactory;
//import org.apache.ibatis.session.SqlSessionFactoryBuilder;
//import org.apache.ibatis.transaction.TransactionFactory;
//import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.alibaba.druid.pool.DruidDataSource;
import com.dcits.cloud.common.Constants;
import com.dcits.cloud.dao.JdbcTemplateImpl;
import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
// @EnableAsync
@EnableScheduling
@PropertySource("classpath:jdbc.properties")
@ImportResource(locations={"classpath:spring-many-datasource-config.xml"})
public class AppConfig {

	@Autowired
	Environment env;

	 @Autowired
	 DataSource aipDataSource;
	 @Autowired
	 DruidDataSource mdDatasource;
//	 @Autowired
//	 RouteManagerContainer routeManagerContainer;

	/**
	 * aip,配置信息，参数信息，数据源，sql脚本
	 * 
	 * @return
	 * @throws PropertyVetoException
	 * @throws SQLException 
	 */
	@Bean(name = "aipDataSource")
	public DataSource oracleJdbcComboPooledDataSource() throws PropertyVetoException, SQLException {
		
		//首先读取是否需要代理模式
		Constants.SSPSERVER_MODE = Integer.valueOf(env.getProperty("sspServer.mode"));
		
		// aip服务代理地址初始化
		if(Constants.SSPSERVER_MODE == 1){
			Constants.PROXY_SERVICE_IP = env.getProperty("publish.ip");
			Constants.PROXY_SERVICE_PORT = env.getProperty("publish.port");
		}
		else{
			Constants.PROXY_SERVICE_IP = env.getProperty("service.ip");
			Constants.PROXY_SERVICE_PORT = env.getProperty("service.port");
		}
		
		Constants.AIP_SERVICE_URL = "http://" + env.getProperty("service.ip") + ":" + env.getProperty("service.port") + "/cloudservice/api";
		
		
		Constants.initialSize = Integer.valueOf(env.getProperty("initialSize"));
		Constants.minIdle =Integer.valueOf(env.getProperty("minIdle"));
		Constants.maxActive =Integer.valueOf(env.getProperty("maxActive"));
		Constants.setMaxWait =Long.valueOf(env.getProperty("maxWait"));
		Constants.timeBetweenEvictionRunsMillis =Long.valueOf(env.getProperty("timeBetweenEvictionRunsMillis"));
		Constants.minEvictableIdleTimeMillis =Long.valueOf(env.getProperty("minEvictableIdleTimeMillis"));
		Constants.validationQuery =env.getProperty("validationQuery");
		Constants.testWhileIdle =Boolean.valueOf(env.getProperty("testWhileIdle"));
		Constants.testOnBorrow =Boolean.valueOf(env.getProperty("testOnBorrow"));
		Constants.testOnReturn =Boolean.valueOf(env.getProperty("testOnReturn"));
		Constants.poolPreparedStatements =Boolean.valueOf(env.getProperty("poolPreparedStatements"));
		
		return mdDatasource;

	}
	
	
	@Bean(name="jdbcTemplate")
	public JdbcTemplateImpl jdbcTemplate() {
		JdbcTemplateImpl ji = new JdbcTemplateImpl();
		ji.setDataSource(this.aipDataSource);
	    return ji;
	}
	
	@Bean(name="aipJdbcTemplate")
	public NamedParameterJdbcTemplate aipJdbcTemplate() {
	    return new NamedParameterJdbcTemplate(this.aipDataSource);
	}
	
	@Bean(name="transactionManager")
	public DataSourceTransactionManager transactionManager() {
		DataSourceTransactionManager mgr  = new DataSourceTransactionManager(this.aipDataSource);
		mgr.setRollbackOnCommitFailure(true);
		return mgr;
	}
	
	
	
	/**
	 * 注册星环大数据inceptor连接数据源
	 * 
	 * @return
	 * @throws PropertyVetoException
	 * @throws SQLException
	 */
	@Bean(name = "dbFactory")
	public DBFactory ComboPooledDataSource() throws PropertyVetoException, SQLException {
		Connection connect = null;
		Statement state = null;

		Map<String,Map<String,String>> tableMap = new HashMap<String,Map<String,String>>();
		String sql = "select local_data.*,d.dict_value from (select a.info_code,a.info_name,b.value_attr_id,b.value_text,c.attr_name,c.attr_code from (select m.* from md_info m,mdm_model mdm  where m.info_model_id=mdm.model_id and mdm.model_code='Schema') a,md_attr_value b,(select ma.* from mdm_attr ma,mdm_model mdm where ma.attr_model_id=mdm.model_id and  mdm.model_code='Schema') c where a.info_id=b.value_info_id and c.attr_id = b.value_attr_id) local_data left join fw_dict_info d on d.dict_id=local_data.value_text";
		try {
			connect = aipDataSource.getConnection();
			state = connect.createStatement();
			ResultSet rs = state.executeQuery(sql);
			while (rs.next()) {
				Map<String, String> map = null;
				if(tableMap.get(rs.getString("INFO_CODE")) == null){
					map = new HashMap<String, String>();
					tableMap.put(rs.getString("INFO_CODE"), map);
				}
				else{
					map = tableMap.get(rs.getString("INFO_CODE"));
				}
				
				if ("dbtype".equals(rs.getString("ATTR_CODE"))) {
					map.put(rs.getString("ATTR_CODE"), rs.getString("DICT_VALUE"));
				} else {
					map.put(rs.getString("ATTR_CODE"), rs.getString("VALUE_TEXT"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (state != null) {
				try {
					state.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (connect != null) {
				try {
					connect.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		DBFactory dbFactory = new DBFactory();
		Map<String, Object> dbMap = new HashMap<String, Object>();
		
		for (Entry<String, Map<String, String>> submap : tableMap.entrySet()) {
			
			Map<String,String> map = submap.getValue();
			if(!"inceptor".equals(map.get("dbtype"))){
				continue;
			}			
//			if(map.get("DATABASE_NAME") == null){
//				System.out.println("初始化数据库【" + submap.getKey() + "】 失败！数据库标识为空");
//				continue;
//			}
			
			
			
			ComboPooledDataSource datasource = new ComboPooledDataSource();
			String url = "jdbc:inceptor2://" + map.get("ip") + ":" + map.get("port") + "/" + map.get("sid");
			datasource.setJdbcUrl(url);
			datasource.setDriverClass(env.getProperty("inceptor.driverClassName"));
			datasource.setMaxPoolSize(
					env.getProperty("maxPoolSize") != null ? Integer.valueOf(env.getProperty("maxPoolSize")) : 5);
			datasource.setMinPoolSize(
					env.getProperty("minPoolSize") != null ? Integer.valueOf(env.getProperty("minPoolSize")) : 1);
			if (env.getProperty("acquireIncrement") != null)
				datasource.setAcquireIncrement(Integer.valueOf(env.getProperty("acquireIncrement")));
			if (env.getProperty("maxIdleTime") != null)
				datasource.setMaxIdleTime(Integer.valueOf(env.getProperty("maxIdleTime")));
			System.out.println("初始化数据库【" + submap.getKey() + "】 成功！！");
			dbMap.put(submap.getKey(), datasource);
		}
		dbFactory.setDbMap(dbMap);
		return dbFactory;
	}

	public static void main(String args[]) throws IOException {
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources("/**/*-mapper.xml");
		System.out.println(resources);
	}

}
