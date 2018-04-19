package com.dcits.cloud.service.task;

import com.dcits.cloud.exception.TaskServiceException;

public interface TaskExeService {
	public void execute(String sericeType,String serviceId) throws TaskServiceException;
}
