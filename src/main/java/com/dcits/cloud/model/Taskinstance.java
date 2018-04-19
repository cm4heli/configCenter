package com.dcits.cloud.model;

import java.sql.Timestamp;
/**
 * 任务实例表
 *
 */
public class Taskinstance {
	private String id;
	private Integer exestatus;//实例运行状态 1--异常    2--运行中    3--已结束
	private Timestamp createtime;//实例创建时间
	private Timestamp starttime;//实例启动时间
	private Timestamp stoptime;//实例停止时间
	private String taskcfgid;//任务配置主 id

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getExestatus() {
		return exestatus;
	}

	public void setExestatus(Integer exestatus) {
		this.exestatus = exestatus;
	}

	public Timestamp getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	public Timestamp getStarttime() {
		return starttime;
	}

	public void setStarttime(Timestamp starttime) {
		this.starttime = starttime;
	}

	public Timestamp getStoptime() {
		return stoptime;
	}

	public void setStoptime(Timestamp stoptime) {
		this.stoptime = stoptime;
	}

	public String getTaskcfgid() {
		return taskcfgid;
	}

	public void setTaskcfgid(String taskcfgid) {
		this.taskcfgid = taskcfgid;
	}

}
