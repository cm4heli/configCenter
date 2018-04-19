package com.dcits.cloud.service;

import java.io.IOException;
import java.util.Map;

import com.dcits.cloud.model.RuleConfig;
import com.dcits.cloud.model.ServiceInfo;

public interface IService {
	public Object search(ServiceInfo serviceInfo, Map<String, String[]> requestMap) throws IOException;
}
