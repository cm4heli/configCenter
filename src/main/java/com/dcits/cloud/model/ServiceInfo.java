package com.dcits.cloud.model;

import java.sql.Date;
import java.util.List;

import com.dcits.cloud.common.Constants;
import com.kenai.constantine.Constant;

public class ServiceInfo {

	private String res_id;
	
	private String show_url;
	private String publish_url;
	
	private String srv_url;
	
	private String res_typ;
	private Integer route_status;
	private Integer running_status;
	
	private String res_nm;
	private String res_desc;
	private Integer is_auth;
	private Date crt_dt;
		
	private String asset_id;
	private Integer file_size;
	private String file_name;
	
	private String provider;	
	private Integer service_type;
	private Integer write_log;
	private Integer match_on_uriprefix;
	private Integer allowDel;
	private String fileuuid;
	
	private String database_identity;
	
	private String sql_script;
	
	private String table_name;
	
	private Integer search_type;
	
	private String use_type;
	
	private Integer pageSize;
	
	private Integer showTotal;
	
	private Integer is_publish;
	
	private String creator;
	
	private String name;
	
	private Integer showTotalData;
	
	private List<ServiceParamInfo> params ;

	public String getRes_id() {
		return res_id;
	}

	public void setRes_id(String res_id) {
		this.res_id = res_id;
	}

	public String getPublish_url() {
		return publish_url;
	}

	public void setPublish_url(String publish_url) {
		this.publish_url = publish_url;
	}

	public String getSrv_url() {
		return srv_url;
	}

	public void setSrv_url(String srv_url) {
		this.srv_url = srv_url;
	}

	public String getRes_typ() {
		return res_typ;
	}

	public void setRes_typ(String res_typ) {
		this.res_typ = res_typ;
	}

	public Integer getRoute_status() {
		return route_status;
	}

	public void setRoute_status(Integer route_status) {
		this.route_status = route_status;
	}

	public Integer getRunning_status() {
		return running_status;
	}

	public void setRunning_status(Integer running_status) {
		this.running_status = running_status;
	}

	public String getRes_nm() {
		return res_nm;
	}

	public void setRes_nm(String res_nm) {
		this.res_nm = res_nm;
	}

	public String getRes_desc() {
		return res_desc;
	}

	public void setRes_desc(String res_desc) {
		this.res_desc = res_desc;
	}

	public Integer getIs_auth() {
		return is_auth;
	}

	public void setIs_auth(Integer is_auth) {
		this.is_auth = is_auth;
	}

	public Date getCrt_dt() {
		return crt_dt;
	}

	public void setCrt_dt(Date crt_dt) {
		this.crt_dt = crt_dt;
	}

	public String getAsset_id() {
		return asset_id;
	}

	public void setAsset_id(String asset_id) {
		this.asset_id = asset_id;
	}

	public Integer getFile_size() {
		return file_size;
	}

	public void setFile_size(Integer file_size) {
		this.file_size = file_size;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public Integer getService_type() {
		return service_type;
	}

	public void setService_type(Integer service_type) {
		this.service_type = service_type;
	}

	public Integer getWrite_log() {
		return write_log;
	}

	public void setWrite_log(Integer write_log) {
		this.write_log = write_log;
	}

	public Integer getMatch_on_uriprefix() {
		return match_on_uriprefix;
	}

	public void setMatch_on_uriprefix(Integer match_on_uriprefix) {
		this.match_on_uriprefix = match_on_uriprefix;
	}

	public String getFileuuid() {
		return fileuuid;
	}

	public void setFileuuid(String fileuuid) {
		this.fileuuid = fileuuid;
	}

	public String getDatabase_identity() {
		return database_identity;
	}

	public void setDatabase_identity(String database_identity) {
		this.database_identity = database_identity;
	}

	public String getSql_script() {
		return sql_script;
	}

	public void setSql_script(String sql_script) {
		this.sql_script = sql_script;
	}

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public Integer getSearch_type() {
		return search_type;
	}

	public void setSearch_type(Integer search_type) {
		this.search_type = search_type;
	}

	public List<ServiceParamInfo> getParams() {
		return params;
	}

	public void setParams(List<ServiceParamInfo> params) {
		this.params = params;
	}

	public String getUse_type() {
		return use_type;
	}

	public void setUse_type(String use_type) {
		this.use_type = use_type;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getShowTotal() {
		return showTotal;
	}

	public void setShowTotal(Integer showTotal) {
		this.showTotal = showTotal;
	}

	public Integer getIs_publish() {
		return is_publish;
	}

	public void setIs_publish(Integer is_publish) {
		this.is_publish = is_publish;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getShowTotalData() {
		return showTotalData;
	}

	public void setShowTotalData(Integer showTotalData) {
		this.showTotalData = showTotalData;
	}

	public String getShow_url() {
		String url = "";
		if("01".equals(this.use_type) && Constants.SSPSERVER_MODE == 1){
			url = "http://" + Constants.PROXY_SERVICE_IP + ":" + Constants.PROXY_SERVICE_PORT + "/service/" + this.publish_url;
		}
		else{
			url = Constants.AIP_SERVICE_URL + "/" + this.publish_url;
		}
		return url;
	}

	public void setShow_url(String show_url) {
		this.show_url = show_url;
	}

	public Integer getAllowDel() {
		return allowDel;
	}

	public void setAllowDel(Integer allowDel) {
		this.allowDel = allowDel;
	}
	
	
	
}
