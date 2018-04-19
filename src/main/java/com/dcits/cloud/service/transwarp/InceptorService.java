package com.dcits.cloud.service.transwarp;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.dcits.cloud.common.Constants;
import com.dcits.cloud.dao.transwarp.InceptorDAO;
import com.dcits.cloud.model.Page;
import com.dcits.cloud.model.ResultStatus;
import com.dcits.cloud.model.RuleConfig;
import com.dcits.cloud.model.RuleParam;
import com.dcits.cloud.model.ServiceInfo;
import com.dcits.cloud.model.ServiceParamInfo;
import com.dcits.cloud.service.AbstractService;
import com.dcits.cloud.service.IService;

@Service("inceptorService")
public class InceptorService extends AbstractService implements IService {

	@Autowired
	InceptorDAO inceptorDAO;

	@Override
	public Object search(ServiceInfo serviceInfo, Map<String, String[]> requestMap) {
		if (serviceInfo.getSql_script() != null
				&& serviceInfo.getSql_script().trim() != "") {
			return inceptorSql2(serviceInfo, requestMap);
		} else {
			//return inceptorSql(config, requestMap);
			return null;
		}
	}

	/**
	 * 全部根据输入框配置信息构建sql语句
	 * 
	 * @param config
	 * @param requestMap
	 * @return
	 */
	public Object inceptorSql(RuleConfig config,
			Map<String, String[]> requestMap) {
		Long startTime = System.currentTimeMillis();
		// 设置请求参数startPage,pageSize
		Page page = new Page(
				requestMap.get("startPage") != null ? Long.valueOf(requestMap
						.get("startPage")[0].toString()) : 0L,
				requestMap.get("pageSize") != null ? Long.valueOf(requestMap
						.get("pageSize")[0].toString()) : 0);
		//
		StringBuffer sql = new StringBuffer();
		String tableName = config.getRuleRepo().getTableName();
		List<RuleParam> params = config.getRuleRepo().getRuleParams();
		String requestCondtions = getRequestParam(requestMap, params);
		String responseColumns = getResponseParam(params);
		sql.append("select").append(responseColumns).append("from ")
				.append(tableName);
		sql.append(" where 1=1 ");
		sql.append(requestCondtions);
		sql.append(" limit ").append(page.getStartIndex()).append(",")
				.append(page.getPageSize() + 1);
		List<Map<String, Object>> data = inceptorDAO.list(config.getRuleRepo()
				.getNameSpace(), sql.toString());
		Long endTime = System.currentTimeMillis();
		page.setCostTime(endTime-startTime);
		// 设置值
		page.setData(data);
		page.setRecords(data != null ? data.size() : 0L);
		//
		return new ResultStatus(page);
	}

	/**
	 * 基于sql脚本查询
	 * 
	 * @param serviceInfo
	 * @param requestMap
	 * @return
	 */
	public Object inceptorSql2(ServiceInfo serviceInfo,
			Map<String, String[]> requestMap) {
		Long startTime = System.currentTimeMillis();
		// 设置请求参数startPage,pageSize
		Page page = new Page(
				requestMap.get("start") != null ? Long.valueOf(requestMap
						.get("start")[0].toString()) : 0L,
				requestMap.get("pageSize") != null ? Long.valueOf(requestMap
						.get("pageSize")[0].toString()) : 0);
		//
		StringBuffer sql = new StringBuffer();
		MapSqlParameterSource configParameters = new MapSqlParameterSource();
		List<ServiceParamInfo> params = serviceInfo.getParams();
		
		sql.append(serviceInfo.getSql_script());
		if(!sql.toString().contains("limit")){
			sql.append(" limit ").append(page.getStartIndex()).append(",")
			.append(page.getPageSize() + 2);		
		}
		else{
			String sqlL = sql.append(" ").toString().replaceAll("limit\\s*?\\d+\\s", "limit 0,15 ");
			sql = new StringBuffer(sqlL);
		}
		String sqlStr = sql.toString();
		for (ServiceParamInfo param : params) {
			if (param.getParameter_type() == Constants.REQUEST_TYPE) {
				
				String value = "";
				if (requestMap.get(param.getParameter_name()) != null
						&& requestMap.get(param.getParameter_name())[0].trim() != "") {
					value = requestMap.get(param.getParameter_name())[0];
				}else{
					if(param.getDefault_value() != null && param.getDefault_value().trim() != "" && requestMap.get(param.getDefault_value()) != null){
						value = requestMap.get(param.getDefault_value())[0];
					}
				}
//				if(StringUtils.isEmpty(value)){
//					return new ResultStatus("参数不能为空");
//				}
//				
				configParameters.addValue(param.getParameter_name(), value);
//				sqlStr = sqlStr.replace(":" + param.getParameter_name(), value);
			}
		}
		//
		StringBuffer resParams = new StringBuffer();
		for (ServiceParamInfo param : serviceInfo.getParams()) {
			if (param.getParameter_type() == Constants.RESPONSE_TYPE) {
			    if( resParams.length() == 0){
			    	resParams.append(param.getParameter_name());
			    }else{
			    	resParams.append(" , ").append(param.getParameter_name());
			    }
			}
		}
		String executeSql = "select "+resParams.toString()+" from ("+sqlStr.toString()+")";
		List<Map<String, Object>> data = null;
		try{
			data = inceptorDAO.list(serviceInfo.getSearch_type(),serviceInfo.getDatabase_identity(),
					executeSql, configParameters);
		}
		catch(Exception e){
			return new ResultStatus("查询失败：" + e.getMessage());
		}
				
		Long endTime = System.currentTimeMillis();
		page.setCostTime(endTime-startTime);
		// 设置值
		page.setData(data);
		page.setRecords(data != null ? data.size() : 0L);
		//
		return new ResultStatus(page);
	}

	
	
	private String getRequestParam(Map<String, String[]> requestMap,
			List<RuleParam> params) {
		String requestCondtions = null;
		for (RuleParam param : params) {
			if (param.getRequestType() == Constants.REQUEST_TYPE) {
				if (requestMap.get(param.getParamName()) != null
						&& requestMap.get(param.getParamName())[0].trim() != "") {
					requestCondtions = " AND " + param.getColumnName()
							+ " like '"
							+ requestMap.get(param.getParamName())[0] + "%'";
				}
			}
		}
		return requestCondtions == null ? "" : requestCondtions;
	}

	private String getResponseParam(List<RuleParam> params) {
		String responseColumns = null;
		for (RuleParam param : params) {
			if (param.getRequestType() == Constants.RESPONSE_TYPE) {
				if (responseColumns == null) {
					responseColumns = param.getColumnName() + " AS "
							+ param.getParamName();
				} else {
					responseColumns = "," + param.getColumnName() + " AS "
							+ param.getParamName();
				}
			}
		}
		return " " + responseColumns + " ";
	}

}
