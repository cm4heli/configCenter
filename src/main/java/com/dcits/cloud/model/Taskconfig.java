package com.dcits.cloud.model;

import java.sql.Timestamp;
/**
 * 任务配置信息
 *
 */
public class Taskconfig {
	private String id;
	private String taskname;  //任务名称
	private String taskdesc;   //任务描述
	private Timestamp createtime;  //配置创建时间
	private Timestamp updatetime; // 配置修改时间
	private Integer tasktype;     // 任务类型    1--定时任务   2--一次性任务
	private String executetime;   // 任务具体执行时间
	private Integer exetype;      // 执行方式，每月、每天、 时间间隔
	private Integer exemonth;      //暂时没有用
	private Integer exeday;       // 1、如果频率是每月则保存时间 1点到31号 ；2、如果时间隔频率保存多少天数据  
	private Integer exehour;      // 1、如果频率是每月、每天则保存时间 1点到24小时点 ；2、如果时间隔频率保存几个小时数据  
	private Integer exeminute;    // 1、如果频率是每月、每天则保存时间 1点到60分钟 ；2、如果时间隔频率保存多少分钟数据  
	private String taskservice;   //任务调度模型逻辑的接口服务

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTaskname() {
		return taskname;
	}

	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}

	public String getTaskdesc() {
		return taskdesc;
	}

	public void setTaskdesc(String taskdesc) {
		this.taskdesc = taskdesc;
	}

	public Timestamp getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	public Timestamp getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}

	public Integer getTasktype() {
		return tasktype;
	}

	public void setTasktype(Integer tasktype) {
		this.tasktype = tasktype;
	}

	public String getExecutetime() {
		return executetime;
	}

	public void setExecutetime(String executetime) {
		this.executetime = executetime;
	}

	public Integer getExetype() {
		return exetype;
	}

	public void setExetype(Integer exetype) {
		this.exetype = exetype;
	}

	public Integer getExemonth() {
		return exemonth;
	}

	public void setExemonth(Integer exemonth) {
		this.exemonth = exemonth;
	}

	public Integer getExeday() {
		return exeday;
	}

	public void setExeday(Integer exeday) {
		this.exeday = exeday;
	}

	public Integer getExehour() {
		return exehour;
	}

	public void setExehour(Integer exehour) {
		this.exehour = exehour;
	}

	public Integer getExeminute() {
		return exeminute;
	}

	public void setExeminute(Integer exeminute) {
		this.exeminute = exeminute;
	}

	public String getTaskservice() {
		return taskservice;
	}

	public void setTaskservice(String taskservice) {
		this.taskservice = taskservice;
	}

}
