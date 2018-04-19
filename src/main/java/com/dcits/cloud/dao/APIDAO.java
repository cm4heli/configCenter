package com.dcits.cloud.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("apiDAO")
@Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED)
public class APIDAO {
	@Autowired
	private JdbcDAO jdbcDAO;
	// 检查contians的正则表达式，可以作简单的动态参数设值
	private String containsRege = "contains[(][^)]*[)]";

	public Object load(Map<String, String> mapConfig, HashMap<String, Object> pamamMap) {
		String sql = mapConfig.get("sql");
		// 设值条件改变sql语法
		Set<String> keys = pamamMap.keySet();
		Iterator<String> it = keys.iterator();
		while (it.hasNext()) {
			String key = it.next();
			sql = updateSqlConditon(sql, key, pamamMap.get(key));
		}
		// 参数设值
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		// 执行查询
		//jdbcDAO.getInceptorJdbcTemplate("").execute("set ngmr.exec.mode=local", null);
		List<Map<String, Object>> map = jdbcDAO.getInceptorJdbcTemplate("").queryForList(sql, namedParameters);

		return map;
	}

	public String updateSqlConditon(String sql, String conditionName, Object value) {
		Pattern pattern = Pattern.compile(containsRege);
		Matcher matcher = pattern.matcher(sql);
		while (matcher.find()) {
			String mstr = matcher.group();
			if (value != null && value.toString().trim().length() > 0 && mstr.contains(":" + conditionName)) {
				sql = sql.replace(":" + conditionName, value.toString());
			} else if ((value == null || value.toString().trim().length() == 0) && mstr.contains(":" + conditionName)) {
				sql = sql.replace(mstr, "1=1");
			}
		}
		return sql;
	}
	
}
