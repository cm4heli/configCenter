package com.dcits.cloud.service.task;

import org.springframework.stereotype.Service;

import com.dcits.cloud.exception.TaskServiceException;
import com.dcits.cloud.service.AbstractService;

@Service("pLSQLService")
public class PLSQLService extends AbstractService implements TaskExeService {

	 /**
	  * 执行存储过程调度
	  * @param serviceId  调度服务配置ID
	  * @throws TaskServiceException
	  */
     public void execute(String sericeType,String serviceId) throws TaskServiceException{
    	 //根据ID查询配置信息
    	 //根据配置信息连接数据库
    	 //执行存储过程
     }
   
}
