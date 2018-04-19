package com.dcits.cloud.model;

import java.sql.Timestamp;
/**
 * 任务记录
 *
 */
public class Taskrecord {
	private String id;
	private Integer resultstatus;//结果状态 1--异常  2--已完成
	private Timestamp starttime;//开始调用模型时间
	private Timestamp finishtime;//完成时间
	private String taskinstanceid;//任务实例主id
	private String errormsg;//异常信息

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getResultstatus() {
		return resultstatus;
	}

	public void setResultstatus(Integer resultstatus) {
		this.resultstatus = resultstatus;
	}

	public Timestamp getStarttime() {
		return starttime;
	}

	public void setStarttime(Timestamp starttime) {
		this.starttime = starttime;
	}

	public Timestamp getFinishtime() {
		return finishtime;
	}

	public void setFinishtime(Timestamp finishtime) {
		this.finishtime = finishtime;
	}

	public String getTaskinstanceid() {
		return taskinstanceid;
	}

	public void setTaskinstanceid(String taskinstanceid) {
		this.taskinstanceid = taskinstanceid;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

}
