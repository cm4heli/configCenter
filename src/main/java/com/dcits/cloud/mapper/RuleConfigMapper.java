package com.dcits.cloud.mapper;

import java.util.List;
import java.util.Map;

//import org.apache.ibatis.annotations.Insert;
//import org.apache.ibatis.annotations.Select;
//import org.apache.ibatis.annotations.SelectProvider;

import com.dcits.cloud.model.RuleConfig;

public interface RuleConfigMapper {
	// @Select("SELECT * FROM SQLConfig WHERE id = #{id} and name=#{name}")
	// public RuleConfig selectRuleConfig(Map paramMap );
	//
	// @Select("SELECT * FROM SQLConfig")
	// public List<RuleConfig> selectList();
	//
	// @Insert("insert into SQLConfig values(#{id} ,#{name} )")
	// public int insertConfig(RuleConfig config);

	// @SelectProvider(type=)
	public RuleConfig selectObject(RuleConfig config);
}
