package com.dcits.cloud.controller;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dcits.cloud.model.ResultStatus;
import com.dcits.cloud.service.CacheService;
import com.dcits.cloud.service.RuleConfigService;
import com.dcits.cloud.service.SqlModelService;

@Controller
public class ConfigController {

	@Autowired
	private RuleConfigService ruleConfigService;

	@Autowired
	private CacheService cacheService;

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private SqlModelService sqlModelService;
	
	
	@RequestMapping("fileUpload")
	public String fileUpload(HttpServletRequest request) throws IOException {

		if (request != null) {
			Iterator iterator = ((MultipartHttpServletRequest) request)
					.getFileNames();

			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = multipartRequest.getFile("file");
			String filename = file.getOriginalFilename();
			File dir = new File("G:/home");
			if (!dir.exists()) {
				dir.mkdir();
			}
			InputStream inputStream = file.getInputStream();

			FileOutputStream outputStream = new FileOutputStream("G:/home/"
					+ filename);
			int n = 0;// 每次读取的字节长度
			byte[] bb = new byte[1024];// 存储每次读取的内容
			while ((n = inputStream.read(bb)) != -1) {
				outputStream.write(bb, 0, n);// 将读取的内容，写入到输出流当中
			}
			inputStream.close();
			outputStream.close();

		}

		// 重定向
		return "up";
	}

	@RequestMapping("/pageAction/list")
	@ResponseBody
	public Object list(String searchCondition, Long startPage) {
		return ruleConfigService.listConfigs(searchCondition, startPage);
	}

	/**
	 * 页面发布，参数传入
	 * 
	 * @param data
	 * @return
	 * @author 陈明 2016年11月7日
	 */
	@RequestMapping("/pageAction/publish")
	@ResponseBody
	public Object publishService(@RequestParam("data") String data)
			throws Exception {
//		return ruleConfigService.save(data);
		return null;
	}

	/**
	 * 只挂接服务
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 * @author 陈明 2016年12月10日
	 */
	@RequestMapping("/pageAction/registerOnly")
	@ResponseBody
	public Object registerOnly(@RequestParam("id") String id) throws Exception {
		return ruleConfigService.registerOnly(id);
	}

	/**
	 * 删除ssp总线上对应的服务
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 * @author 陈明 2016年12月10日
	 */
	@RequestMapping("/pageAction/deleteSSP")
	@ResponseBody
	public Object deleteSSP(@RequestParam("id") String id) throws Exception {
//		return ruleConfigService.deleteSSP(id);
		return null;
	}

	/**
	 * 初始化页面
	 * 
	 * @return
	 * @author 陈明 2016年11月7日
	 */
	@RequestMapping("/pageAction/initpage")
	@ResponseBody
	public Object initPage() {
		return ruleConfigService.initPage();
	}

	/**
	 * 初始化页面
	 * 
	 * @return
	 * @author 陈明 2016年11月7日
	 */
	@RequestMapping("/pageAction/removeConfig")
	@ResponseBody
	public Object removeConfig(@RequestParam("id") String id) {
//		return ruleConfigService.remove(id);
		return null;
	}

	@RequestMapping("/pageAction/registService")
	@ResponseBody
	public Object registService(@RequestParam("data") String data)
			throws Exception {

//		return ruleConfigService.register(data);
		return null;
	}

	/**
	 * 页面转向
	 * 
	 * @param requestMapping
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/pageAction/edit")
	public Object service(HttpServletRequest request, String id)
			throws Exception {
		return ruleConfigService.getById(id);
	}

	@RequestMapping("/pageAction/testSQL")
	@ResponseBody
	public ResultStatus testSQL(@RequestParam("sql") String sql,
			@RequestParam("rowData") String rowData,
			@RequestParam("database") String database,@RequestParam("type") String type) throws InstantiationException, IllegalAccessException, NumberFormatException, ClassNotFoundException, SQLException, PropertyVetoException {

		String errorStr = ruleConfigService.checkSQL(sql, rowData, database,Integer.valueOf(type));
		if (StringUtils.isEmpty(errorStr)) {
			return new ResultStatus();
		}

		return new ResultStatus(errorStr);
	}
	
	
	/**
	 * 保存sql模板
	 * @return
	 * @author 陈明
	 * 2017年1月4日
	 */
	@RequestMapping("/pageAction/saveModel")
	@ResponseBody 
	public ResultStatus saveSQLModel(@RequestParam("config") String config){
		
		return sqlModelService.saveSqlModel(config);
	}
	
	/**
	 * 保存sql模板
	 * @return
	 * @author 陈明
	 * 2017年1月4日
	 */
	@RequestMapping("/pageAction/getModel")
	@ResponseBody 
	public ResultStatus getSQLModel(){
		
		return sqlModelService.getSqlModel();
	}

	@RequestMapping("/")
	public String service(HttpServletRequest request) throws Exception {
		return "page/createService";
	}
}
