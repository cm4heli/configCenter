package com.dcits.cloud.model;

import java.util.List;

@SuppressWarnings("serial")
public class PageList<E> implements java.io.Serializable
{
	private List<E>	list;
	private long		count;
	private long pageIndex;
	private long pageSize;
	public long getPageSize()
	{
		return pageSize;
	}
	public void setPageSize(long pageSize)
	{
		this.pageSize = pageSize;
	}
	public long getPageIndex()
	{
		return pageIndex;
	}
	public void setPageIndex(long pageIndex)
	{
		this.pageIndex = pageIndex;
	}
	public PageList()
	{
		
	}
	public PageList(List<E> list,long count)
	{
		this.list=list;
		this.count=count;
	}
	public List<E> getList()
	{
		return list;
	}

	public void setList(List<E> list)
	{
		this.list = list;
	}

	public long getCount()
	{
		return count;
	}

	public void setCount(long count)
	{
		this.count = count;
	}
}
