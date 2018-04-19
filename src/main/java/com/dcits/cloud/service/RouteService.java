package com.dcits.cloud.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.dcits.cloud.common.Constants;
import com.dcits.cloud.dao.DatabaseDAO;
import com.dcits.cloud.dao.ServiceDAO;
import com.dcits.cloud.dao.SystemDAO;
import com.dcits.cloud.model.Page;
import com.dcits.cloud.model.PageList;
import com.dcits.cloud.model.ResultStatus;
import com.dcits.cloud.model.ServiceInfo;
import com.dcits.cloud.model.ServiceParamInfo;
import com.dcits.cloud.utils.Utils;
import com.digitalchina.ldp.app.smp.bean.RouteInfo;
import com.digitalchina.ldp.app.smp.common.util.RouteManagerContainer;
import com.digitalchina.ldp.app.smp.service.RmiRouteManager;

import net.sf.json.JSONArray;

@Service("routeService")
@Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED)
public class RouteService {

	@Autowired
	private ServiceDAO serviceDAO;

	@Autowired
	private DatabaseDAO databaseDAO;
	
	@Autowired
	private SystemDAO systemDAO;
	

	public PageList<ServiceInfo> getPageService(String searchCondition, Long startPage, Long pageSize,String type, Integer publish, String owner, String user)
			throws InstantiationException, IllegalAccessException {
		PageList<ServiceInfo> list = new PageList<ServiceInfo>();
//		if(null == Constants.AIP_SERVICE_URL){
//			systemDAO.initURL();
//		}
		Long totalCount = serviceDAO.getAllServiceCount(searchCondition,type,publish,owner,user);
		if (totalCount != 0) {
			list.setCount(totalCount);
			List<ServiceInfo> data = serviceDAO.getAllServiceList(searchCondition, startPage,
					pageSize,type,publish,owner,user);
			list.setList(data);
		}
		list.setPageIndex(startPage);
		return list;
	}

	/**
	 * 获取所有的数据源
	 * 
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @author 陈明 2017年1月10日
	 */
	public Object getAllDataSource() throws InstantiationException, IllegalAccessException {

		List<Map<String, Object>> list = databaseDAO.getAll();

		ResultStatus resultStatus = new ResultStatus();

		resultStatus.setData(list);

		return resultStatus;
	}

	/**
	 * 添加服务
	 * 
	 * @param data
	 * @return
	 * @author 陈明 2017年1月11日
	 * @param url 
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public Object addService(String data) throws InstantiationException, IllegalAccessException {
		
		Map<String, Object> beanMap = Utils.jsonToMap(data);
  
		// 设置表配置信息
		Map<String, Object> tableData = Utils.jsonToMap(beanMap.get("tableData").toString());
		
		ServiceInfo serviceInfo = new ServiceInfo();

		try {
			// 基本信息设置
			String res_id = StringUtils.isEmpty(beanMap.get("id")) ? null : beanMap.get("id").toString();
			if(res_id != null && serviceDAO.getPublishMode(res_id) == 1){
				return new ResultStatus("不能修改已经发布的服务，请先取消发布！");
			}
			serviceInfo.setRes_id(res_id);
//			serviceInfo.setCrt_dt(new Date(System.currentTimeMillis()));
			serviceInfo.setFile_name("");
			serviceInfo.setFile_size(1);
			serviceInfo.setFileuuid("");
			serviceInfo.setIs_auth(Integer.valueOf(beanMap.get("authType").toString()));
			serviceInfo.setMatch_on_uriprefix(1);
			// 目前默认写死 所属提供者
			serviceInfo.setProvider("iCwfIGL1EeSmyOtDehbOGg");
			serviceInfo.setRes_desc(beanMap.get("serviceDesc").toString());
			serviceInfo.setRes_nm(beanMap.get("serviceName").toString());
			serviceInfo.setRoute_status(1);
			serviceInfo.setPageSize(Integer.valueOf("".equals(beanMap.get("pageSize"))||beanMap.get("pageSize") == null ?0:Integer.valueOf(beanMap.get("pageSize").toString())));
			serviceInfo.setRunning_status(1);
			serviceInfo.setService_type(0);
			serviceInfo.setUse_type(beanMap.get("use_type").toString());
			serviceInfo.setShowTotal(Integer.valueOf(beanMap.get("showTotal").toString()));
			serviceInfo.setCreator(beanMap.get("creator").toString());
			serviceInfo.setShowTotalData(Integer.valueOf(beanMap.get("showTotalData").toString()));
			String pubUrl = beanMap.get("serviceAddr").toString();
			
//			if(Constants.AIP_SERVICE_URL == null || "".equals(Constants.AIP_SERVICE_URL)){
//				systemDAO.initURL();
//			}
			String apiurl = Constants.AIP_SERVICE_URL;
			String proxyUrl = apiurl + "/" + pubUrl;
			
			serviceInfo.setPublish_url(pubUrl);
			//保存代理地址
			serviceInfo.setSrv_url(proxyUrl);
			serviceInfo.setWrite_log(Integer.valueOf(beanMap.get("logFlag").toString()));
			serviceInfo.setRes_typ("http");
			serviceInfo.setAllowDel(Integer.valueOf(beanMap.get("allowDel").toString()));
			serviceInfo.setSearch_type(Integer.valueOf(tableData.get("searchType").toString()));
			serviceInfo.setDatabase_identity(tableData.get("tableSpace").toString());
			serviceInfo.setTable_name(tableData.get("tableName").toString());
			serviceInfo.setSql_script(beanMap.get("sql").toString());
			//默认为未发布状态
			serviceInfo.setIs_publish(0);
			

			List<ServiceParamInfo> ruleList = new ArrayList<ServiceParamInfo>();
			List<Map<String, Object>> requestList = Utils.jsonToList(beanMap.get("requestData").toString());
			// 请求参数填充
			for (Map<String, Object> requestMap : requestList) {
				requestMap.put("paramater_type", Constants.REQUEST_TYPE);
				ServiceParamInfo ruleParam = new ServiceParamInfo();
				ruleParam.setParameter_type(Constants.REQUEST_TYPE);
				ruleParam.setIs_null(0);
				ruleParam.setParameter_name(requestMap.get("paramName").toString().trim());
				ruleParam.setDefault_value(requestMap.get("defaultValue") == null ? "": requestMap.get("defaultValue").toString());
				ruleParam.setParameter_desc(requestMap.get("zhName").toString());
				ruleParam.setRoute_id(serviceInfo.getRes_id());
				String parameter_id = Utils.generateKey();
				ruleParam.setParameter_id(parameter_id);
				ruleParam.setMax_length(0);
				ruleParam.setCodeSQL(requestMap.get("codeSQL") == null?"": requestMap.get("codeSQL").toString());
				String column_name = StringUtils.isEmpty(requestMap.get("columnName")) ? ""
						: requestMap.get("columnName").toString();
				ruleParam.setColumn_name(column_name);
				String dataType = StringUtils.isEmpty(requestMap.get("dataType")) ? ""
						: requestMap.get("dataType").toString();
				ruleParam.setData_type(dataType);
				String expression = StringUtils.isEmpty(requestMap.get("expresstion")) ? ""
						: requestMap.get("expresstion").toString();
				String order = StringUtils.isEmpty(requestMap.get("order")) ? ""
						: requestMap.get("order").toString();
				ruleParam.setParameter_order(Integer.valueOf(order));
				ruleParam.setExpression(expression);
				if (null != ruleParam) {
					ruleList.add(ruleParam);
				}
			}
			// 响应参数填充
			List<Map<String, Object>> responseList = Utils.jsonToList(beanMap.get("responseData").toString());
			for (Map<String, Object> responseMap : responseList) {
				responseMap.put("paramater_type", Constants.RESPONSE_TYPE);
				ServiceParamInfo ruleParam = new ServiceParamInfo();
				ruleParam.setParameter_type(Constants.RESPONSE_TYPE);
				ruleParam.setIs_null(0);
				ruleParam.setParameter_name(responseMap.get("paramName").toString().trim());
				ruleParam.setDefault_value(responseMap.get("defaultValue") == null ? "":responseMap.get("defaultValue").toString());
				ruleParam.setParameter_desc(responseMap.get("zhName").toString());
				ruleParam.setRoute_id(serviceInfo.getRes_id());
				String parameter_id = Utils.generateKey();
				ruleParam.setParameter_id(parameter_id);
				ruleParam.setMax_length(0);

				String column_name = StringUtils.isEmpty(responseMap.get("columnName")) ? ""
						: responseMap.get("columnName").toString();
				ruleParam.setColumn_name(column_name);
				String dataType = StringUtils.isEmpty(responseMap.get("dataType")) ? ""
						: responseMap.get("dataType").toString();
				ruleParam.setData_type(dataType);
				String expression = StringUtils.isEmpty(responseMap.get("expresstion")) ? ""
						: responseMap.get("expresstion").toString();
				ruleParam.setExpression(expression);
				
				String order = StringUtils.isEmpty(responseMap.get("order")) ? ""
						: responseMap.get("order").toString();
				ruleParam.setParameter_order(Integer.valueOf(order));
				if (null != ruleParam) {
					ruleList.add(ruleParam);
				}
			}
			
			serviceInfo.setParams(ruleList);
			
			 
			// 先删除参数数据
			serviceDAO.deleteParams(serviceInfo.getRes_id());
     
			
			serviceDAO.addService(serviceInfo);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultStatus(e.getMessage());
		}

		return new ResultStatus();
	}

	/**
	 * 删除服务
	 * 
	 * @param id
	 * @return
	 * @author 陈明 2017年1月11日
	 */
	public Object deleteService(String id) {
		try{
			serviceDAO.deleteService(id);
		}
		catch(Exception e){
			return new ResultStatus(e.getMessage());
		}
		return new ResultStatus();
		
	}

	/**
	 * 修改服务
	 * 
	 * @param data
	 * @return
	 * @author 陈明 2017年1月11日
	 */
	public Object updateService(String data) {

		return null;
	}

	/**
	 * 根据id获取单个服务
	 * 
	 * @param id
	 * @return
	 * @author 陈明 2017年1月13日
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public Object getServiceById(String id) throws InstantiationException, IllegalAccessException {

		return serviceDAO.getServiceById(id);
	}
	
	
	
	/**
	 * 传统关系型数据库，走代理方式
	 * @param beanMap
	 * @return
	 * @author 陈明
	 * 2017年1月16日
	 * @throws Exception 
	 */
//	private void proxyService(Map<String, Object> beanMap) throws Exception{
//		
//		//数据填充
//		Map<String,Object> sendMap = new HashMap<String,Object>();
//		sendMap.put("serviceName", beanMap.get("serviceName"));
//		sendMap.put("serviceAddress", beanMap.get("serviceAddr"));
//		sendMap.put("serviceDesc", beanMap.get("serviceDesc"));
//		sendMap.put("isAuth", beanMap.get("authType"));
//		sendMap.put("isLog", beanMap.get("logFlag"));
//		sendMap.put("sql", beanMap.get("sql"));
//		sendMap.put("requestData", beanMap.get("requestData"));
//		sendMap.put("responseData", beanMap.get("responseData"));
//		Map<String,Object> returnMap = Utils.jsonToMap(Utils.sendPOST(Constants.CREATE_SERVICE, sendMap));
//		if (!Boolean.valueOf(returnMap.get("success").toString())) {
//			throw new Exception("服务创建失败" + "(" + returnMap.get("msg").toString() + ")");
//		}
//	}  
	
	
	/**
	 * 删除ssp总线上的服务
	 * @param id
	 * @return
	 * @author 陈明
	 * 2016年12月10日
	 */
	public ResultStatus deleteSSP(String id){
		
		return new ResultStatus();
	}
	
	public Object getServiceByUrl(String url) throws InstantiationException, IllegalAccessException{
		
		return serviceDAO.getServiceInfoByUrl(url);
	}

	
	/**
	 * 服务测试
	 * @param requestMap
	 * @return
	 * @author 陈明
	 * 2017年1月19日
	 * @param url 
	 * @param routeid 
	 */
	public Object serTest(Map<String, String[]> requestMap) {
		
		Map<String,Object> dataMap = new HashMap<String,Object>();
		
		for(Entry<String, String[]> entry : requestMap.entrySet()){
			dataMap.put(entry.getKey(), entry.getValue()[0]);
		}
		dataMap.put("Accept-Lafnguag", dataMap.get("routeid"));
		
		String url = requestMap.get("url")[0];
		
		if("4".equals(dataMap.get("type"))){
			url = Constants.AIP_SERVICE_URL;
		}
		return Utils.sendPOST(url, dataMap);
	}

	
	/**
	 * 发布服务
	 * @param id
	 * @return
	 */
	public Object unpublishService(String id,Integer mode) {
		
		try{
			serviceDAO.unpublishService(id, mode);
		}
		catch(Exception e){
			return new ResultStatus(e.getMessage());
		}
		
		return new ResultStatus();
	}

	/**
	 * 获取服务API 的码表type
	 * @return
	 */
	public ResultStatus getAPIType() {
		ResultStatus result = new ResultStatus();
		List<Map<String,Object>> typeList = new ArrayList<Map<String,Object>>();
		try{
			typeList = serviceDAO.getDictInfoByparentCode("SJZD-APIFL");
		}
		catch(Exception e){
			return new ResultStatus(e.getMessage());
		}
		result.setData(typeList);
		return result;
	}

	/**
	 * 获取数据库字段类型
	 * @return
	 */
	public ResultStatus getDBType() {
		ResultStatus result = new ResultStatus();
		List<Map<String,Object>> typeList = new ArrayList<Map<String,Object>>();
		try{
			typeList = serviceDAO.getDBType();
		}
		catch(Exception e){
			return new ResultStatus(e.getMessage());
		}
		result.setData(typeList);
		return result;
	}

	/**
	 * 获取服务API的应用主题和业务领域
	 * @return
	 */
	public Object getServiceAttr() {
		ResultStatus resultStatus = new ResultStatus();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		try{
			dataMap = serviceDAO.getServiceAttr();
		}
		catch(Exception e){
			return new ResultStatus(e.getMessage());
		}
		resultStatus.setData(dataMap);
		
		return resultStatus;
		
	}

	
	/**
	 * 发布服务
	 * @param id
	 * @param topic 主题
	 * @param field 领域
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ResultStatus publishService(String id, String topic, String field) {

		ResultStatus resultStatus = new ResultStatus();
		List<String> topicList = null;
		List<String> fieldList = null;
		List<Map<String,Object>> assList = new ArrayList<Map<String,Object>>();
		try{
			topicList = (List<String>)JSONArray.fromObject(topic);
			fieldList = (List<String>)JSONArray.fromObject(field);
			for(String eachtopic : topicList){
				Map<String,Object> subMap = new HashMap<String,Object>();
				subMap.put("typeid", eachtopic);
				subMap.put("type", "1");
				assList.add(subMap);
			}
			
			for(String eachfield : fieldList){
				Map<String,Object> subMap = new HashMap<String,Object>();
				subMap.put("typeid", eachfield);
				subMap.put("type", "2");
				assList.add(subMap);
			}
			serviceDAO.publishService(id,assList);
			//修改状态为发布状态
			serviceDAO.unpublishService(id, 1);
		}
		catch(Exception e){
			//异常回滚
			return new ResultStatus(e.getMessage());
		}
		
		return resultStatus;
	}

	
	/**
	 * 获取服务的所属领域和所属主题
	 * @param id
	 * @return
	 */
	public Object getServiceTopic(String id) {
		ResultStatus resultStatus= new ResultStatus();
		List<Map<String, Object>> topicList = new ArrayList<Map<String,Object>>();
		try{
			topicList = serviceDAO.getServiceTopic(id);
		}
		catch(Exception e){
			return new ResultStatus(e.getMessage());
		}
		resultStatus.setData(topicList);
		
		return resultStatus;
	}

	
	/**
	 * 根据用户名获取用户组的所有用户
	 * @param userName
	 * @return
	 */
	public Object getGroupUser(String userName) {
		ResultStatus resultStatus= new ResultStatus();
		List<Map<String, Object>> userList = new ArrayList<Map<String,Object>>();
		try{
			userList = serviceDAO.getGroupUser(userName);
		}
		catch(Exception e){
			return new ResultStatus(e.getMessage());
		}
		resultStatus.setData(userList);
		
		return resultStatus;
	}

	
	/**
	 * 获取所有用户
	 * @return
	 */
	public Object getAllUser(Long startPage,String condition) {
		Page page = new Page(startPage);
		List<Map<String, Object>> userList = new ArrayList<Map<String,Object>>();
		long count = 0l;
		try{
			userList = serviceDAO.getAllUser(startPage,condition);
			count = serviceDAO.getUserCount(condition);
		}
		catch(Exception e){
			return new ResultStatus(e.getMessage());
		}
		page.setData(userList);
		page.setTotalCount(count);
		return page;
	}

	
	/**
	 * 改变服务创建人
	 * @param resid
	 * @param newUser
	 * @return
	 */
	public Object modifyUser(String resid, String newUser) {
		
		try{
			serviceDAO.modifyUser(resid,newUser);
		}
		catch(Exception e){
			return new ResultStatus(e.getMessage());
		}
		return new ResultStatus();
	}

	
	/**
	 * 获取所有创建了服务的用户
	 * @param userName
	 * @return
	 */
	public Object getAllCreator(String userName) {
		ResultStatus resultStatus= new ResultStatus();
		List<Map<String, Object>> userList = new ArrayList<Map<String,Object>>();
		try{
			userList = serviceDAO.getAllCreator(userName);
		}
		catch(Exception e){
			return new ResultStatus(e.getMessage());
		}
		resultStatus.setData(userList);
		
		return resultStatus;
	}
}
 