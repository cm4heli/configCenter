package com.dcits.cloud.service;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.dcits.cloud.common.Constants;
import com.dcits.cloud.dao.ConfigDAO;
import com.dcits.cloud.dao.JdbcDAO;
import com.dcits.cloud.dao.transwarp.InceptorDAO;
import com.dcits.cloud.model.IRuleConfig;
import com.dcits.cloud.model.Page;
import com.dcits.cloud.model.ResultStatus;
import com.dcits.cloud.model.RuleConfig;
import com.dcits.cloud.model.RuleParam;
import com.dcits.cloud.model.RuleRepo;
import com.dcits.cloud.utils.Utils;

@Service("ruleConfigService")
public class RuleConfigService {

	@Autowired
	private ConfigDAO configDAO;

	@Autowired
	InceptorDAO inceptorDAO;
	
	@Autowired
	private JdbcDAO jdbcDAO;

	private static final String FIELD_NOT_EXSIT = "sql写入的参数错误";

	/**
	 * 初始化页面
	 * 
	 * @return
	 * @author 陈明 2016年11月7日
	 */
	public Object initPage() {
		return configDAO.loadData();
	}

	/**
	 * 删除或者使得
	 * 
	 * @param data
	 * @return
	 * @author 陈明 2016年11月29日
	 */
//	public Object remove(String id) {
//		
////		ResultStatus result = this.deleteSSP(id);
////		if(result.getHasError()){
////			return result;
////		}
//		return configDAO.removeConfig(id);
//	}
//
//	/**
//	 * 服务数据入库service
//	 * 
//	 * @param data
//	 * @return
//	 * @throws Exception
//	 * @author 陈明 2016年11月8日
//	 */
//	public Object register(String data) throws Exception {
//		IRuleConfig config = buildRuleConfig(data);
//
//		
//		String configId = ((RuleConfig) config).getId();
//		ResultStatus result = configDAO.save(config);
//		if (result.getHasError()) {
//			return result;
//		}
//		if(!StringUtils.isEmpty(configId)){
//			ResultStatus deleteResult =  this.deleteSSP(configId.toString());
//			if(deleteResult.getHasError()){
//				return deleteResult ;
//			}
//		}
//		ResultStatus resultsta = this.sendData(config);
//		if (resultsta.getHasError()) {
//			return resultsta;
//		}
//		
//		return new ResultStatus();
//	}
//	
//	
//	/**
//	 * 保存服务信息
//	 * @param data
//	 * @return
//	 * @author 陈明
//	 * 2016年12月8日
//	 * @throws Exception 
//	 */
//	public Object save(String data) throws Exception{
//		IRuleConfig config = buildRuleConfig(data);
//		ResultStatus result = configDAO.save(config);
//		return result;
//	}

	/**
	 * 发送数据到服务管理平台
	 * 
	 * @param data
	 * @return
	 * @author 陈明 2016年12月7日
	 */
//	private ResultStatus sendData(IRuleConfig iRuleConfig) {
//
//		
//		RuleConfig config = (RuleConfig) iRuleConfig;
//		this.deleteSSP(config.getId());
//		List<RuleParam> params = config.getRuleRepo().getRuleParams();
//		for (RuleParam param : params) {
//			Map<String, Object> paramMap = new HashMap<String, Object>();
//			paramMap.put("routeId", config.getId());
//			paramMap.put("type", param.getRequestType());
//			// 默认能为空
//			paramMap.put("isNull", 0);
//			paramMap.put("defaultValue", param.getParamType());
//			paramMap.put("dataType", "string");
//			paramMap.put("name", param.getParamName());
//			paramMap.put("parameterDesc", param.getZhName());
//			Map<String, Object> responseCode1 = Utils.jsonToMap(Utils.sendPOST(Constants.PARAM_URL, paramMap));
//			if(responseCode1.isEmpty()){
//				return new ResultStatus("服务参数挂接失败" + "(" + "服务连接异常" + ")");
//			}
//			if (!Boolean.valueOf(responseCode1.get("success").toString())) {
//				this.deleteSSP(config.getId());
//				return new ResultStatus("服务参数挂接失败" + "(" + responseCode1.get("msg").toString() + ")");
//			}
//		}
//		
//		
//		Map<String, Object> sendMap = new HashMap<String, Object>();
//		
//		sendMap.put("serviceType", 2);
//		sendMap.put("routeName", config.getServiceName());
//		sendMap.put("resourceId", "");
//		sendMap.put("orgId", "4lEQYM62EeOT-4gqnHrXfQ");
//		String pubUrl = "";
//		String proxyUrl = config.getServiceAddr();
//		String[] proStr = proxyUrl.split("/");
//		if(proStr.length <= 2){
//			pubUrl = proxyUrl;
//		}
//		else{
//			pubUrl = proStr[proStr.length -2] + "/" + proStr[proStr.length -1];
//		}
//		sendMap.put("publishURL", pubUrl);
//		
//		sendMap.put("prxoyURL", proxyUrl);
//		sendMap.put("routeType", "http");
//		sendMap.put("writeLog", config.getLogFlag());
//		sendMap.put("matchOnUriPrefix", 1);
//		sendMap.put("routeDesc", config.getServiceDesc());
//		sendMap.put("isAuth", config.getAuthType());
//		sendMap.put("routeId", config.getId());
//		Map<String, Object> responseCode = Utils.jsonToMap(Utils.sendPOST(Constants.SSP_URL, sendMap));
//
//		if (!Boolean.valueOf(responseCode.get("success").toString())) {
//			return new ResultStatus("服务创建挂接失败" + "(" + responseCode.get("msg").toString() + ")");
//		}
//		
//
//		configDAO.updateStatus(config.getId(), Constants.REGISTER);
//		return new ResultStatus();
//	}
//
//	
//	/**
//	 * 删除ssp总线上的服务
//	 * @param id
//	 * @return
//	 * @author 陈明
//	 * 2016年12月10日
//	 */
//	public ResultStatus deleteSSP(String id){
//		
//		Map<String,Object> paramMap = new HashMap<String,Object>();
//		
//		paramMap.put("routeId", id);
//		Map<String,Object> returnMap = Utils.jsonToMap(Utils.sendPOST(Constants.SERVICE_DELETE_URL, paramMap));
//		if(returnMap.isEmpty()){
//			return new ResultStatus("服务删除挂接失败" + "(" + "访问出错" + ")");
//		}
//		if (!Boolean.valueOf(returnMap.get("success").toString())) {
//			return new ResultStatus("服务删除挂接失败" + "(" + returnMap.get("msg").toString() + ")");
//		}
//		configDAO.updateStatus(id, Constants.NOT_REGISTER);
//		return new ResultStatus();
//	}
//
//	public IRuleConfig buildRuleConfig(String data) throws Exception {
//		if (null == data || "".equals(data)) {
//			throw new Exception("入参为空！");
//		}
//
//		Map<String, Object> dataMap = Utils.jsonToMap(data);
//
//		IRuleConfig config = new RuleConfig();
//		RuleRepo ruleRepo = this.assembleRuleRepo(dataMap);
//		config.init(dataMap);
//		config.addRuleParams(this.getParamList(dataMap));
//		config.addRuleRepo(ruleRepo);
//		return config;
//	}
//
//	/**
//	 * 组装RuleRepo 类
//	 * 
//	 * @param data
//	 * @return
//	 * @author 陈明 2016年11月8日
//	 * @throws IllegalAccessException 
//	 * @throws InstantiationException 
//	 */
//	private RuleRepo assembleRuleRepo(Map<String, Object> dataMap) throws InstantiationException, IllegalAccessException {
//		// 数据库信息及基本信息填充
//		RuleRepo ruleRepo = new RuleRepo();
//		Map<String, Object> tableData = Utils.jsonToMap(dataMap.get("tableData").toString());
//		if (null != dataMap.get("reId") && !"".equals(dataMap.get("reId"))) {
//
//			ruleRepo.setId(dataMap.get("reId").toString());
//		}
//		ruleRepo.setNameSpace(tableData.get("tableSpace").toString());
//		ruleRepo.setSearchType(Integer.valueOf(tableData.get("searchType").toString()));
//		ruleRepo.setSqlScript(dataMap.get("sql").toString());
//		ruleRepo.setTableName(tableData.get("tableName").toString());
//
//		ruleRepo.setRuleParams(this.getParamList(dataMap));
//		return ruleRepo;
//	}

	private List<RuleParam> getParamList(Map<String, Object> dataMap) throws InstantiationException, IllegalAccessException {
		List<RuleParam> ruleList = new ArrayList<RuleParam>();
		List<Map<String, Object>> requestList = Utils.jsonToList(dataMap.get("requestData").toString());

		// 请求参数填充
		for (Map<String, Object> requestMap : requestList) {
			requestMap.put("requestType", Constants.REQUEST_TYPE);
			RuleParam ruleParam = (RuleParam) Utils.jsonToBean(requestMap, RuleParam.class);

			if (null != ruleParam) {
				ruleList.add(ruleParam);
			}
		}

		// 响应参数填充
		List<Map<String, Object>> responseList = Utils.jsonToList(dataMap.get("responseData").toString());
		for (Map<String, Object> responseMap : responseList) {
			responseMap.put("requestType", Constants.RESPONSE_TYPE);
			RuleParam ruleParam = (RuleParam) Utils.jsonToBean(responseMap, RuleParam.class);
			if (null != ruleParam) {
				ruleList.add(ruleParam);
			}
		}

		return ruleList;
	}

	public Page listConfigs(String searchCondtion, Long startPage) {
		Page page = new Page(startPage);
		Long totalCount = configDAO.selectCount(searchCondtion);
		if (totalCount != 0) {
			page.setRecords(totalCount);
			List<RuleConfig> data = configDAO.selectList(searchCondtion, page.getStartIndex(), page.getPageSize());
			page.setData(data);
		}
		return page;
	}

	public RuleConfig getById(String id) {
		return (RuleConfig) configDAO.selectOneById(id);
	}

	/**
	 * 检查sql语句的合法性
	 * 
	 * @param sql
	 * @param requestParam
	 * @return
	 * @author 陈明 2016年12月1日
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws PropertyVetoException 
	 */
	public String checkSQL(String sql, String rowData, String database,int type) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, PropertyVetoException {
 
		Map<String, Object> dataMap = Utils.jsonToMap(rowData);
		
		List<RuleParam> ruleParamList = this.getParamList(dataMap);
		// 检查参数是否正确传入
		Pattern pattern = Pattern.compile(":.*?[%\\s\\']|:.*?$");
		Matcher matcher = pattern.matcher(sql);
		MapSqlParameterSource configParameters = new MapSqlParameterSource();
		List<String> matchList = new ArrayList<String>();
		while (matcher.find()) {
			String str = matcher.group();
			if(str.contains(":mm:ss")){
				continue;
			}
			str = str.replaceAll("[\\:%\\s\\']", "");
			matchList.add(str);
		}
		
		List<String> paramList = new ArrayList<String>();
		for (int i = 0; i < ruleParamList.size(); i++) {
			RuleParam ruleParam = ruleParamList.get(i);
			if (ruleParam.getRequestType() == Constants.REQUEST_TYPE) {
				paramList.add(ruleParam.getParamName());
			}
		}
		for(String str : matchList){
			if(!paramList.contains(str)){
				return FIELD_NOT_EXSIT + ":" + str;
			}
			for (int i = 0; i < ruleParamList.size(); i++) {
				RuleParam ruleParam = ruleParamList.get(i);
				if (ruleParam.getRequestType() == Constants.RESPONSE_TYPE) {
					continue;
				}
//				if (str.equals(ruleParam.getColumnName())) {
//					break;
//				}
				String value = "0";
				if (!StringUtils.isEmpty(ruleParam.getDefaultValue())) {
					value = ruleParam.getDefaultValue();
				}
				configParameters.addValue(str, value);
//				sql = sql.replaceAll(":" + str, value);
			}
		}
		
		if(type == Constants.search_type_oracle){
//			if(sql.contains("where")){
//				sql = selecsql + " and rownum<2";
//			}
//			else{
//				sql = sql + " where rownum<2";
//			}
			sql = "select * from (" + sql + ") where rownum <2";
		}
		else{
			sql = sql + " limit 0,1";
		}
		
		
		StringBuffer strb = new StringBuffer();
		for (RuleParam ruleParam : ruleParamList) {
			if (ruleParam.getRequestType() == Constants.RESPONSE_TYPE) {
				if (strb.length() == 0) {
					strb.append(ruleParam.getParamName());
				} else {
					strb.append(" , ").append(ruleParam.getParamName());
				}
			}
		}
		
		if(strb.length() == 0){
			strb.append("*");
		}

		String executeSql = "select " + strb.toString() + " from (" + sql.toString() + ")";


		//传统oracle 走 分支
		if(type == Constants.search_type_oracle){
			Map<String,Object> sendMap = new HashMap<String,Object>();
			
			sendMap.put("sql", sql);
			sendMap.put("requestData", rowData);
			NamedParameterJdbcTemplate conn = null;
			try {
				conn = jdbcDAO.getDbmsConnection(database);
			} catch (Exception e1) {
				return e1.getMessage();
			}
			
			try{
				conn.queryForList(sql,configParameters);
			}
			catch(Exception e){
				return "sql执行出现错误：\n" + e.getMessage();
			}
			
			
			try {
				conn.queryForList(executeSql,configParameters);
			} catch (Exception e) {
				return "返回参数出现错误：\n" + e.getMessage();
			}
			return null;
		}
		
		////////////////////////oracle分支
		
		/**
		 * 大数据分支
		 */
		try {
			inceptorDAO.list(database, sql);
		} catch (Exception e) {

			return "sql执行出现错误：\n" + e.getMessage();
		}

		try {
			inceptorDAO.list(database, executeSql);
		} catch (Exception e) {

			return "返回参数出现错误：\n" + e.getMessage();
		}

		return null;
	}

	
	
	/**
	 * 只挂接服务
	 * @param id
	 * @return
	 * @author 陈明
	 * 2016年12月10日
	 */
	public Object registerOnly(String id) {
		
		RuleConfig config = this.getById(id);
		
//		ResultStatus status = this.sendData(config);
//		if(status.getHasError()){
//			return status;
//		}
		return new ResultStatus();
	}

}
