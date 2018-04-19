package com.dcits.cloud.model;

public class DataSource {

	
	private String id;
	
	private String name;
	
	private String ip;
	
	private String database_type;
	
	private String username;
	
	private String psw;
	
	private int max_active;
	
	private String identify;
	
	private String status;
	
	private String database_name;
	
	private int type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDatabase_type() {
		return database_type;
	}

	public void setDatabase_type(String database_type) {
		this.database_type = database_type;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPsw() {
		return psw;
	}

	public void setPsw(String psw) {
		this.psw = psw;
	}

	public int getMax_active() {
		return max_active;
	}

	public void setMax_active(int max_active) {
		this.max_active = max_active;
	}

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDatabase_name() {
		return database_name;
	}

	public void setDatabase_name(String database_name) {
		this.database_name = database_name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	
	
	
}
