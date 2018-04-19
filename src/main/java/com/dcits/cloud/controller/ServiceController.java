package com.dcits.cloud.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dcits.cloud.common.Constants;
import com.dcits.cloud.model.PageList;
import com.dcits.cloud.model.ServiceInfo;
import com.dcits.cloud.service.APIService;
import com.dcits.cloud.service.RouteService;
import com.dcits.cloud.service.SystemService;

@Controller
public class ServiceController {

	@Autowired
	private RouteService routeService;
	@Autowired
	private APIService apiService;
	@Autowired
	private SystemService sysService;
	/**
	 * 获取单页数据
	 * @param start
	 * @param pageSize
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @author 陈明
	 * 2017年1月10日
	 */
	@RequestMapping("/service/getPageService")
	@ResponseBody
	public PageList<ServiceInfo> getPageService(String searchCondition, Long pageIndex,Long pageSize,String type,Integer publish,String owner,String user) throws InstantiationException, IllegalAccessException{
		
		return routeService.getPageService(searchCondition,pageIndex,pageSize,type,publish,owner,user);
	}
	
	@RequestMapping("/service/getServiceById")
	@ResponseBody
	public Object getServiceById(@RequestParam("id") String id) throws InstantiationException, IllegalAccessException{
		return routeService.getServiceById(id);
	}
	
	
	/**
	 * 获取所有的数据源
	 * @return
	 * @author 陈明
	 * 2017年1月10日
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@RequestMapping("/service/getDataSource")
	@ResponseBody
	public Object getAllDataSource() throws InstantiationException, IllegalAccessException{
		return routeService.getAllDataSource();
	}
	
	
	/**
	 * 添加服务
	 * @param data
	 * @return
	 * @author 陈明
	 * 2017年1月11日
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@RequestMapping("/service/addService")
	@ResponseBody
	public Object addSingleService(@RequestParam("data") String data) throws InstantiationException, IllegalAccessException{
		
		return routeService.addService(data);
	}
	
	
	/**
	 * 页面转向
	 * 
	 * @param requestMapping
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/service/edit")
	public Object service(HttpServletRequest request, String id)
			throws Exception {
		return routeService.getServiceById(id);
	}

	
	
	/**
	 * 删除服务
	 * @param id
	 * @return
	 * @author 陈明
	 * 2017年1月11日
	 */
	@RequestMapping("/service/deleteService")
	@ResponseBody
	public Object deleteService(@RequestParam("id") String id){
		
		return routeService.deleteService(id);
	}
	
	
	/**
	 * 获取服务
	 * @param id
	 * @return
	 * @author 陈明
	 * 2017年1月11日
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@RequestMapping("/service/getServiceByUrl")
	@ResponseBody
	public Object getServiceByUrl(@RequestParam("url") String url) throws InstantiationException, IllegalAccessException{
		
		return routeService.getServiceByUrl(url);
	}
	
	
	/**
	 * 获取服务
	 * @param id
	 * @return
	 * @author 陈明
	 * 2017年1月11日
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@RequestMapping("/service/unpublishService")
	@ResponseBody
		public Object unpublishService(@RequestParam("id") String id,@RequestParam("mode") String mode) throws InstantiationException, IllegalAccessException{
		
		return routeService.unpublishService(id,Integer.valueOf(mode));
	}
	
	
	/**
	 * 获取服务
	 * @param id
	 * @return
	 * @author 陈明
	 * 2017年1月11日
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@RequestMapping("/service/publishService")
	@ResponseBody
	public Object publishService(@RequestParam("id") String id,@RequestParam("topic") String topic,@RequestParam("field") String field) throws InstantiationException, IllegalAccessException{
		
		return routeService.publishService(id,topic,field);
	}
	
	
	/**
	 * 获取服务
	 * @param id
	 * @return
	 * @author 陈明
	 * 2017年1月11日
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@RequestMapping("/service/getServiceTopic")
	@ResponseBody
	public Object getServiceTopic(@RequestParam("id") String id) throws InstantiationException, IllegalAccessException{
		
		return routeService.getServiceTopic(id);
	}
	
	
	
	
	
	/**
	 * 获取API类型
	 * @param id
	 * @return
	 * @author 陈明
	 * 2017年1月11日
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@RequestMapping("/service/getAPIType")
	@ResponseBody
	public Object getAPIType() throws InstantiationException, IllegalAccessException{
		
		return routeService.getAPIType();
	}
	
	
	/**
	 * 获取数据库字段类型
	 * @param id
	 * @return
	 * @author 陈明
	 * 2017年1月11日
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@RequestMapping("/service/getDBType")
	@ResponseBody
	public Object getDBType() throws InstantiationException, IllegalAccessException{
		
		return routeService.getDBType();
	}
	
	/**
	 * 获取服务API的应用主题和业务领域
	 * @param id
	 * @return
	 * @author 陈明
	 * 2017年1月11日
	 * @throws Exception 
	 */
	@RequestMapping("/service/serviceAttr")
	@ResponseBody
	public Object getServiceAttr() throws Exception{
		
		return routeService.getServiceAttr();
	}
	
	
	/**
	 * 根据操作员ID获取
	 * @param id
	 * @return
	 * @author 陈明
	 * 2017年1月11日
	 * @throws Exception 
	 */
	@RequestMapping("/service/getPageUser")
	@ResponseBody
	public Object getPageUser(Long startPage,String condition) throws Exception{
		
		return routeService.getAllUser(startPage,condition);
	}
	
	
	/**
	 * 改变服务创建人
	 * @param id
	 * @return
	 * @author 陈明
	 * 2017年1月11日
	 * @throws Exception 
	 */
	@RequestMapping("/service/modifyUser")
	@ResponseBody
	public Object modifyUser(String resid,String newUser) throws Exception{
		
		return routeService.modifyUser(resid,newUser);
	}
	
	
	/**
	 * 根据用户名获取组用户
	 * @param id
	 * @return
	 * @author 陈明
	 * 2017年1月11日
	 * @throws Exception 
	 */
	@RequestMapping("/service/getGroupUser")
	@ResponseBody
	public Object getGroupUser(@RequestParam("userName") String userName) throws Exception{
		
		return routeService.getGroupUser(userName);
	}
	
	
	/**
	 * 获取所有创建了服务的用户
	 * @param id
	 * @return
	 * @author 陈明
	 * 2017年1月11日
	 * @throws Exception 
	 */
	@RequestMapping("/service/getAllCreator")
	@ResponseBody
	public Object getAllCreator(@RequestParam("user") String userName) throws Exception{
		
		return routeService.getAllCreator(userName);
	}
	
	/**
	 * 获取服务
	 * @param id
	 * @return
	 * @author 陈明
	 * 2017年1月11日
	 * @throws Exception 
	 */
	@RequestMapping("/service/serTest")
	@ResponseBody
	public Object getServiceByUrltest(HttpServletRequest request) throws Exception{
		

		Map<String,String[]> requestMap = request.getParameterMap();
		String url = requestMap.get("url")[0];
		String api_url = "";
		if(url.contains(Constants.PROXY_SERVICE_PORT) && url.contains(Constants.PROXY_SERVICE_IP)){
			api_url = "http://" + Constants.PROXY_SERVICE_IP + ":" + Constants.PROXY_SERVICE_PORT + "/service";
		}
		else{
			api_url = Constants.AIP_SERVICE_URL;
		}
		url = url.replace(api_url + "/", "");
		return apiService.search(url, requestMap,0);
	}
	
	
	
	
	
	
}

