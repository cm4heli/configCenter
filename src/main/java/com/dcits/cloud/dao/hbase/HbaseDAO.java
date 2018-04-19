package com.dcits.cloud.dao.hbase;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.dcits.cloud.common.GlobleCache;
import com.dcits.cloud.dao.JdbcDAO;

@Repository
public class HbaseDAO {

	@Autowired
	private JdbcDAO jdbcDAO;

	private static HConnection connection = null;
//	static {
//		try {
//			Configuration configuration = HBaseConfiguration.create();
//			connection = HConnectionManager.createConnection(configuration);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	public List<String> getTabesList() {
		List<String> tableList = new ArrayList<>();
		try {
			TableName[] tables = connection.listTableNames();
			for (TableName table : tables) {
				tableList.add(table.getNameAsString());
			}
			return tableList;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tableList;
	}

	public Object getRowByrowkey(String tableName, String rowkey) throws IOException {
		HTableInterface table = connection.getTable(tableName);

		Get get = new Get(Bytes.toBytes(rowkey));

		Result result = table.get(get);
		return null;
	}

	/**
	 * 根据filter筛选结果
	 * 
	 * @param tableName
	 * @param filters
	 * @return
	 * @author 陈明 2016年11月21日
	 * @param paramMap
	 * @param stratRow 
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getRowByFilter(String tableName, Filter filters, Map<String, Object> paramMap, String stratRow)
			throws IOException {
		HTableInterface table = connection.getTable(tableName);
		Scan scan = new Scan();
		String rowkey = null;
		if (paramMap != null) {
			if (paramMap.containsKey("rowkey")) {
				rowkey = paramMap.get("rowkey").toString();
			}
			List<Map<String, byte[]>> paramList = (List<Map<String, byte[]>>) paramMap.get("rowList");
			for (Map<String, byte[]> param : paramList) {
				scan.addColumn(param.get("cf"), param.get("column"));
			}
		}
		if(!"".equals(stratRow)){
			scan.setStartRow(Bytes.toBytes(stratRow));
		}
		scan.setFilter(filters);
		ResultScanner rs = table.getScanner(scan);
		table.close();
		return assembleResult(rs, tableName, rowkey);
	}

	public byte[] getRowByRowkeyCF(String tableName, String rowkey, String columnFamily,String column) throws IOException {

		HTableInterface table = connection.getTable(tableName);

		Get get = new Get(Bytes.toBytes(rowkey));
		get.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(column));
		Result result = table.get(get);
		byte[] returnByte = null;
		for(KeyValue kv : result.raw()){
			returnByte = kv.getValue();
		}
		return returnByte;
	}
	
	
	/**
	 * 通过
	 * @param tableName
	 * @param rowkey
	 * @param columnFamily
	 * @param column
	 * @param value
	 * @return
	 * @author 陈明
	 * 2016年12月15日
	 * @throws IOException 
	 */
	public void saveData(String tableName,String rowkey,String columnFamily,String column,byte[] value) throws IOException{
		
		
		HTableInterface table = connection.getTable(tableName);
		Put put = new Put(Bytes.toBytes(rowkey));
		
		put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column),	value);
		table.put(put);
		table.close();
	}

	/**
	 * 组装数据
	 * 
	 * @param rs
	 * @return
	 * @author 陈明 2016年11月23日
	 * @param tableName
	 * @param rowkey2
	 */
	@SuppressWarnings("deprecation")
	private List<Map<String, Object>> assembleResult(ResultScanner rs, String tableName, String rowkeyName) {
		List<Map<String, Object>> rowList = new ArrayList<Map<String, Object>>();
		for (Result r : rs) {
			Map<String, Object> rowMap = new HashMap<String, Object>();
			for (KeyValue kv : r.raw()) {
				String rowkey = Bytes.toString(kv.getRow());
				String columnFamily = Bytes.toString(kv.getFamily());
				String column = Bytes.toString(kv.getQualifier());
				String value = Bytes.toString(kv.getValue());
				if (null != rowkeyName) {
					rowMap.put(rowkeyName, rowkey);
				}
				rowMap.put("rowkey", rowkey);
				rowMap.put(columnFamily + ":" + column, value);
			}
			rowList.add(rowMap);
		}
		return rowList;
	}

	/**
	 * 初始化rowkey
	 * 
	 * @author 陈明 2016年11月23日
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> initRowkey() {

		Map<String, String> rowkeyMap = new HashMap<String, String>();
		String sql = "select distinct TABLE_NAME,ROWKEY from SYS_TABLE_INFO";
		System.out.println(jdbcDAO);
		rowkeyMap = (Map<String, String>) jdbcDAO.getAipJdbcTemplate().query(sql, new ResultSetExtractor() {

			@Override
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<String, String> returnMap = new HashMap<String, String>();
				while (rs.next()) {
					returnMap.put(rs.getString("TABLE_NAME"), rs.getString("ROWKEY"));
				}

				return returnMap;
			}

		});

		GlobleCache.setRowkey(rowkeyMap);
		return rowkeyMap;
	}
}
