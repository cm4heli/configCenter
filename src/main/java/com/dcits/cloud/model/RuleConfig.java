package com.dcits.cloud.model;

import java.util.List;
import java.util.Map;

public class RuleConfig extends AbstractModel implements IRuleConfig {
	// 服务配置基本信息
	private String serviceName;
	private String serviceAddr;
	private Integer authType;
	private Integer logFlag;
	private String serviceDesc;
	private Integer opFlag;
	private Integer registerStatus = 0; //0-未注册  1-已注册
	// 规则库(所属的库和表)
	private RuleRepo ruleRepo = new RuleRepo();

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceAddr() {
		return serviceAddr;
	}

	public void setServiceAddr(String serviceAddr) {
		this.serviceAddr = serviceAddr;
	}

	public Integer getAuthType() {
		return authType;
	}

	public void setAuthType(Integer authType) {
		this.authType = authType;
	}

	public Integer getLogFlag() {
		return logFlag;
	}

	public void setLogFlag(Integer logFlag) {
		this.logFlag = logFlag;
	}

	public String getServiceDesc() {
		return serviceDesc;
	}

	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}

	public Integer getOpFlag() {
		return opFlag;
	}

	public void setOpFlag(Integer opFlag) {
		this.opFlag = opFlag;
	}

	public RuleRepo getRuleRepo() {
		return ruleRepo;
	}

	public void setRuleRepo(RuleRepo ruleRepo) {
		this.ruleRepo = ruleRepo;
	}

	@Override
	public void addRuleRepo(RuleRepo ruleRepo) {
		this.ruleRepo = ruleRepo;
	}

	@Override
	public void addRuleParams(List<RuleParam> ruleParams) {
		this.ruleRepo.setRuleParams(ruleParams);
	}

	public Integer getRegisterStatus() {
		return registerStatus;
	}

	public void setRegisterStatus(Integer registerStatus) {
		this.registerStatus = registerStatus;
	}

	@Override
	public void init(Map<String, Object> initData) {
		this.authType = Integer.valueOf(initData.get("authType").toString());
		this.logFlag = Integer.valueOf(initData.get("logFlag").toString());

		Object opFl = initData.get("opFlag");
		// if(null != opFl && !"".equals(opFl));
		// {
		// this.opFlag = Integer.valueOf(opFl.toString());
		// }
		//
		Object id = initData.get("id");
		if(null != id && !"".equals(id)){
			this.setId(id.toString());
		}
		this.serviceAddr = initData.get("serviceAddr").toString();
		this.serviceDesc = initData.get("serviceDesc").toString();
		this.serviceName = initData.get("serviceName").toString();
	}

}
