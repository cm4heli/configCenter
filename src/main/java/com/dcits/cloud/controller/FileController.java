package com.dcits.cloud.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.jamon.annotations.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dcits.cloud.model.ResultStatus;
import com.dcits.cloud.service.FileScanService;
import com.dcits.cloud.service.FileService;

@Controller
public class FileController {

	@Autowired
	private FileService fileService;
	/**
	 * 保存文件
	 * @param request
	 * @return
	 * @author 陈明
	 * 2016年12月15日
	 * @throws IOException 
	 */
	@RequestMapping("/file/save")
	@ResponseBody
	public Object saveFile(HttpServletRequest request,@RequestParam("rowkey") String rowkey,@RequestParam("fileup") MultipartFile file) throws IOException{
		
        return fileService.saveFile(request,rowkey,file);//直接返回string 也可以  
    }
	
	
	@RequestMapping("/file/get")
	@ResponseBody
	public Object getFile(@RequestParam("rowkey") String rowkey) throws IOException{
		
		return fileService.getFile(rowkey);
	}
	
	
	@RequestMapping("/file/test")
	@ResponseBody
	public Object getFile() throws IOException{
		
		fileService.saveF("123", "E:\\test.jpg");
		
		fileService.getFile("123");
		return null;
	}
	
	@RequestMapping("/file/upload")
	@ResponseBody
	public Object  imgControll(String path) throws IOException{
		
		fileService.fileOper(path);
		return new ResultStatus();
	}
	
	
	@RequestMapping("/file/getFile")
	@ResponseBody
	public Object  getfile(@RequestParam("data") String type) throws IOException{
		

//		return fileService.getTextFile(type);
		return null;
	}
	
	
	
}  
