package com.dcits.cloud.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
@Repository
@Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED)
public class AipJdbcDAO {

	@Autowired
	private NamedParameterJdbcTemplate aipJdbcTemplate;
	
	@Autowired
	private JdbcTemplateImpl jdbcTemplate  ;
	
	 
	
	public NamedParameterJdbcTemplate getTemplate(){
		return aipJdbcTemplate;
	}
	
	public JdbcTemplateImpl getjdbcTemplate(){
		return jdbcTemplate;
	}
}
