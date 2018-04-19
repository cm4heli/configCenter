package com.dcits.cloud.model;

import java.util.List;

/**
 * 返回给外部使用的对象
 * 
 * @author libin
 * 
 */
public class ResultStatus {
	// 请求参数
	private Long pageSize = 15L; // 每页显示行数
	//查询起始值
	private String start = null;

	// 分页参数
	private Boolean hasNext = false; // 是否有下一页
	private Boolean hasPrevious = false; // 是否有前一页
	// 下一页参数值
	private String next = null;
	// 上一页参数值
	private String previous = null;
	// 当前页参数值
	private String current = null;
	//
	private String nextRow = "";
	//
	private Long costTime = 0l;
	private String errorMsg;
	private Boolean hasError = false;
	
	private long totalPage = 0;
	
	private Long totalCount;
	// 数据
	private Object data;

	public ResultStatus() {

	}

	public ResultStatus(String errorMsg) {
		hasError = true;
		this.errorMsg = errorMsg;
	}

	public ResultStatus(Page page) {
		this.data = page.getData();
		this.pageSize = page.getPageSize();
		this.start = ""+page.getCurrentPage();
		this.current = page.getCurrentPage() == 0 ? "1" : ""+page.getCurrentPage();
		if (((List) data).size()>pageSize.longValue()) {
			this.hasNext = true;
			((List) data).remove(((List) data).size() - 1);
		}
		if (page.getCurrentPage().longValue() > 1) {
			this.hasPrevious = true;
		}
		this.nextRow = page.getNextRow();
		this.costTime = page.getCostTime();
		this.next = this.current + 1;
		this.previous =   ""+(Long.valueOf(this.current )- 1);
		this.totalCount = page.getRecords();
		this.totalPage = page.getTotalPage();
	}

	public Long getPageSize() {
		return pageSize;
	}

	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Boolean getHasNext() {
		return hasNext;
	}

	public void setHasNext(Boolean hasNext) {
		this.hasNext = hasNext;
	}

	public Boolean getHasPrevious() {
		return hasPrevious;
	}

	public void setHasPrevious(Boolean hasPrevious) {
		this.hasPrevious = hasPrevious;
	}

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getPrevious() {
		return previous;
	}

	public void setPrevious(String previous) {
		this.previous = previous;
	}

	public String getCurrent() {
		return current;
	}

	public void setCurrent(String current) {
		this.current = current;
	}

	public String getNextRow() {
		return nextRow;
	}

	public void setNextRow(String nextRow) {
		this.nextRow = nextRow;
	}

	public Long getCostTime() {
		return costTime;
	}

	public void setCostTime(Long costTime) {
		this.costTime = costTime;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Boolean getHasError() {
		return hasError;
	}

	public void setHasError(Boolean hasError) {
		this.hasError = hasError;
	}

	public long getTotalPage() {
		return totalPage;
	}

	public Long getTotalCount() {
		return totalCount;
	}
	

}
