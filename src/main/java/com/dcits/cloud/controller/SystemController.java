package com.dcits.cloud.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dcits.cloud.service.SystemService;

@Controller
public class SystemController {

	@Autowired
	private SystemService systemService;
	
	/**
	 * 获取服务
	 * @param id
	 * @return
	 * @author 陈明
	 * 2017年1月11日
	 * @throws Exception 
	 */
	@RequestMapping("/system/update")
	@ResponseBody
	public Object updateSystemData(HttpServletRequest request) throws Exception{
		
		return systemService.getSystemData();
	}
	
	
	/**
	 * 获取管理员电话
	 * @param id
	 * @return
	 * @author 陈明
	 * 2017年1月11日
	 * @throws Exception 
	 */
	@RequestMapping("/system/getPhone")
	@ResponseBody
	public Object getSystemAdminPhone(HttpServletRequest request) throws Exception{
		
		
		return systemService.getSystemData();
	}
	
	/**
	 * 获取服务
	 * @param id
	 * @return
	 * @author 陈明
	 * 2017年1月11日
	 * @throws Exception 
	 */
	@RequestMapping("/system/getData")
	@ResponseBody
	public Object getSystemData(HttpServletRequest request) throws Exception{
		
		
		return systemService.getData();
	}
	
//	/**
//	 * 获取服务
//	 * @param id
//	 * @return
//	 * @author 陈明
//	 * 2017年1月11日
//	 * @throws Exception 
//	 */
//	@RequestMapping("/system/setData")
//	@ResponseBody
//	public Object setSystemData(HttpServletRequest request) throws Exception{
//		
//		Map<String, String[]> paramMap = request.getParameterMap();
//		
//		String ip = paramMap.get("ip")[0];
//		String port = paramMap.get("port")[0];
//		
//		return systemService.initSystemData(ip, Integer.valueOf(port));
//	}
	
	
	@RequestMapping("/system/setDataBase")
	@ResponseBody
	
	public Object getDataBaseData(HttpServletRequest request) throws Exception{
		
		
		return systemService.getDabaseData();
	}
	
	
	
	
}
