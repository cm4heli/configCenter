package com.dcits.cloud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.cloud.service.hbase.HbaseService;
import com.dcits.cloud.service.oracle.CloudOracleService;
import com.dcits.cloud.service.transwarp.InceptorService;

@Service("serviceFactory")
public class ServiceFactory {
	@Autowired
	private InceptorService inceptorService;
	@Autowired
	HbaseService hbaseService;
	@Autowired
	private CloudOracleService oracleService;
	public InceptorService getInceptorService() {
		return inceptorService;
	}
	
	public HbaseService getHbaseService(){
		return hbaseService;
	}
	
	
	public CloudOracleService getOracleService(){
		return oracleService;
	}
	
}
