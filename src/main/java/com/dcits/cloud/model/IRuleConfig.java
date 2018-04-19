package com.dcits.cloud.model;

import java.util.List;
import java.util.Map;

public interface IRuleConfig {

	public void addRuleRepo(RuleRepo ruleRepo);

	public void addRuleParams(List<RuleParam> ruleParams);

	public void init(Map<String, Object> initData);

}
