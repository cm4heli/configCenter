package com.dcits.cloud.model;

import java.util.List;

public class RuleRepo extends AbstractModel {
	// 规则库表基本信息
	private Integer searchType;
	private String nameSpace;
	private String tableName;
	private String sqlScript;
	private String configId;
	private String  rowkey;
	// 对应的请求和参数信息
	private List<RuleParam> ruleParams;

	public Integer getSearchType() {
		return searchType;
	}

	public void setSearchType(Integer searchType) {
		this.searchType = searchType;
	}

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getSqlScript() {
		return sqlScript;
	}

	public void setSqlScript(String sqlScript) {
		this.sqlScript = sqlScript;
	}

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public List<RuleParam> getRuleParams() {
		return ruleParams;
	}

	public void setRuleParams(List<RuleParam> ruleParams) {
		this.ruleParams = ruleParams;
	}

	public String getRowkey() {
		return rowkey;
	}

	public void setRowkey(String rowkey) {
		this.rowkey = rowkey;
	}

}
