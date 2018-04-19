package com.dcits.cloud.service.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.cloud.common.Constants;
import com.dcits.cloud.common.GlobleCache;
import com.dcits.cloud.dao.hbase.HbaseDAO;
import com.dcits.cloud.model.Page;
import com.dcits.cloud.model.ResultStatus;
import com.dcits.cloud.model.RuleConfig;
import com.dcits.cloud.model.RuleParam;
import com.dcits.cloud.model.ServiceInfo;
import com.dcits.cloud.model.ServiceParamInfo;
import com.dcits.cloud.service.IService;

@Service("hbaseService")
public class HbaseService implements IService {

	@Autowired
	private HbaseDAO hbaseDAO;

	private static Map<String, CompareOp> mapper = new HashMap<String, CompareOp>();

	static {
		mapper.put("大于", CompareOp.GREATER);
		mapper.put("小于", CompareOp.LESS);
		mapper.put("等于", CompareOp.EQUAL);
		mapper.put("大于等于", CompareOp.GREATER_OR_EQUAL);
		mapper.put("小于等于", CompareOp.LESS_OR_EQUAL);
		mapper.put("不等于", CompareOp.NOT_EQUAL);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object search(ServiceInfo serviceInfo, Map<String, String[]> requestMap) throws IOException {
		
		Long startTime = System.currentTimeMillis();
		// 设置请求参数startPage,pageSize
		Long startPage = 0l;
		Long pageSize = 0l;
		if(requestMap.get("start") != null && !"".equals(requestMap.get("start")[0])){
			startPage = Long.valueOf(requestMap.get("pageSize")[0].toString());
		}
		if(requestMap.get("pageSize") != null && !"".equals(requestMap.get("pageSize")[0])){
			pageSize = Long.valueOf(requestMap.get("pageSize")[0].toString());
		}
		Page page = new Page(startPage,pageSize);

		Map<String, Object> data = new HashMap<String, Object>();
		try{
			data = this.resolveConfig(serviceInfo, requestMap,page);
		}
		catch(Exception e){
			return new ResultStatus("查询出错：" + e.getMessage());
		}
		 
		
		Long endTime = System.currentTimeMillis();
		List<Object> dataList = (List<Object>) data.get("dataList");
		page.setData(dataList);
		page.setRecords(dataList != null ? dataList.size() : 0L);
		page.setCostTime(endTime - startTime);
		//记录endrow
		if(null != data.get("endRow")){
			page.setNextRow(data.get("endRow").toString());
		}
		return new ResultStatus(page);
	}

	/**
	 * 解析组装数据
	 * 
	 * @param serviceInfo
	 * @param requestMap
	 * @return
	 * @throws IOException
	 * @author 陈明 2016年11月24日
	 * @param page 
	 */
	private Map<String, Object> resolveConfig(ServiceInfo serviceInfo, Map<String, String[]> requestMap, Page page)
			throws IOException {

		FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
		// 组装request的过滤器
		this.assembleRequestFilter(filterList, serviceInfo, requestMap);
		Map<String,Object> paramMap = this.assembleResponseFilter(filterList, serviceInfo, requestMap);
		this.assemblePageFilter(filterList, serviceInfo, requestMap,page);
		String startRow = "";
		if(null != requestMap.get("startRow")){
			startRow = requestMap.get("startRow")[0].toString();
		}
		List<Map<String, Object>> resultList = hbaseDAO.getRowByFilter(serviceInfo.getTable_name(), filterList,paramMap,startRow);
		return this.assembleResult(resultList, serviceInfo, requestMap);
	}

	/**
	 * 组装page的filter
	 * 
	 * @param filterList
	 * @param serviceInfo
	 * @param requestMap
	 * @author 陈明 2016年11月24日
	 * @param page2 
	 */
	private void assemblePageFilter(FilterList filterList, ServiceInfo serviceInfo, Map<String, String[]> requestMap, Page page) {

//		Long startPage = requestMap.get("startPage") != null ? Long.valueOf(requestMap.get("startPage").toString())
//				: 0L;
		//Long pageSize = requestMap.get("pageSize") != null ? Long.valueOf(requestMap.get("pageSize")[0].toString()) : 0l;

		//每次夺取一条数据
		Filter filter = new PageFilter(page.getPageSize() + 1l);
		filterList.addFilter(filter);
	}

	/**
	 * 组装request 的filter
	 * 
	 * @param filterList
	 * @param serviceInfo
	 * @param requestMap
	 * @author 陈明 2016年11月21日
	 */
	private void assembleRequestFilter(FilterList filterList, ServiceInfo serviceInfo, Map<String, String[]> requestMap) {

		List<ServiceParamInfo> ruleParamList = serviceInfo.getParams();
		for (ServiceParamInfo ruleParam : ruleParamList) {
			if (ruleParam.getParameter_type() == Constants.RESPONSE_TYPE) {
				continue;
			}
			String[] paramValue = requestMap.get(ruleParam.getParameter_name());
			if (null == paramValue || paramValue.length == 0) {
				String[] defaultValue = requestMap.get(ruleParam.getDefault_value());
				if (null == defaultValue || defaultValue.length == 0) {
					continue;
				}
				paramValue = defaultValue;
			}
			if (null == paramValue[0] || "".equals(paramValue[0])) {
				continue;
			}
			String column = ruleParam.getColumn_name();
			String expre = ruleParam.getExpression();

			// 如果是rowkey，则用RowFilter
			if (!column.contains(":")) {

				if ("like".contains(expre)) {
					Filter rowFilter = new RowFilter(CompareFilter.CompareOp.EQUAL,
							new RegexStringComparator(".*" + paramValue[0] + ".*"));
					filterList.addFilter(rowFilter);
				} else {
					Filter filter = new RowFilter(mapper.get(ruleParam.getExpression()),
							new BinaryComparator(Bytes.toBytes(paramValue[0])));
					filterList.addFilter(filter);
				}
				continue;
			}

			// 如果是普通的列
			String[] columns = column.split(":");
			if ("like".equals(expre)) {
				RegexStringComparator comp = new RegexStringComparator(".*" + paramValue[0] + ".*");
				Filter filter = new SingleColumnValueFilter(Bytes.toBytes(columns[0]), Bytes.toBytes(columns[1]),
						CompareOp.EQUAL, comp);
				filterList.addFilter(filter);
				continue;
			}
			Filter filter = new SingleColumnValueFilter(Bytes.toBytes(columns[0]), Bytes.toBytes(columns[1]),
					mapper.get(ruleParam.getExpression()), Bytes.toBytes(paramValue[0]));

			filterList.addFilter(filter);
		}
	}

	/**
	 * 组装返回列的filter
	 * 
	 * @param filterList
	 * @param serviceInfo
	 * @param requestMap
	 * @author 陈明 2016年11月24日
	 * @return
	 */
	public Map<String,Object> assembleResponseFilter(FilterList filterList, ServiceInfo serviceInfo,
			Map<String, String[]> requestMap) {

		Map<String,Object> returnMap = new HashMap<String,Object>();
		List<Map<String, byte[]>> returnList = new ArrayList<Map<String, byte[]>>();
		List<ServiceParamInfo> ruleParamList = serviceInfo.getParams();
		for (ServiceParamInfo ruleParam : ruleParamList) {
			if (ruleParam.getParameter_type() == Constants.REQUEST_TYPE) {
				continue;
			}
			
			String columnName = ruleParam.getColumn_name();
			if (columnName.contains(":")) {
				Map<String, byte[]> paramMap = new HashMap<String, byte[]>();
				String[] columnNameSplit = columnName.split(":");
				paramMap.put("cf", Bytes.toBytes(columnNameSplit[0]));
				paramMap.put("column", Bytes.toBytes(columnNameSplit[1]));
				returnList.add(paramMap);
			}
			else{
				returnMap.put("rowkey", columnName);
			}
			returnMap.put("rowList", returnList);
		}

		return returnMap;

	}

	/**
	 * 根据用户需求返回不同的列
	 * 
	 * @param resultList
	 * @param serviceInfo
	 * @param requestMap
	 * @return
	 * @author 陈明 2016年11月23日
	 */
	private Map<String, Object> assembleResult(List<Map<String, Object>> resultList, ServiceInfo serviceInfo,
			Map<String, String[]> requestMap) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		List<ServiceParamInfo> ruleParamList = serviceInfo.getParams();
		for (Map<String, Object> resultMap : resultList) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			for (int i=0;i<ruleParamList.size();i++) {
				ServiceParamInfo ruleParam = ruleParamList.get(i);
				if(i == ruleParamList.size() - 1){
					returnMap.put("endRow", resultMap.get("rowkey"));
				}
				if (ruleParam.getParameter_type() == Constants.REQUEST_TYPE) {
					continue;
				}

				Object cName = resultMap.get(ruleParam.getColumn_name());

				if (null == cName || "".equals(cName)) {
					cName = ruleParam.getDefault_value();
				}
				paramMap.put(ruleParam.getParameter_name().toLowerCase(), cName);
			}
			returnList.add(paramMap);
		}
		returnMap.put("dataList", returnList);
		return returnMap;
	}

}
