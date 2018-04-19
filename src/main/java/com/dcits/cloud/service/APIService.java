package com.dcits.cloud.service;



import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.cloud.common.Constants; 
import com.dcits.cloud.dao.CacheDAO;
import com.dcits.cloud.dao.ConfigDAO;
import com.dcits.cloud.dao.ServiceDAO;
import com.dcits.cloud.model.ResultStatus;
import com.dcits.cloud.model.RuleConfig;
import com.dcits.cloud.model.ServiceInfo;

/**
 * 服务API逻辑类
 * 
 * @author libin
 * 
 */
@Service("apiService")
public class APIService {
	@Autowired
	private ServiceFactory serviceFactory;
	@Autowired
	private ConfigDAO configDAO;
	
	@Autowired
	private CacheDAO cacheDAO;

	@Autowired
	private ServiceDAO serviceDAO;
	/**
	 * 服务API
	 * 
	 * @param requestMapping
	 * @param requestMap
	 * @param request_type  区分是测试还是实际请求
	 * @return
	 * @throws Exception
	 */
	public Object search(String url, Map<String, String[]> requestMap, int request_type)
			throws Exception {
		Object obj = null;
		Long start = System.currentTimeMillis();
		// 根据url获取配置
		ServiceInfo serviceInfo = serviceDAO.getServiceInfoByUrl(url);
		if(serviceInfo  == null){
			return new ResultStatus("服务地址错误:"+url);
		}
		
		Integer exception = 0;
		String errorMsg = "";
		try{
			// 根据查询类型调用服务
			if (serviceInfo != null) {
				if ( serviceInfo.getSearch_type()== Constants.search_type_inceptor  || serviceInfo.getSearch_type() == Constants.search_type_es) {
					// 为hbase
					IService service = serviceFactory.getInceptorService();
					obj = service.search(serviceInfo, requestMap);
					return obj;
				} else if (serviceInfo.getSearch_type() == Constants.search_type_hbase) {
					IService service = serviceFactory.getHbaseService();
					// 为hbase
					obj = service.search(serviceInfo, requestMap);
					return obj;
				}
				else if(serviceInfo.getSearch_type() == Constants.search_type_oracle){
					IService service = serviceFactory.getOracleService();
					obj = service.search(serviceInfo, requestMap);
					return obj;
				}
				
			}
		}
		catch(Exception e){
			exception = 1;
			errorMsg = e.getMessage();
			throw e;
		}
		finally{
			Long end = System.currentTimeMillis();
			//如果是实际外部请求，则记录日志，测试不记录日志
			if(request_type == 1 && serviceInfo.getWrite_log() == 1 && !"01".equals(serviceInfo.getUse_type())){
				ResultStatus result = (ResultStatus)obj;
				serviceDAO.remeberLOG(exception,errorMsg,serviceInfo,requestMap,end-start,result);
			}
		}
		return null;
	}

	private RuleConfig getConfig(String url) {
		Object obj = configDAO.selectOneByUrl(url);
		if (obj != null) {
			return (RuleConfig) obj;
		} else {
			return null;
		}
	}

}
