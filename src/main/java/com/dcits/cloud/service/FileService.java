package com.dcits.cloud.service;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dcits.cloud.common.Constants;
import com.dcits.cloud.dao.hbase.HbaseDAO;
import com.dcits.cloud.model.ResultStatus;
import com.dcits.cloud.utils.Utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@Service("fileService")
public class FileService {

	@Autowired
	Environment env;
	
	private static List<String> imageFomat = new ArrayList<String>();
	
	static{
		imageFomat.add("png");
		imageFomat.add("gif");
		imageFomat.add("jpg");
		imageFomat.add("bmp");
	}
	
	static BASE64Encoder encoder = new sun.misc.BASE64Encoder();  
    static BASE64Decoder decoder = new sun.misc.BASE64Decoder();  
    
	@Autowired
	private HbaseDAO hbaseDAO;
	
	public Object saveFile(HttpServletRequest request, String rowkey, MultipartFile file) throws IOException{
		
		hbaseDAO.saveData("IMAGE_TABLE", rowkey, "cf", "c", file.getBytes());
		return null;
	}
	
	
	public Object saveF(String rowkey, String  filepath) throws IOException{
		File f = new File(filepath);  
        BufferedImage bi;  
        byte[] bytes = null;
        try {  
            bi = ImageIO.read(f);  
            ByteArrayOutputStream baos = new ByteArrayOutputStream();  
            ImageIO.write(bi, "jpg", baos);  //经测试转换的图片是格式这里就什么格式，否则会失真  
            bytes = baos.toByteArray();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        
		hbaseDAO.saveData("IMAGE_TABLE", rowkey, "cf", "c", bytes);
		return null;
	}
	
	
	public Object getFile(String rowkey) throws IOException{
		byte[] imgByte =  hbaseDAO.getRowByRowkeyCF("IMAGE_TABLE", rowkey, "cf", "c");
		  
		  
        ByteArrayInputStream bais = new ByteArrayInputStream(imgByte);  
        BufferedImage bi1 = ImageIO.read(bais);  
        File w2 = new File("G://QQ.jpg");// 可以是jpg,png,gif格式  
        ImageIO.write(bi1, "jpg", w2);
		return null;
	}
	
	
	public void fileOper(String path) throws IOException{
    	if(null == path || "".equals(path)){
			path = env.getProperty("temp_dir");
		}
		File file = new File(path);
		File[] files = file.listFiles();
		if(files.length == 0){
			return;
		}
		
		
		for(File eachFile : files){
			
		     if(eachFile.isDirectory()){
		    	 fileOper(eachFile.getAbsolutePath());
		     }
		     
			 String fileName = eachFile.getName();
			 
			 /*if(!imageFomat.contains(fileSplit[1]) ){
				 continue;
			 }*/
			 
			 long len = eachFile.length();
			 byte[] bytes = new byte[(int)len];

			 BufferedInputStream bufferedInputStream = null;
			try {
				bufferedInputStream = new BufferedInputStream(new FileInputStream(eachFile));
				int r = bufferedInputStream.read(bytes);
				 bufferedInputStream.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally{
				if( null != bufferedInputStream){
					bufferedInputStream.close();
				}
			}
			 
			 
			 //存二进制图片值
			 hbaseDAO.saveData("IMAGE_TABLE", fileName, "cf", "file", bytes);
		}
		
		for(File eachFile : files){
			eachFile.delete();
		}
    }
	
	
	/**
	 * 获取sql说明文件的内容
	 * @return
	 * @author 陈明
	 * 2017年2月20日
	 * @throws IOException 
	 */
//	public Object getTextFile(String type) throws IOException{
//		
//		String path = Constants.SQL_ORACLE_URL;
//		if("1".equals(type)){
//			path = Constants.SQL_ELASTIC_URL;
//		}
//		
//		List<String> returnStr = Utils.readTxt(path);
//		
//		ResultStatus result = new ResultStatus();
//		result.setData(returnStr);
//		return result;
//	}
//	
}
