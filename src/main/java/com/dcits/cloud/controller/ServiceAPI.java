package com.dcits.cloud.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dcits.cloud.service.APIService;
import com.dcits.cloud.service.RuleConfigService;

import net.sf.json.JSONObject;

@Controller
public class ServiceAPI {

	@Autowired
	private APIService apiService;

	@Autowired
	private RuleConfigService ruleConfigService;

	/**
	 * 服务访问
	 * 
	 * @param requestMapping
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/api/**/{requestMapping}",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object service(@PathVariable String requestMapping, HttpServletRequest request) throws Exception {
		// request.setCharacterEncoding("UTF-8");
		String callback = request.getParameter("jsonpcallback");
		String url = request.getServletPath();
		url =url.replaceFirst("/api/", "");
		// 获取请求参数
		Map<String, String[]> paramMap = request.getParameterMap();
		Object obj = apiService.search(url, paramMap,1);
		if(callback == null || "".equals(callback)){
			return obj;
		}
		else{
			JSONObject json = JSONObject.fromObject(obj);
			return callback + "(" + json.toString() + ")";
		}
	}

}