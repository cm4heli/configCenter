package com.dcits.cloud.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.cloud.dao.SqlModelDAO;
import com.dcits.cloud.model.ResultStatus;
import com.dcits.cloud.utils.Utils;

@Service("sqlModelService")
public class SqlModelService {
	
	@Autowired
	private SqlModelDAO sqlModelDAO;
	
	public ResultStatus getSqlModel(){
		ResultStatus resultStatus = new ResultStatus();
		resultStatus.setData(sqlModelDAO.getSqlModel());
		return resultStatus;
	}
	
	
	/**
	 * 保存模板信息
	 * @return
	 * @author 陈明
	 * 2017年1月5日
	 */
	public ResultStatus saveSqlModel(String config){
		
		Map<String,Object> configMap = Utils.jsonToMap(config);
		
		try{
			int code = sqlModelDAO.saveSqlModel(configMap);
		}
		catch(Exception e){
			return new ResultStatus("保存出错(" + e.getMessage() + ")"); 
		}
		return new ResultStatus();
	}
	
}
