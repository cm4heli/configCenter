package com.dcits.cloud.service.oracle;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.dcits.cloud.common.Constants;
import com.dcits.cloud.dao.ServiceDAO;
import com.dcits.cloud.dao.oracle.CloudOracleDAO;
import com.dcits.cloud.model.Page;
import com.dcits.cloud.model.ResultStatus;
import com.dcits.cloud.model.ServiceInfo;
import com.dcits.cloud.model.ServiceParamInfo;
import com.dcits.cloud.service.IService;
import com.dcits.cloud.utils.Utils;


@Service("cloudOracleService")
public class CloudOracleService implements IService{

	private static final Log log = LogFactory.getLog(CloudOracleService.class);
	
	@Autowired
	private CloudOracleDAO cloudOracleDAO;
	@Autowired
	private ServiceDAO serviceDAO;
	
	@Override
	public Object search(ServiceInfo serviceInfo, Map<String, String[]> requestMap){

		Long startTime = System.currentTimeMillis();
		StringBuffer sql = new StringBuffer();
		MapSqlParameterSource configParameters = new MapSqlParameterSource();
		List<ServiceParamInfo> params = serviceInfo.getParams();
		long start = 1l;
		long size = 15l;
		try{
			start = requestMap.get("start") != null && !"".equals(requestMap.get("start")[0]) ? Integer.valueOf(requestMap
					.get("start")[0].toString()) : 1;
		}
		catch(Exception e){
			return new ResultStatus("参数【start】必须为数字");
		}
		try{
			size = requestMap.get("pageSize") != null && !"".equals(requestMap.get("pageSize")[0]) ? Integer.valueOf(requestMap
					.get("pageSize")[0].toString()) : 15;
			
		}
		catch(Exception e){
			return new ResultStatus("参数【pageSize】必须为数字");
		}
		
		
		if(serviceInfo.getShowTotalData() != null && serviceInfo.getShowTotalData() == 1 && requestMap.get("pageSize") == null){
			size = 10000000000l;
		}
		  
		//如果设置了每页显示的行数，则设置为此值
		if("03".equals(serviceInfo.getUse_type() ) && serviceInfo.getPageSize() != null && serviceInfo.getPageSize() != 0 && serviceInfo.getShowTotalData() == 0){
			size = serviceInfo.getPageSize();
		}
		
		Page page = new Page(start,size == 10000000000l?0l:size);
		sql.append(serviceInfo.getSql_script());
		String sqlStr = sql.toString();
		String revStr = new StringBuffer(sqlStr).reverse().toString();
		for (ServiceParamInfo param : params) {
			if (param.getParameter_type() == Constants.REQUEST_TYPE) {
				if(!"02".equals(serviceInfo.getUse_type()) && !"03".equals(serviceInfo.getUse_type())&&requestMap.get(param.getParameter_name()) == null){
					return new ResultStatus("缺少参数[" + param.getParameter_name() + "]");
				}
				
				String value = "";
				if (requestMap.get(param.getParameter_name()) != null
						&& !"".equals(requestMap.get(param.getParameter_name())[0].trim())) {
					value = requestMap.get(param.getParameter_name())[0];
				}else{
					if(param.getDefault_value() != null && !"".equals(param.getDefault_value().trim())){
						String defaultValue = param.getDefault_value();
						if("date".equalsIgnoreCase(param.getData_type())){
							if(defaultValue.toLowerCase().contains("today") || defaultValue.contains("#")){
								value = getDefaultTime(defaultValue);
							}
							else{
								value = defaultValue;
							}
						}
						else{
							value = defaultValue;
						}
					}
				}
				if(StringUtils.isEmpty(value)){
					String paramname = param.getParameter_name();
					String reg  = new StringBuffer(paramname).reverse().toString() + ":.*?(\\=<|\\=>|\\=|ekil|<>|ni\\s)";
					Pattern pattern1 = Pattern.compile(reg);
				    
				    Matcher matcher1 = pattern1.matcher(revStr);
				    while(matcher1.find()){
				    	String findStr = matcher1.group();
				    	
				    	if(findStr.contains("ekil")){
				    		String newfindStr = findStr.replace("ekil", " ekil ton ");
				    		revStr = revStr.replace(findStr, newfindStr);
				    	}
				    	else{
				    		String newfindStr = findStr.replaceAll("(\\=<|\\=>|\\=|ekil)", " >< ");
				    		revStr = revStr.replace(findStr, newfindStr);
				    	}
				    	
				    	//加入一个不会匹配上的数据
				    	configParameters.addValue(param.getParameter_name(), "112233561676");
				    }
				}
				else{
					configParameters.addValue(param.getParameter_name(), value);					
				}
			}
		}
		
		sqlStr = new StringBuffer(revStr).reverse().toString();

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
		
		String requestStr = "".equals(resParams.toString())? " * ": resParams.toString();
		String executeSql = "select " + requestStr +" from ("+sqlStr.toString()+")";

		log.info("execute sql : " + executeSql);
		
		long totalPage = 0l;
		//总页数查询
		if(true){
			
			try{
				totalPage = cloudOracleDAO.getTotal(executeSql,configParameters ,size, serviceInfo.getDatabase_identity());
			}
			catch(Exception e){
				return new ResultStatus("获取总数出错:" + e.getMessage());
			}
		}
		
		if("03".equals(serviceInfo.getUse_type() ) && serviceInfo.getPageSize() != null && serviceInfo.getPageSize() != 0 && serviceInfo.getShowTotalData() == 0){
			Map<String,Object> dictInfo= serviceDAO.getDictInfo("XTYYPZ-STSJCXTSXZ");
			if(!StringUtils.isEmpty(dictInfo.get("DCIT_REMARK"))){
				String dictDesc = dictInfo.get("DCIT_REMARK").toString().trim();
				Pattern pattern = Pattern.compile("^\\d+");
				Matcher matcher = pattern.matcher(dictDesc);
				if (matcher.find()) {
					if (totalPage > Long.valueOf(matcher.group())){
						totalPage = Long.valueOf(matcher.group());
					}
				}
			}
		}
		
		
		//数据查询
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		try {
			dataList = cloudOracleDAO.getPager(resParams.toString(),configParameters,executeSql, serviceInfo, start, size,totalPage);
		} catch (Exception e) {
			return new  ResultStatus(e.getMessage());
		} 

		List<Map<String,Object>> returnList = Utils.parseData(serviceInfo,dataList);
		Long endtime = System.currentTimeMillis();
		page.setCostTime(endtime-startTime);
		page.setData(returnList);
		page.setRecords(totalPage);
		if(size == 10000000000l){
			page.setPageSize(Long.valueOf(returnList.size() + ""));
			page.setTotalCount(Long.valueOf(returnList.size() + ""));
		}
		log.info("return data size : " + returnList.size());
		return new ResultStatus(page);
	}
	

	private String getDefaultTime(String time){
		if(time.contains("#")){
			SimpleDateFormat formatter = new SimpleDateFormat(time.replace("#", ""));
			return formatter.format(new Date(System.currentTimeMillis()));
		}
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd"); 
		String [] defaultSplit = time.split("\\+|\\-");
		
		if(defaultSplit.length == 1){
			String nowTime = formatter.format(new Date(System.currentTimeMillis()));
			return nowTime;
		}
		
		String reg  = "\\d";
		Pattern pattern1 = Pattern.compile(reg);
	    
		Integer timeNum = 0;
	    Matcher matcher1 = pattern1.matcher(defaultSplit[1]);
	    if(matcher1.find()){
	    	timeNum = Integer.valueOf(matcher1.group());
	    }
	    
	    String reg2  = "[a-zA-Z]";
		Pattern pattern2 = Pattern.compile(reg2);
	    
		String timeType = "";
	    Matcher matcher2 = pattern2.matcher(defaultSplit[1]);
	    if(matcher2.find()){
	    	timeType = matcher2.group();
	    }
	    
	    Calendar c = Calendar.getInstance();
	    c.setTime(new Date(System.currentTimeMillis()));
	    if(time.contains("-")){
	    	timeNum = 0 - timeNum;
	    }
	    switch(timeType.toLowerCase()){
	    	case "d": c.add(Calendar.DATE, timeNum);break;
	    	case "w": c.add(Calendar.DATE, timeNum * 7);break;
	    	case "M": c.add(Calendar.MONTH, timeNum);break;
	    	case "y": c.add(Calendar.YEAR, timeNum);break;
	    }
	    
	    String nowTime = formatter.format(c.getTime());
	    
	    
	    return nowTime;
	}

	
	
}
