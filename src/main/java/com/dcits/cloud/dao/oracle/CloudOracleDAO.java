package com.dcits.cloud.dao.oracle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dcits.cloud.dao.JdbcDAO;
import com.dcits.cloud.model.ServiceInfo;
import com.dcits.cloud.utils.Utils;

@Repository
public class CloudOracleDAO {

	@Autowired
	private JdbcDAO jdbcDAO;
	
	
	/**
	 * 分页查询
	 * @param params 
	 * @param configParameters 
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getPager(String params, MapSqlParameterSource configParameters, String sql, ServiceInfo serviceInfo,long startIndex,
			long pageSize,long totalPage) throws Exception {
		StringBuffer exeSql = new StringBuffer("select " + params +" from (   select ROWNUM as row_num , a.*  from ( ");
		exeSql.append(sql);
		long endRow = Utils.getEndRowNo(startIndex, pageSize);
		if(endRow > totalPage + 1 && "03".equals(serviceInfo.getUse_type())){
			endRow = totalPage + 1;
		}
		exeSql.append(" )  a  where rownum <" + endRow +"  ) b  where b.row_num >" + Utils.getStartRowNo(startIndex, pageSize) + " ");
		
		//获取连接
		NamedParameterJdbcTemplate template = jdbcDAO.getDbmsConnection(serviceInfo.getDatabase_identity());
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		datas = template.queryForList(exeSql.toString(),configParameters);
		return datas;
	}
	
	
	/**
	 * 获取总页数
	 * @param sql
	 * @param configParameters 
	 * @param pageSize
	 * @return
	 */
	public Long getTotal(String sql,MapSqlParameterSource configParameters, Long pageSize,String identify){
		
		String exeSQL = "select count(0) from (" + sql +")";
		
		NamedParameterJdbcTemplate template;
		try {
			template = jdbcDAO.getDbmsConnection(identify);
		} catch (Exception e) {
			return 0l;
		}
		
		Map<String,Object> dataMap = template.queryForMap(exeSQL, configParameters);
		
		if(dataMap.get("count(0)") == null){
			return 0l;
		}
		else{
			return Long.valueOf(dataMap.get("count(0)").toString());
		}
		
		
	}

}
