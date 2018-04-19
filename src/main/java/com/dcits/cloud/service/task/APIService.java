package com.dcits.cloud.service.task;

import org.springframework.stereotype.Service;

import com.dcits.cloud.exception.TaskServiceException;
import com.dcits.cloud.service.AbstractService;

@Service("aPIService")
public class APIService extends AbstractService implements TaskExeService {

	 /**
	  * 执行存储过程调度
	  * @param serviceId  调度服务配置ID
	  * @throws TaskServiceException
	  */
     public void execute(String sericeType,String serviceId) throws TaskServiceException{
    	 //根据ID查询配置信息
    	 //调用服务API url
     }
   
}
