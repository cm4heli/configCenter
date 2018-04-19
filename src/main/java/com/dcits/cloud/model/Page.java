package com.dcits.cloud.model;

import java.util.List;

public class Page {
	public static Long PAGESIZE = 15L;

	/**
	 * 每页显示行数
	 */
	private Long pageSize = PAGESIZE; // 每页显示行数

	/**
	 * 总行数
	 */
	private Long totalCount = 0L; // 总行数

	/**
	 * 总页数
	 */
	private Long totalPage = 0L; // 总页数

	/**
	 * 从页面传过来的页数
	 */
	private Long startPage = 1L; // 从页面传过来的页数

	/**
	 * 是否有下一页
	 */
	private Boolean hasNextPage = false; // 是否有下一页

	/**
	 * 是否有前一页
	 */
	private Boolean hasPreviousPage = false; // 是否有前一页

	private Long startIndex = 0L; // 起始num

	//针对hbase的 起始rowkey做标记
	private String nextRow = "";
	
	private Long costTime = 0l;
	
	private Object data;

	public Page() {

	}

	public Page(Long startPage) {
		if(startPage == null || startPage.longValue()==0){
			startPage = 1L;
		} 
		this.startPage = startPage;
	}

	public Page(Long startPage, Long pageSize) {
		if(startPage == null || startPage.longValue()==0){
			startPage = 1L;
		}
		if(pageSize != null && pageSize.longValue() !=0){
			this.pageSize = pageSize;
		}
		this.startPage = startPage;
	}

	/**
	 * @return Returns the hasNextPage.
	 */
	public Boolean getHasNextPage() {
		try{
			List<Object> datalist = (List<Object>)data;
			if (datalist.size() == this.getPageSize() + 1) {
				hasNextPage = true;
			} else {
				hasNextPage = false;
			}
		}
		catch(Exception e){
			return hasNextPage;
		}
		
		return hasNextPage;
	}

	/**
	 * @return Returns the hasPreviousPage.
	 */
	public Boolean getHasPreviousPage() {
		if ((getCurrentPage() - 1) > 0) {
			hasPreviousPage = true;
		} else {
			hasPreviousPage = false;
		}
		return hasPreviousPage;
	}

	/**
	 * 获得下一页的页数
	 * 
	 * @return
	 */
	public Long getNextPage() {
		return startPage + 1;
	}

	public Long getStartIndex() {
		startIndex = (startPage - 1) * pageSize;
		return startIndex;
	}

	/**
	 * @return Returns the totalPage.
	 */
	public Long getTotalPage() {
		
		if(totalCount == 0){
			return 0l;
		}
		totalPage = totalCount / pageSize;
		if (totalCount % pageSize > 0 || totalCount == 0) {
			totalPage += 1;
		}
		return totalPage;
	}

	/**
	 * @return Returns the currPage.
	 */
	public Long getCurrentPage() {
		return startPage;
	}

	/**
	 * @param page
	 *            The page to set.
	 */
	public void setCurrentPage(Long page) {
		this.startPage = page;
	}

	public Long getPageSize() {
		return pageSize;
	}

	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 获得上一页的页数
	 * 
	 * @return
	 */
	public Long getPreviousPage() {
		return startPage - 1;
	}

	public Long getRecords() {
		return totalCount;
	}

	public void setRecords(Long totalCount) {
		this.totalCount = totalCount;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Long getCostTime() {
		return costTime;
	}

	public void setCostTime(Long costTime) {
		this.costTime = costTime;
	}

	public String getNextRow() {
		return nextRow;
	}

	public void setNextRow(String nextRow) {
		this.nextRow = nextRow;
	}

	
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public Long getTotalCount() {
		return totalCount;
	}
	


}
