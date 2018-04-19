package com.dcits.cloud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.cloud.common.Constants;
import com.dcits.cloud.dao.JdbcDAO;
import com.dcits.cloud.dao.SystemDAO;
import com.dcits.cloud.model.ResultStatus;

@Service
public class SystemService {

	@Autowired
	private SystemDAO systemDAO;
	
	
//	public ResultStatus initSystemData(String ip,int port){
//		
//		try{
//			systemDAO.setData(ip, port);
//		}
//		catch(Exception e){
//			return new ResultStatus(e.getMessage());
//		}
//		
//		Constants.AIP_SERVICE_URL = "http://" + ip + ":" + port + "/cloudservice/api";
//		return new ResultStatus();
//	}
	
	
	
	public Object getSystemData(){
		Object returnData = null;
		try{
			returnData = systemDAO.getData();
		}
		catch(Exception e){
			return new ResultStatus(e.getMessage());
		}
		ResultStatus status = new ResultStatus();
		status.setData(returnData);
		return status;
	}
	
	
//	public String getApiUrl(){
//		return systemDAO.initURL();
//	}



	public Object getData() {
		Object returnData = null;
		try{
			returnData = systemDAO.getSystemData();
		}
		catch(Exception e){
			return new ResultStatus(e.getMessage());
		}
		ResultStatus status = new ResultStatus();
		status.setData(returnData);
		return status;
	}
	
	
	public Object getDabaseData(){
		
		return JdbcDAO.source_map;
	}
	

		
}
