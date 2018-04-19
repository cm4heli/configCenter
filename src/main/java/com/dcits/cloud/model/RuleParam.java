package com.dcits.cloud.model;

public class RuleParam extends AbstractModel {
	private Integer requestType;
	private String paramName;
	private String columnName;
	private String paramType;
	private String zhName;
	private String defaultValue;
	private String expresstion;
	private String repoId;
	private String paramId;


	public String getParamId() {
		return paramId!=null?paramId.trim():null;
	}

	public void setParamId(String paramId) {
		this.paramId = paramId;
	}

	public Integer getRequestType() {
		return requestType;
	}

	public void setRequestType(Integer requestType) {
		this.requestType = requestType;
	}

	public String getParamName() {
		return paramName!=null?paramName.trim():null;
	}

	public void setParamName(String paramName) {
		this.paramName = (paramName !=null ? paramName:paramName);
	}

	public String getColumnName() {
		return columnName!=null?columnName.trim():null;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getParamType() {
		return paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
	}

	public String getZhName() {
		return zhName!=null?zhName.trim():null;
	}

	public void setZhName(String zhName) {
		this.zhName = zhName;
	}

	public String getDefaultValue() {
		return defaultValue!=null?defaultValue.trim():null;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getExpresstion() {
		return expresstion;
	}

	public void setExpresstion(String expresstion) {
		this.expresstion = expresstion;
	}

	public String getRepoId() {
		return repoId;
	}

	public void setRepoId(String repoId) {
		this.repoId = repoId;
	}

}
