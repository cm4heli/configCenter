package com.dcits.cloud.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.dcits.cloud.common.Constants;
import com.dcits.cloud.common.GlobleCache;
import com.dcits.cloud.model.IRuleConfig;
import com.dcits.cloud.model.ResultStatus;
import com.dcits.cloud.model.RuleConfig;
import com.dcits.cloud.model.RuleParam;
import com.dcits.cloud.model.RuleRepo;
import com.dcits.cloud.utils.Utils;

@Repository("configDAO")
public class ConfigDAO {
	@Autowired
	Environment env;

	@Autowired
	private JdbcDAO jdbcDAO;

	public ResultStatus save(IRuleConfig iRuleConfig) {
		RuleConfig config = (RuleConfig) iRuleConfig;
		if (StringUtils.isEmpty(config.getId())) {
			IRuleConfig oldConfig = null;
			oldConfig = selectOneByUrl(config.getServiceAddr());

			if (oldConfig != null) {
				// 服务地址已经存在，请重新编辑
				return new ResultStatus("服务地址已经存在，请重新编辑!");
			}
		} else {
			IRuleConfig oldConfig = (RuleConfig) selectOneById(config.getId());
			if (!((RuleConfig) oldConfig).getServiceAddr().equals(
					config.getServiceAddr())) {
				oldConfig = selectOneByUrl(config.getServiceAddr());
				if (oldConfig != null) {
					// 服务地址已经存在，请重新编辑
					return new ResultStatus("服务地址已经存在，请重新编辑!");
				}
			}
		}
		
		GlobleCache.removeConfig(config.getServiceAddr());
		saveConfig(config);
		if (config.getRuleRepo() != null) {
			config.getRuleRepo().setConfigId(config.getId());
			saveConfig(config.getRuleRepo());
			if (config.getRuleRepo().getRuleParams() != null) {
				MapSqlParameterSource namedParameters = new MapSqlParameterSource();
				jdbcDAO.getAipJdbcTemplate().update(
						"delete from RULE_PARAM where REPO_ID='"
								+ config.getRuleRepo().getId() + "'",
						namedParameters);
				for (RuleParam param : config.getRuleRepo().getRuleParams()) {
					param.setRepoId(config.getRuleRepo().getId());
					saveConfig(param);
				}
			}
		}
		return new ResultStatus();
	}
	/**
	 * status  0 表示未注册，1表示注册成功
	 * @param ruleConfigId
	 * @param status
	 */
	public Integer updateStatus(String ruleConfigId,Integer status){
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("REGISTER_STATUS",status);
		String sql = "update RULE_CONFIG set  REGISTER_STATUS=:REGISTER_STATUS where ID='"+ruleConfigId+"'";
		return jdbcDAO.getAipJdbcTemplate().update(sql, namedParameters);
	}

	private Integer saveConfig(RuleConfig config) {
		String sql = null;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("SERVICE_NAME", config.getServiceName());
		namedParameters.addValue("SERVICE_ADDR", config.getServiceAddr());
		namedParameters.addValue("AUTH_TYPE", config.getAuthType());
		namedParameters.addValue("LOG_FLAG", config.getLogFlag());
		namedParameters.addValue("SERVICE_DESC", config.getServiceDesc());
		namedParameters.addValue("CREATE_DATE", new Timestamp(new Date().getTime()));
		namedParameters.addValue("UPDATE_DATE", new Timestamp(new Date().getTime()));
		namedParameters.addValue("OP_FLAG", config.getOpFlag());
		namedParameters.addValue("REGISTER_STATUS", config.getRegisterStatus());
		if (StringUtils.isEmpty(config.getId())) { 
			config.setId(Utils.generateKey());
			namedParameters.addValue("ID", config.getId());
			sql = "insert into RULE_CONFIG values(:ID,:SERVICE_NAME,:SERVICE_ADDR,:AUTH_TYPE,:LOG_FLAG,:SERVICE_DESC,:CREATE_DATE,:UPDATE_DATE,:OP_FLAG,:REGISTER_STATUS)";

		} else {
			namedParameters.addValue("ID", config.getId());
			sql = "update RULE_CONFIG set SERVICE_NAME=:SERVICE_NAME,SERVICE_ADDR=:SERVICE_ADDR,AUTH_TYPE=:AUTH_TYPE,LOG_FLAG=:LOG_FLAG,SERVICE_DESC=:SERVICE_DESC,UPDATE_DATE=:UPDATE_DATE where ID=:ID";
		}
		return jdbcDAO.getAipJdbcTemplate().update(sql, namedParameters);
	}

	private Integer saveConfig(RuleRepo config) {
		String sql = null;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("SEARCH_TYPE", config.getSearchType());
		namedParameters.addValue("NAME_SPACE", config.getNameSpace());
		namedParameters.addValue("TABLE_NAME", config.getTableName());
		namedParameters.addValue("CREATE_DATE", config.getCreateDate());
		namedParameters.addValue("UPDATE_DATE", config.getUpdateDate());
		namedParameters.addValue("SQL_SCRIPT", config.getSqlScript());
		namedParameters.addValue("CONFIG_ID", config.getConfigId());
		if (StringUtils.isEmpty(config.getId())) {
			config.setId(Utils.generateKey());
			namedParameters.addValue("ID", config.getId());
			sql = "insert into RULE_REPO values(:ID,:SEARCH_TYPE,:NAME_SPACE,:TABLE_NAME,:CREATE_DATE,:UPDATE_DATE,:SQL_SCRIPT,:CONFIG_ID)";
		} else {
			namedParameters.addValue("ID", config.getId());
			sql = "update RULE_REPO set SEARCH_TYPE=:SEARCH_TYPE,NAME_SPACE=:NAME_SPACE,TABLE_NAME=:TABLE_NAME,UPDATE_DATE=:UPDATE_DATE,SQL_SCRIPT=:SQL_SCRIPT where ID=:ID";
		}
		return jdbcDAO.getAipJdbcTemplate().update(sql, namedParameters);
	}

	private Integer saveConfig(RuleParam config) {
		String sql = null;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("REQUEST_TYPE", config.getRequestType());
		namedParameters.addValue("PARAM_NAME", config.getParamName());
		namedParameters.addValue("COLUMN_NAME", config.getColumnName());
		namedParameters.addValue("PARAM_TYPE", config.getParamType());
		namedParameters.addValue("ZH_NAME", config.getZhName());
		namedParameters.addValue("DEFAULT_VALUE", config.getDefaultValue());
		namedParameters.addValue("EXPRESSTION", config.getExpresstion());
		namedParameters.addValue("CREATE_DATE", config.getCreateDate());
		namedParameters.addValue("UPDATE_DATE", config.getUpdateDate());
		namedParameters.addValue("REPO_ID", config.getRepoId());
		if (StringUtils.isEmpty(config.getId())) {
			config.setId(Utils.generateKey());
		}
		namedParameters.addValue("ID", config.getId());
		sql = "insert into RULE_PARAM values(:ID,:REQUEST_TYPE,:PARAM_NAME,:COLUMN_NAME,:PARAM_TYPE,:ZH_NAME,:DEFAULT_VALUE,:EXPRESSTION,:CREATE_DATE,:UPDATE_DATE,:REPO_ID)";
		return jdbcDAO.getAipJdbcTemplate().update(sql, namedParameters);
	}

	/**
	 * 根据配置ID查询唯一RuleConfig
	 * 
	 * @param ruleConfigId
	 * @return
	 */
	public IRuleConfig selectOneById(String ruleConfigId) {
		MapSqlParameterSource configParameters = new MapSqlParameterSource();
		configParameters.addValue("ID", ruleConfigId);
		IRuleConfig config = (RuleConfig) jdbcDAO
				.getAipJdbcTemplate()
				.queryForObject("select * from RULE_CONFIG where  ID=:ID",
						configParameters,
						new BeanPropertyRowMapper<RuleConfig>(RuleConfig.class));
		MapSqlParameterSource repoParameters = new MapSqlParameterSource();
		repoParameters.addValue("CONFIG_ID", ruleConfigId);
		RuleRepo repo = (RuleRepo) jdbcDAO.getAipJdbcTemplate().queryForObject(
				"select * from RULE_REPO where  CONFIG_ID=:CONFIG_ID",
				repoParameters,
				new BeanPropertyRowMapper<RuleRepo>(RuleRepo.class));
		config.addRuleRepo(repo);
		if (repo != null) {
			List<RuleParam> ruleParams = jdbcDAO.getAipJdbcTemplate().query(
					"select * from RULE_PARAM where  REPO_ID='" + repo.getId()
							+ "'",
					new BeanPropertyRowMapper<RuleParam>(RuleParam.class));
			config.addRuleParams(ruleParams);
		}
		return config;
	}

	/**
	 * 根据配置URL查询唯一RuleConfig
	 * 
	 * @param url
	 * @return
	 */
	public IRuleConfig selectOneByUrl(String url) {
		//先从缓存取数据
		if(null != GlobleCache.getConfig(url)){
			return GlobleCache.getConfig(url);
		}
		MapSqlParameterSource configParameters = new MapSqlParameterSource();
		// 查询主配置
		IRuleConfig config = null;
		try {
			config = (RuleConfig) jdbcDAO.getAipJdbcTemplate().queryForObject(
					"select * from RULE_CONFIG where  SERVICE_ADDR like '%"
							+ url + "'", configParameters,
					new BeanPropertyRowMapper<RuleConfig>(RuleConfig.class));
		} catch (Exception e) {
			return null;
		}
		if (config != null) {
			// 查询库表
			MapSqlParameterSource repoParameters = new MapSqlParameterSource();
			repoParameters.addValue("CONFIG_ID", ((RuleConfig) config).getId());
			RuleRepo repo = (RuleRepo) jdbcDAO
					.getAipJdbcTemplate()
					.queryForObject(
							"select * from RULE_REPO where  CONFIG_ID=:CONFIG_ID",
							repoParameters,
							new BeanPropertyRowMapper<RuleRepo>(RuleRepo.class));
			config.addRuleRepo(repo);
			// 查询参数
			if (repo != null) {
				List<RuleParam> ruleParams = jdbcDAO.getAipJdbcTemplate()
						.query("select * from RULE_PARAM where  REPO_ID='"
								+ repo.getId() + "'",
								new BeanPropertyRowMapper<RuleParam>(
										RuleParam.class));
				config.addRuleParams(ruleParams);
			}
		}
		GlobleCache.setConfig(url, config);
		return config;
	}

	/**
	 * 分页查询配置
	 * 
	 * @param searchConfig
	 * @param startNum
	 * @param pageSize
	 * @return
	 */
	public List<RuleConfig> selectList(String searchCondition, Long startNum,
			Long pageSize) {
		MapSqlParameterSource configParameters = new MapSqlParameterSource();
		String sql = null;
		List<RuleConfig> ruleConfigs = null;
		if (env.getProperty("jdbc.driverClassName").toLowerCase()
				.contains("oracle")) {
			configParameters.addValue("startNum", startNum);
			configParameters.addValue("pageSize", startNum+pageSize);
			sql = "SELECT * FROM RULE_CONFIG WHERE 1=1  ";
			if (searchCondition != null && searchCondition.trim() != "") {
				sql += "  and ( ";
				sql += " SERVICE_NAME like '%" + searchCondition + "%'";
				sql += " or SERVICE_DESC like '%" + searchCondition + "%'";
				sql += " or SERVICE_ADDR like '%" + searchCondition + "%'";
				sql += " ) ";
				sql += " order by UPDATE_DATE desc ";
			}else{
				//sql = "SELECT * FROM  ( SELECT ROWNUM AS rowno, t.*  FROM RULE_CONFIG  t  WHERE  1=1  AND ROWNUM <=:pageSize  ) table_alias  WHERE 1=1   ";
				//sql += "AND table_alias.rowno > :startNum  order by UPDATE_DATE desc";
				sql = "SELECT * FROM RULE_CONFIG WHERE 1=1 ";
				sql += "  order by UPDATE_DATE desc";
			}
			
			ruleConfigs = jdbcDAO.getAipJdbcTemplate().query(sql,
					configParameters,
					new BeanPropertyRowMapper<RuleConfig>(RuleConfig.class));
		} else if (env.getProperty("jdbc.driverClassName").toLowerCase()
				.contains("mysql")) {
			sql = "SELECT * FROM  RULE_CONFIG  where 1=1 ";
			if (searchCondition != null && searchCondition.trim() != "") {
				sql += "  and ( ";
				sql += " SERVICE_NAME like '%" + searchCondition + "%'";
				sql += " or SERVICE_DESC like '%" + searchCondition + "%'";
				sql += " or SERVICE_ADDR like '%" + searchCondition + "%'";
				sql += " ) ";
			}
			sql += " limit :startNum,:pageSize order by UPDATE_DATE desc";
			ruleConfigs = jdbcDAO.getAipJdbcTemplate().query(sql,
					configParameters,
					new BeanPropertyRowMapper<RuleConfig>(RuleConfig.class));
		}
		//searchCondition != null && searchCondition.trim() != "" && ruleConfigs != null && ruleConfigs.size()>0 &&
		if ( env.getProperty("jdbc.driverClassName").toLowerCase()
				.contains("oracle") ) {
			List<RuleConfig> searchList = new ArrayList<RuleConfig>();
			Integer i = startNum.intValue()-1<0?0:startNum.intValue()-1;
			i=i>0?i+1:i;
			for(int n  = i ;n<=(i+pageSize-1);n++){
				if(n<ruleConfigs.size())
				searchList.add(ruleConfigs.get(n));
			}
			return searchList;
		}
		return ruleConfigs;
	}

	public Long selectCount(String searchCondition) {
		MapSqlParameterSource configParameters = new MapSqlParameterSource();
		String sql = "SELECT count(*) FROM  RULE_CONFIG  where 1=1 ";
		if (searchCondition != null && searchCondition.trim() != "") {
			sql += "  and ( ";
			sql += " SERVICE_NAME like '%" + searchCondition + "%'";
			sql += " or SERVICE_DESC like '%" + searchCondition + "%'";
			sql += " or SERVICE_ADDR like '%" + searchCondition + "%'";
			sql += " ) ";
		}
		Object obj = jdbcDAO.getAipJdbcTemplate().queryForObject(sql,
				configParameters, Integer.class);
		if (obj != null) {
			return Long.valueOf(obj.toString());
		}
		return 0L;
	}

	@SuppressWarnings("unchecked")
	public Object loadData() {

		String sql = "select m.*,n.info_code as p_info_code,n.info_name as p_info_name,g.info_code as g_info_code,g.info_name as g_info_name from MD_INFO m "
              + "left join MD_INFO n on n.info_id=m.info_parent_id " 
       
              + " left join MD_INFO g on g.info_id=n.info_parent_id "
              + " where m.info_parent_id in "
       		  + " (select t.info_id from MD_INFO t where t.info_parent_id in "   
              +	"  (select info_id from MD_INFO where info_code='hbase')) ";
		// hbase表查询
		String hbasesql = "select t1.*,t1.DESCRIPTION as column_desc,t2.*,t2.DESCRIPTION as table_desc from SYS_COLUMN_INFO t1,SYS_TABLE_INFO t2 where t1.table_id=t2.table_id and t2.type="
				+ Constants.search_type_hbase;  
		List<Object> hbaseResultList = (List<Object>) jdbcDAO
				.getAipJdbcTemplate().query(sql, new ResultSetExtractor() {
					@Override
					public Object extractData(ResultSet rs)
							throws SQLException, DataAccessException {

						List<Object> tableList = new ArrayList<Object>();
						while (rs.next()) {
							// 第一层database的填充
							String tableName = rs.getString("P_INFO_CODE");
							String tableDesc = rs.getString("P_INFO_NAME");
							List<Map<String, Object>> columnList = null;
							for (Object obj : tableList) {
								Map<String, Object> tableMap = (Map<String, Object>) obj;
								if (tableName.equals(tableMap.get("tableName")
										.toString())) {
									columnList = (List<Map<String, Object>>) tableMap
											.get("columnList");
								}
							}
							if (null == columnList) {
								columnList = new ArrayList<Map<String, Object>>();
								Map<String, Object> newtableMap = new HashMap<String, Object>();
								newtableMap.put("tableName", tableName);
								newtableMap.put("table_desc", tableDesc);
								newtableMap.put("columnList", columnList);
								tableList.add(newtableMap);
							}
							Map<String, Object> columnMap = new HashMap<String, Object>();
							columnMap.put("COLUMN_NAME",
									rs.getString("INFO_CODE"));
							columnMap.put("COLUMN_ID",
									rs.getString("INFO_ID"));
							columnMap.put("TABLE_NAME",
									rs.getString("P_INFO_CODE"));
							columnMap.put("column_desc",
									rs.getString("INFO_NAME"));
							columnList.add(columnMap);
							continue;
						}
						return tableList;
					}
				});
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("hbase", hbaseResultList);
		return returnMap;

	}

	/**
	 * 根据id删除配置
	 * 
	 * @param id
	 * @author 陈明 2016年11月29日
	 */
	public ResultStatus removeConfig(String id) {

		
		// 删除rule_repo
		RuleConfig ruleConfig = (RuleConfig) this.selectOneById(id);
		//删除缓存
		GlobleCache.removeConfig(ruleConfig.getServiceAddr());
		String reId = ruleConfig.getRuleRepo().getId();
		
		// 删除config
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		int configCode = jdbcDAO.getAipJdbcTemplate().update(
				"delete from RULE_CONFIG where ID='" + id + "'",
				namedParameters);
		jdbcDAO.getAipJdbcTemplate().update(
				"delete from RULE_REPO where CONFIG_ID='" + id + "'",
				namedParameters);
		// 删除rule_param
		jdbcDAO.getAipJdbcTemplate().update(
				"delete from RULE_PARAM where REPO_ID='" + reId + "'",
				namedParameters);

		return new ResultStatus();
	}
	
	
	
	/**
	 * 测试sql
	 * 
	 * @author 陈明
	 * 2017年1月20日
	 */
	public void testSQL(){
		
		
		
	}

}
