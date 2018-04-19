package com.dcits.cloud.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.dcits.cloud.common.Constants;
import com.dcits.cloud.model.ResultStatus;
import com.dcits.cloud.model.ServiceInfo;
import com.dcits.cloud.model.ServiceParamInfo;
import com.dcits.cloud.utils.Utils;
import com.digitalchina.ldp.app.smp.bean.RouteInfo;
import com.digitalchina.ldp.app.smp.common.util.RouteManagerContainer;
import com.digitalchina.ldp.app.smp.service.RmiRouteManager;

@Repository
@Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED)
public class ServiceDAO {

	@Autowired
	private AipJdbcDAO aipJdbcDAO;
	@Autowired
	RouteManagerContainer routeManagerContainer;

	public Long getAllServiceCount(String searchCondition, String type, Integer publish, String owner, String user) {

		MapSqlParameterSource configParameters = new MapSqlParameterSource();
		String sql = "SELECT count(*) from  ESB_ROUTE_INFO     where 1=1 and (SEARCH_TYPE is not null)";
		if (searchCondition != null && searchCondition.trim() != "") {
			sql += "  and ( ";
			sql += " res_nm like '%" + searchCondition + "%'";
			sql += " or res_desc like '%" + searchCondition + "%'";
			sql += " or srv_url like '%" + searchCondition + "%'";
			sql += " ) ";
		}
		if (type != null && !"".equals(type)) {
			sql += " and USE_TYPE= '" + type + "'";
		}

		if (publish != null && publish != -1) {
			sql += " and IS_PUBLISH = " + publish;
		}
		if ("1".equals(owner)) {
			sql += " and CREATOR='" + user + "'";
		}

		if ("2".equals(owner)) {
			List<Map<String, Object>> groupUser = this.getGroupUser(user);
			StringBuffer groupStr = new StringBuffer();
			for (Map<String, Object> userMap : groupUser) {
				groupStr.append("'");
				groupStr.append(userMap.get("LOGIN_NAME"));
				groupStr.append("',");
			}
			if (groupStr.length() == 0) {
				sql += " and CREATOR='" + user + "'";
			} else {
				sql += " and CREATOR in (" + groupStr.substring(0, groupStr.length() - 1).toString() + ") ";
			}
		}

		if (null != owner && !"2".equals(owner) && !"1".equals(owner) && !"".equals(owner) && !"0".equals(owner)) {
			sql += " and CREATOR='" + owner + "'";
		}

		Object obj = aipJdbcDAO.getTemplate().queryForObject(sql, configParameters, Integer.class);
		if (obj != null) {
			return Long.valueOf(obj.toString());
		}
		return 0L;
	}

	public List<ServiceInfo> getAllServiceList(String searchCondition, Long startNum, Long pageSize, String type,
			Integer publish, String owner, String user) throws InstantiationException, IllegalAccessException {
		MapSqlParameterSource configParameters = new MapSqlParameterSource();

		String sql = "select t1.allowdel,t1.res_id,t1.publish_url,t1.srv_url,t1.res_typ,t1.route_status,t1.running_status,t1.res_nm,t1.res_desc,t1.is_auth,t1.crt_dt,t1.asset_id,t1.file_size,t1.file_name,t1.provider,t1.service_type,t1.write_log,t1.match_on_uriprefix,t1.fileuuid,t1.database_identity,t1.search_type,t1.table_name,t1.use_type,t1.pagesize,t1.showtotal,t1.is_publish,t1.creator,t1.update_dt,t1.showtotaldata,fwi.name  from   ESB_ROUTE_INFO t1 left join fw_user_info fwi on t1.creator=fwi.login_name where 1=1 and (SEARCH_TYPE is not null) ";
		if (searchCondition != null && searchCondition.trim() != "") {
			sql += "  and ( ";
			sql += " res_nm like '%" + searchCondition + "%'";
			sql += " or res_desc like '%" + searchCondition + "%'";
			sql += " or srv_url like '%" + searchCondition + "%'";
			sql += " ) ";
		}
		if (type != null && !"".equals(type)) {
			sql += " and USE_TYPE='" + type + "'";
		}

		if (null != publish && publish != -1) {
			sql += " and IS_PUBLISH = " + publish;
		}

		if ("1".equals(owner)) {
			sql += " and CREATOR='" + user + "'";
		}

		if ("2".equals(owner)) {
			List<Map<String, Object>> groupUser = this.getGroupUser(user);
			StringBuffer groupStr = new StringBuffer();
			for (Map<String, Object> userMap : groupUser) {
				groupStr.append("'");
				groupStr.append(userMap.get("LOGIN_NAME"));
				groupStr.append("',");
			}
			if (groupStr.length() == 0) {
				sql += " and CREATOR='" + user + "'";
			} else {
				sql += " and CREATOR in (" + groupStr.substring(0, groupStr.length() - 1).toString() + ") ";
			}
		}

		if (null != owner && !"2".equals(owner) && !"1".equals(owner) && !"".equals(owner) && !"0".equals(owner)) {
			sql += " and CREATOR='" + owner + "'";
		}

		sql += " group by t1.allowdel,t1.res_id,t1.publish_url,t1.srv_url,t1.res_typ,t1.route_status,t1.running_status,t1.res_nm,t1.res_desc,t1.is_auth,t1.crt_dt,t1.asset_id,t1.file_size,t1.file_name,t1.provider,t1.service_type,t1.write_log,t1.match_on_uriprefix,t1.fileuuid,t1.database_identity,t1.search_type,t1.table_name,t1.use_type,t1.pagesize,t1.showtotal,t1.is_publish,t1.creator,t1.update_dt,t1.showtotaldata,fwi.name ";
		sql += " order by UPDATE_DT desc";
		StringBuffer exeSql = new StringBuffer("select * from (   select ROWNUM as row_num , a.*  from ( ");
		exeSql.append(sql);
		exeSql.append(" ) a  where rownum <" + (startNum*pageSize+pageSize+1) + "  ) b  where b.row_num >"
				+ startNum*pageSize + " ");

		List<Map<String, Object>> result = aipJdbcDAO.getTemplate().queryForList(exeSql.toString(), configParameters);
		List<ServiceInfo> list = Utils.listToBean(result, ServiceInfo.class);
		return list;
	}

	/**
	 * 获取单页服务
	 * 
	 * @return
	 * @author 陈明 2017年1月10日
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public List<ServiceInfo> getAllServiceAndParam(long start, long pageSize)
			throws InstantiationException, IllegalAccessException {
		long endNum = start * pageSize + pageSize;
		String sql = "select * from (select t1.*,rownum rn from ESB_ROUTE_INFO t1 where rownum<" + endNum + " where rn>"
				+ (endNum - pageSize - 1);

		List<Map<String, Object>> result = aipJdbcDAO.getTemplate().queryForList(sql, new HashMap<String, Object>());
		List<ServiceInfo> list = Utils.listToBean(result, ServiceInfo.class);

		if (null == list) {
			return null;
		}

		for (ServiceInfo serviceInfo : list) {
			String paramSql = "select * from ESB_PARAMETER_INFO where ROUTE_ID =:routid order by PATAMETER_ORDER asc";

			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("routid", serviceInfo.getRes_id());
			List<Map<String, Object>> paramResult = aipJdbcDAO.getTemplate().queryForList(paramSql, paramMap);

			List<ServiceParamInfo> paramlist = Utils.listToBean(paramResult, ServiceParamInfo.class);

			serviceInfo.setParams(paramlist);
		}

		return list;
	}

	/**
	 * 加入 单个服务
	 * 
	 * @return
	 * @author 陈明 2017年1月10日
	 * @throws Exception
	 */

	public synchronized void  addService(ServiceInfo serviceInfo) throws Exception {
		aipJdbcDAO.getjdbcTemplate().beginTranstaion();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		String sql = "";
		String resId = null;
		String curentTime = Utils.getCurrentDate();
		if (!StringUtils.isEmpty(serviceInfo.getRes_id())) {

			resId = serviceInfo.getRes_id().toString();
			sql = "update ESB_ROUTE_INFO set update_dt=to_date(:update_dt,'yyyy/mm/dd hh24:mi:ss'),database_identity=:database_identity,"
					+ "file_name=:file_name,file_size=:file_size,fileuuid=:fileuuid,is_auth=:is_auth,allowdel=:allowDel,"
					+ "match_on_uriprefix=:match_on_uriprefix,provider=:provider,publish_url=:publish_url,"
					+ "res_desc=:res_desc,res_nm=:res_nm,res_typ=:res_typ,route_status=:route_status,"
					+ "service_type=:service_type,srv_url=:srv_url,write_log=:write_log,search_type=:search_type,table_name=:table_name,use_type=:use_type,pagesize=:pagesize,showTotal=:showTotal,showTotalData=:showTotalData where res_id=:res_id";
			namedParameters.addValue("update_dt", curentTime);
		} else {
			// 检测是否发布地址重复
			String uniqueSQL = "select * from ESB_ROUTE_INFO where publish_url=:url";
			MapSqlParameterSource uniquenamedParameters = new MapSqlParameterSource();
			uniquenamedParameters.addValue("url", serviceInfo.getPublish_url());
			List uniqueServiceInfo = aipJdbcDAO.getTemplate().queryForList(uniqueSQL, uniquenamedParameters);
			if (null != uniqueServiceInfo && uniqueServiceInfo.size() > 0) {
				throw new Exception("发布地址重复");
			}
			resId = Utils.generateKey();
			sql = "insert into ESB_ROUTE_INFO(res_id,publish_url,srv_url,res_typ,route_status,running_status"
					+ ",res_nm,res_desc,is_auth,crt_dt,asset_id,file_size,file_name,provider,service_type,write_log"
					+ ",match_on_uriprefix,fileuuid,database_identity,search_type,table_name,use_type,pagesize,showtotal,is_publish,creator,update_dt,showtotaldata,allowdel) values(:res_id,:publish_url,:srv_url,:res_typ,:route_status"
					+ ",:running_status,:res_nm,:res_desc,:is_auth,to_date(:crt_dt,'yyyy/mm/dd hh24:mi:ss'),:asset_id,:file_size,:file_name,:provider,:service_type"
					+ ",:write_log,:match_on_uriprefix,:fileuuid,:database_identity,:search_type,:table_name,:use_type,:pagesize,:showTotal,:is_publish,:creator,to_date(:update_dt,'yyyy/mm/dd hh24:mi:ss'),:showTotalData,:allowDel)";
			namedParameters.addValue("crt_dt", curentTime);
			namedParameters.addValue("update_dt", curentTime);
			namedParameters.addValue("asset_id", "");
		}

		serviceInfo.setRes_id(resId);
		
		namedParameters.addValue("database_identity", serviceInfo.getDatabase_identity());
		namedParameters.addValue("file_name", serviceInfo.getFile_name());
		namedParameters.addValue("file_size", serviceInfo.getFile_size());
		namedParameters.addValue("running_status", serviceInfo.getRunning_status());
		namedParameters.addValue("fileuuid", serviceInfo.getFileuuid());
		namedParameters.addValue("allowDel", serviceInfo.getAllowDel());
		namedParameters.addValue("is_auth", serviceInfo.getIs_auth());
		namedParameters.addValue("match_on_uriprefix", serviceInfo.getMatch_on_uriprefix());
		namedParameters.addValue("provider", serviceInfo.getProvider());
		namedParameters.addValue("publish_url", serviceInfo.getPublish_url());
		namedParameters.addValue("res_desc", serviceInfo.getRes_desc());
		namedParameters.addValue("res_id", resId);
		namedParameters.addValue("res_nm", serviceInfo.getRes_nm());
		namedParameters.addValue("res_typ", serviceInfo.getRes_typ());
		namedParameters.addValue("route_status", serviceInfo.getRoute_status());
		namedParameters.addValue("service_type", serviceInfo.getService_type());
		namedParameters.addValue("srv_url", serviceInfo.getSrv_url());
		namedParameters.addValue("write_log", serviceInfo.getWrite_log());
		namedParameters.addValue("table_name", serviceInfo.getTable_name());
		namedParameters.addValue("search_type", serviceInfo.getSearch_type());
		namedParameters.addValue("use_type", serviceInfo.getUse_type());
		namedParameters.addValue("pagesize", serviceInfo.getPageSize());
		namedParameters.addValue("showTotal", serviceInfo.getShowTotal());
		namedParameters.addValue("is_publish", serviceInfo.getIs_publish());
		namedParameters.addValue("creator", serviceInfo.getCreator());
		namedParameters.addValue("showTotalData", serviceInfo.getShowTotalData());

		try {

			// 删除sql
			String deleteSQL = "delete from ROUTESQLINFO where routeid=:routeid";
			MapSqlParameterSource deletenamedParameters = new MapSqlParameterSource();
			deletenamedParameters.addValue("routeid", resId);
			aipJdbcDAO.getTemplate().update(deleteSQL, deletenamedParameters);
			// 添加sql
			String sqlScriptSQL = "insert into ROUTESQLINFO values(:id,:routeid,:sql)";
			MapSqlParameterSource sqlnamedParameters = new MapSqlParameterSource();
			sqlnamedParameters.addValue("routeid", resId);
			sqlnamedParameters.addValue("sql", serviceInfo.getSql_script());
			String sqlid = Utils.generateKey();
			sqlnamedParameters.addValue("id", sqlid);

			aipJdbcDAO.getTemplate().update(sqlScriptSQL, sqlnamedParameters);
			// 添加参数
			this.addParam(serviceInfo);
			String deleteTempSQL = "delete from response_template where route_id=:route_id";

			MapSqlParameterSource deleteTempParameters = new MapSqlParameterSource();
			deleteTempParameters.addValue("route_id", resId);
			// 首先删除模板
			aipJdbcDAO.getTemplate().update(deleteTempSQL, deleteTempParameters);

			// 增加模板
			String addtempSQL = "insert into response_template values(:route_id,:response_id,:json_response,:xml_response)";

			MapSqlParameterSource addTempParameters = new MapSqlParameterSource();
			addTempParameters.addValue("route_id", resId);
			addTempParameters.addValue("response_id", Utils.generateKey());
			addTempParameters.addValue("json_response", "");
			addTempParameters.addValue("xml_response", "");
			// 增加模板
			aipJdbcDAO.getTemplate().update(addtempSQL, addTempParameters);
			if(Constants.SSPSERVER_MODE == 1){
				try{
					deleteSSP(serviceInfo.getRes_id());
				}
				catch(Exception e){
					
				}
				
			}
			
			// 最好增加服务基本数据
			aipJdbcDAO.getTemplate().update(sql, namedParameters);
			if(Constants.SSPSERVER_MODE == 1){
				addSSP(serviceInfo);
			}
			aipJdbcDAO.getjdbcTemplate().commit();

		} catch (Exception e) {
			aipJdbcDAO.getjdbcTemplate().rollback();
			throw new Exception(e.getMessage());
		}

	}

	/**
	 * 添加路由到SSP
	 * @param serviceInfo
	 */
	private void addSSP(ServiceInfo serviceInfo) throws Exception{
		RmiRouteManager rmiRouteManager = routeManagerContainer.getRouteManagers().get(0);
		RouteInfo route = new RouteInfo();
		route.setFileName(serviceInfo.getFile_name());
		route.setRouteType("http");
		route.setIsAuth(serviceInfo.getIs_auth());
		route.setMatchOnUriPrefix(1);
		// route.setProvider();
		route.setPrxoyURL(serviceInfo.getSrv_url());
		// route.setPublishDate(serviceInfo.get);
		route.setPublishURL(serviceInfo.getPublish_url());
		route.setRouteDesc(serviceInfo.getRes_desc());
		route.setRouteId(serviceInfo.getRes_id());
		route.setRouteName(serviceInfo.getName());
		route.setRouteStatus(1);
		route.setServiceType(serviceInfo.getService_type());
		route.setRunningStatus(1);
		route.setWriteLog(serviceInfo.getWrite_log());
		rmiRouteManager.addRoute(route);

	}
	
	
	private void deleteSSP(String routeId){
		RmiRouteManager rmiRouteManager = routeManagerContainer.getRouteManagers().get(0);
		rmiRouteManager.deleteRoute(routeId);
	}

	/**
	 * 删除服务
	 * 
	 * @param serviceId
	 * @return
	 * @author 陈明 2017年1月10日
	 * @throws Exception 
	 */
	public int deleteService(String serviceId) throws Exception {
		
		aipJdbcDAO.getjdbcTemplate().beginTranstaion();
		try{
		if(Constants.SSPSERVER_MODE == 1){
			try{
				deleteSSP(serviceId);
			}
			catch(Exception e){
				
			}
		}
		
		String sql = "delete from ESB_ROUTE_INFO where res_id=:res_id";

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		namedParameters.addValue("res_id", serviceId);

		aipJdbcDAO.getTemplate().update(sql, namedParameters);

		String paramSql = "delete from ESB_PARAMETER_INFO where route_id=:route_id";

		MapSqlParameterSource paramNamedParameters = new MapSqlParameterSource();

		paramNamedParameters.addValue("route_id", serviceId);

		aipJdbcDAO.getTemplate().update(paramSql, paramNamedParameters);
		
		aipJdbcDAO.getjdbcTemplate().commit();
		return Constants.EXEC_SUCCESS;
		}
		catch(Exception e){
			aipJdbcDAO.getjdbcTemplate().rollback();
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 添加参数
	 * 
	 * @return
	 * @author 陈明 2017年1月13日
	 */
	public ResultStatus addParam(ServiceInfo serviceInfo) {
		List<ServiceParamInfo> params = serviceInfo.getParams();
		String sql = "insert into ESB_PARAMETER_INFO values(:parameter_id,:route_id,:data_type,"
				+ ":default_value,:is_null,:parameter_type,:parameter_desc,"
				+ ":max_length,:parameter_name,:expression,:column_name,:parameter_path,:order," + ":codeSQL)";
		try {
			for (ServiceParamInfo param : params) {
				MapSqlParameterSource paramNamedParameters = new MapSqlParameterSource();
				paramNamedParameters.addValue("codeSQL", param.getCodeSQL());
				paramNamedParameters.addValue("parameter_id", param.getParameter_id());
				paramNamedParameters.addValue("route_id", serviceInfo.getRes_id());
				paramNamedParameters.addValue("data_type", param.getData_type());
				paramNamedParameters.addValue("default_value", param.getDefault_value());
				paramNamedParameters.addValue("is_null", param.getIs_null());
				paramNamedParameters.addValue("parameter_type", param.getParameter_type());
				paramNamedParameters.addValue("parameter_desc", param.getParameter_desc());
				paramNamedParameters.addValue("max_length", param.getMax_length());
				paramNamedParameters.addValue("parameter_name", param.getParameter_name());
				paramNamedParameters.addValue("expression", param.getExpression());
				paramNamedParameters.addValue("column_name", param.getColumn_name());

				String path = "";

				if (param.getParameter_type() == Constants.RESPONSE_TYPE) {
					path = "$." + param.getParameter_name();
				}

				paramNamedParameters.addValue("parameter_path", path);
				paramNamedParameters.addValue("order", param.getParameter_order());
				aipJdbcDAO.getTemplate().update(sql, paramNamedParameters);
			}
		}

		catch (Exception e) {

			return new ResultStatus(e.getMessage());
		}

		return new ResultStatus();

	}

	/**
	 * 删除所有参数
	 * 
	 * @param id
	 * @return
	 * @author 陈明 2017年1月13日
	 */
	public int deleteParams(String id) {
		String sql = "delete from ESB_PARAMETER_INFO where route_id=:id";

		MapSqlParameterSource paramNamedParameters = new MapSqlParameterSource();
		paramNamedParameters.addValue("id", id);

		return aipJdbcDAO.getTemplate().update(sql, paramNamedParameters);
	}

	/**
	 * 获取服务根据id
	 * 
	 * @param id
	 * @return
	 * @author 陈明 2017年1月13日
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public ServiceInfo getServiceById(String id) throws InstantiationException, IllegalAccessException {

		String sql = "select t1.*,t2.sql as sql_script from ESB_ROUTE_INFO t1 left join ROUTESQLINFO t2 on t1.res_id=t2.ROUTEID where 1=1 and res_id=:id";
		MapSqlParameterSource paramNamedParameters = new MapSqlParameterSource();
		paramNamedParameters.addValue("id", id);
		Map<String, Object> serviceMap = new HashMap<String, Object>();
		try {
			serviceMap = aipJdbcDAO.getTemplate().queryForMap(sql, paramNamedParameters);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}

		ServiceInfo serviceInfo = Utils.jsonToBean(serviceMap, ServiceInfo.class);
		String paramSql = "select * from ESB_PARAMETER_INFO where route_id=:id order by PARAMETER_ORDER asc";

		List<Map<String, Object>> params = aipJdbcDAO.getTemplate().queryForList(paramSql, paramNamedParameters);

		List<ServiceParamInfo> paramList = new ArrayList<ServiceParamInfo>();
		for (Map<String, Object> param : params) {
			ServiceParamInfo serviceParamInfo = Utils.jsonToBean(param, ServiceParamInfo.class);
			paramList.add(serviceParamInfo);
		}
		serviceInfo.setParams(paramList);
		return serviceInfo;
	}

	/**
	 * 存入sql
	 * 
	 * @param sql
	 * @author 陈明 2017年1月16日
	 */
	public void saveSql(String sql) {

	}

	/**
	 * 根据服务id获取sql脚本
	 * 
	 * @param id
	 * @return
	 * @author 陈明 2017年1月16日
	 */
	public String getSqlByRouteId(String id) {

		return null;
	}

	/**
	 * 根据url获取ServiceInfo
	 * 
	 * @param url
	 * @return
	 * @author 陈明 2017年1月16日
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public ServiceInfo getServiceInfoByUrl(String url) throws InstantiationException, IllegalAccessException {

		String sql = "select t1.*,t2.SQL as sql_script from ESB_ROUTE_INFO t1 left join ROUTESQLINFO t2 on t1.res_id=t2.routeid where 1=1 and t1.publish_url=:url";

		// 查询库表
		MapSqlParameterSource repoParameters = new MapSqlParameterSource();
		repoParameters.addValue("url", url);

		List<Map<String, Object>> serviceInfoList = aipJdbcDAO.getTemplate().queryForList(sql, repoParameters);

		if (null == serviceInfoList || serviceInfoList.isEmpty() || serviceInfoList.size() != 1) {
			return null;
		}

		ServiceInfo serviceInfo = Utils.jsonToBean(serviceInfoList.get(0), ServiceInfo.class);
		String res_id = serviceInfo.getRes_id();

		String paramSql = "select * from ESB_PARAMETER_INFO where route_id=:route_id order by PARAMETER_ORDER asc";

		MapSqlParameterSource paramRepoParameters = new MapSqlParameterSource();
		paramRepoParameters.addValue("route_id", res_id);

		List<ServiceParamInfo> params = aipJdbcDAO.getTemplate().query(paramSql, paramRepoParameters,
				new BeanPropertyRowMapper<ServiceParamInfo>(ServiceParamInfo.class));
		serviceInfo.setParams(params);
		return serviceInfo;
	}

	/**
	 * 保存sql
	 * 
	 * @param routeID
	 * @param sql
	 * @author 陈明 2017年1月17日
	 */
	public void saveSQL(String routeID, String sql) {

	}

	/**
	 * 保存sql
	 * 
	 * @param routeID
	 * @param sql
	 * @author 陈明 2017年1月17日
	 */
	public void unpublishService(String routeID, Integer mode) {

		String sql = "update ESB_ROUTE_INFO set IS_PUBLISH=:mode where res_id=:resid";

		MapSqlParameterSource paramNamedParameters = new MapSqlParameterSource();

		paramNamedParameters.addValue("mode", mode);
		paramNamedParameters.addValue("resid", routeID);

		aipJdbcDAO.getTemplate().update(sql, paramNamedParameters);

		String updateSQL = "update ESB_ROUTE_INFO set update_dt=to_date(:update_dt,'yyyy/mm/dd hh24:mi:ss') where res_id=:routeID";

		MapSqlParameterSource paramNamedParameters1 = new MapSqlParameterSource();
		paramNamedParameters1.addValue("update_dt", Utils.getCurrentDate());
		paramNamedParameters1.addValue("routeID", routeID);
		aipJdbcDAO.getTemplate().update(updateSQL, paramNamedParameters1);

	}

	/**
	 * 获取发布状态
	 * 
	 * @param routeID
	 * @param sql
	 * @author 陈明 2017年1月17日
	 */
	public Integer getPublishMode(String routeID) {

		String sql = "select is_publish from ESB_ROUTE_INFO where res_id=:resid";

		MapSqlParameterSource paramNamedParameters = new MapSqlParameterSource();
		paramNamedParameters.addValue("resid", routeID);

		Map<String, Object> modeMap = aipJdbcDAO.getTemplate().queryForMap(sql, paramNamedParameters);
		Integer is_publish = modeMap.get("is_publish") == null ? 0
				: Integer.valueOf(modeMap.get("is_publish").toString());

		return is_publish;
	}

	/**
	 * 获取服务API码表分类
	 */
	public List<Map<String, Object>> getDictInfoByparentCode(String code) {

		String sql = "select t.dict_value code,t.dict_text name from FW_DICT_INFO t,FW_DICT_INFO b where t.dict_parent = b.dict_id and b.DICT_VALUE = '" + code + "' and b.delete_mark = '0' and t.delete_mark = '0' order by  t.dict_sort_num";
		List<Map<String, Object>> typeList = aipJdbcDAO.getTemplate().queryForList(sql, new MapSqlParameterSource());
		return typeList;

	}

	/**
	 * 从字典获取数据库字段
	 */
	public List<Map<String, Object>> getDBType() {
		String sql = "select t.dict_value code,t.dict_text name from FW_DICT_INFO t,FW_DICT_INFO b where t.dict_parent = b.dict_id and b.DICT_VALUE = 'SQL_DATATYPE' and b.delete_mark = '0' and t.delete_mark = '0' order by  t.dict_sort_num";
		List<Map<String, Object>> typeList = aipJdbcDAO.getTemplate().queryForList(sql, new MapSqlParameterSource());
		return typeList;
	}

	/**
	 * 
	 * @return
	 */
	public Map<String, Object> getServiceAttr() {
		String sql = "select t.dict_value code,t.dict_text name from FW_DICT_INFO t,FW_DICT_INFO b where t.dict_parent = b.dict_id and b.DICT_VALUE = 'SJZD-YYZT' and b.delete_mark = '0' and t.delete_mark = '0' and t.dict_status='1' order by  t.dict_sort_num";
		List<Map<String, Object>> topic = aipJdbcDAO.getTemplate().queryForList(sql, new MapSqlParameterSource());

		String sql1 = "select t.dict_value code,t.dict_text name from FW_DICT_INFO t,FW_DICT_INFO b where t.dict_parent = b.dict_id and b.DICT_VALUE = 'SJZD-YWLY' and b.delete_mark = '0' and t.delete_mark = '0' and t.dict_status='1' order by  t.dict_sort_num";
		List<Map<String, Object>> field = aipJdbcDAO.getTemplate().queryForList(sql1, new MapSqlParameterSource());

		Map<String, Object> returnMap = new HashMap<String, Object>();

		String sql2 = "select t.dict_value code,t.dict_text name from FW_DICT_INFO t where t.dict_value='SJZD-YWLY'";
		Map<String, Object> fieldName = aipJdbcDAO.getTemplate().queryForMap(sql2, new MapSqlParameterSource());

		String sql3 = "select t.dict_value code,t.dict_text name from FW_DICT_INFO t where t.dict_value='SJZD-YYZT'";

		Map<String, Object> topicName = aipJdbcDAO.getTemplate().queryForMap(sql3, new MapSqlParameterSource());

		returnMap.put("topic", topic);
		returnMap.put("field", field);
		returnMap.put("topicName", topicName.get("name"));
		returnMap.put("fieldName", fieldName.get("name"));
		return returnMap;
	}

	/**
	 * 发布服务，保存应用领域和主题
	 * 
	 * @param id
	 * @param topicList
	 * @param fieldList
	 * @throws Exception
	 */
	public void publishService(final String id, final List<Map<String, Object>> assList) throws Exception {

		aipJdbcDAO.getjdbcTemplate().beginTranstaion();

		try {
			String delsql = "delete from ESB_ROUTE_TYPE where resid=:resid";
			MapSqlParameterSource paramNamedParameters = new MapSqlParameterSource();

			paramNamedParameters.addValue("resid", id);

			aipJdbcDAO.getTemplate().update(delsql, paramNamedParameters);

			String sql = "insert into esb_route_type values(?,?,?,?)";

			aipJdbcDAO.getjdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {

				@Override
				public int getBatchSize() {
					return assList.size();
				}

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					Map<String, Object> topicMap = assList.get(i);
					ps.setString(1, Utils.generateKey());
					ps.setString(2, id);
					ps.setString(3, topicMap.get("typeid").toString());
					ps.setInt(4, Integer.valueOf(topicMap.get("type").toString()));

				}

			});
			aipJdbcDAO.getjdbcTemplate().commit();
		} catch (Exception e) {
			aipJdbcDAO.getjdbcTemplate().rollback();
			throw new Exception(e.getMessage());
		}

	}

	/**
	 * 获取服务的所属主题和领域
	 * 
	 * @param id
	 */
	public List<Map<String, Object>> getServiceTopic(String id) {

		String sql = "select * from esb_route_type where resid=:resid";
		MapSqlParameterSource paramNamedParameters = new MapSqlParameterSource();
		paramNamedParameters.addValue("resid", id);

		List<Map<String, Object>> topicList = aipJdbcDAO.getTemplate().queryForList(sql, paramNamedParameters);

		return topicList;

	}

	/**
	 * 根据用户名获取用户组的所有角色
	 * 
	 * @param userName
	 */
	public List<Map<String, Object>> getGroupUser(String userName) {

		String sql = "select fu.login_name from fw_user_info fu  where fu.user_id in "
				+ "(select fw.user_id from fw_user_group fw where fw.group_id in "
				+ "(select fug.group_id from fw_user_group fug where fug.user_id in "
				+ "(select fui.user_id from fw_user_info fui where fui.login_name=:userName ))) ";
		// + "and fu.LOGIN_NAME <> 'admin'";

		MapSqlParameterSource paramNamedParameters = new MapSqlParameterSource();
		paramNamedParameters.addValue("userName", userName);

		List<Map<String, Object>> userList = aipJdbcDAO.getTemplate().queryForList(sql, paramNamedParameters);

		return userList;
	}

	/**
	 * 获取所有用户
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getAllUser(Long startPage, String condition) {

		StringBuffer sql = new StringBuffer("select * from (   select ROWNUM as row_num , a.*  from (");

		StringBuffer exesql = new StringBuffer(
				"select t.login_name,t.name,t.user_remark from FW_USER_INFO t where 1=1");

		if (!StringUtils.isEmpty(condition)) {
			exesql.append(" and (login_name like '%'||:condition||'%' or name like '%'||:condition||'%' or user_remark like '%'||:condition||'%')");
		}

		exesql.append(" and delete_mark=0 ");
		sql.append(exesql);

		sql.append(" )  a  where rownum <" + Utils.getEndRowNo(startPage, 10) + "  ) b  where b.row_num >"
				+ Utils.getStartRowNo(startPage, 10) + " ");
		MapSqlParameterSource paramNamedParameters = new MapSqlParameterSource();
		paramNamedParameters.addValue("condition", condition);
		List<Map<String, Object>> userList = aipJdbcDAO.getTemplate().queryForList(sql.toString(),
				paramNamedParameters);

		return userList;
	}

	/**
	 * 获取所有用户的总数
	 * 
	 * @return
	 */
	public long getUserCount(String condition) {
		StringBuffer sql = new StringBuffer("select count(*) from FW_USER_INFO t where 1=1");
		if (!StringUtils.isEmpty(condition)) {
			sql.append(" and (login_name like '%'||:condition||'%' or name like '%'||:condition||'%' or user_remark like '%'||:condition||'%')");
		}

		sql.append(" and delete_mark=0");
		MapSqlParameterSource paramNamedParameters = new MapSqlParameterSource();
		paramNamedParameters.addValue("condition", condition);
		Object obj = aipJdbcDAO.getTemplate().queryForObject(sql.toString(), paramNamedParameters, Integer.class);
		if (obj != null) {
			return Long.valueOf(obj.toString());
		}
		return 0L;
	}

	/**
	 * 
	 * @param resid
	 * @param newUser
	 */
	public void modifyUser(String resid, String newUser) {

		String sql = "update esb_route_info set creator=:newUser where res_id=:resid";
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("resid", resid);
		namedParameters.addValue("newUser", newUser);
		aipJdbcDAO.getTemplate().update(sql, namedParameters);
	}

	/**
	 * 记录日志
	 * 
	 * @param exception
	 * @param errorMsg
	 * @param serviceInfo
	 * @param requestMap
	 * @param l
	 */
	public void remeberLOG(Integer exception, String errorMsg, ServiceInfo serviceInfo,
			Map<String, String[]> requestMap, long l,ResultStatus result) {

		String sql1 = "insert into ESB_ROUTE_LOG(log_id,access_date,consuming,route_id,is_exception,route_node,auth_id,user_id,service_type,output,input) values(:id,to_date(:date,'yyyy/mm/dd hh24:mi:ss'),:consuming,:route_id,:is_exception,:route_node,:auth_id,:user_id,:service_type,:output,:input)";
		String sql = "insert into ESB_ROUTE_LOG(log_id,access_date,consuming,route_id,is_exception,route_node,auth_id,user_id,service_type,output,input,data_count) values(:id,to_date(:date,'yyyy/mm/dd hh24:mi:ss'),:consuming,:route_id,:is_exception,:route_node,:auth_id,:user_id,:service_type,:output,:input,:data_count)";
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("id", Utils.generateKey());
		namedParameters.addValue("date", Utils.getCurrentDate());
		namedParameters.addValue("consuming", l);
		namedParameters.addValue("route_id", serviceInfo.getRes_id());
		namedParameters.addValue("is_exception", exception);
		namedParameters.addValue("route_node", "");
		namedParameters.addValue("auth_id", "");
		namedParameters.addValue("user_id", "");
		namedParameters.addValue("service_type", 2);
		namedParameters.addValue("output", errorMsg);
		long datacount = 0;
		if(null != result && StringUtils.isEmpty(result.getErrorMsg())){
			datacount = result.getTotalCount();
		}
		namedParameters.addValue("data_count", datacount);
		StringBuffer requestStr = new StringBuffer();
		for (Entry<String, String[]> entry : requestMap.entrySet()) {
			requestStr.append(entry.getKey());
			requestStr.append("=");
			requestStr.append(entry.getValue()[0]);
			requestStr.append("&");
		}
		if (requestStr.length() != 0 && !"".equals(requestStr)) {
			namedParameters.addValue("input", requestStr.substring(0, requestStr.length() - 1).toString());
		} else {
			namedParameters.addValue("input", "");
		}
		aipJdbcDAO.getTemplate().update(sql, namedParameters);
	}

	/**
	 * 获取
	 * 
	 * @param userName
	 * @return
	 */
	public List<Map<String, Object>> getAllCreator(String userName) {

		String sql = "select distinct (t2.name),t2.login_name from ESB_ROUTE_INFO t1 left join fw_user_info t2 on t1.creator=t2.login_name where t1.creator is not null and t1.creator <> :userName ";

		MapSqlParameterSource paramNamedParameters = new MapSqlParameterSource();

		paramNamedParameters.addValue("userName", userName);
		List<Map<String, Object>> userList = aipJdbcDAO.getTemplate().queryForList(sql.toString(),
				paramNamedParameters);

		return userList;
	}

	public Map<String, Object> getDictInfo(String code) {
		String sql = "select t.dict_value code,t.dict_text,t.dcit_remark  from FW_DICT_INFO t where t.DICT_VALUE = '" + code + "' and t.delete_mark = '0' and t.delete_mark = '0'";
		Map<String, Object> returnMap = aipJdbcDAO.getTemplate().queryForMap(sql, new MapSqlParameterSource());
		return returnMap;
	}
	
	

}
