package com.dcits.cloud.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dcits.cloud.dao.hbase.HbaseDAO;

@Component
@EnableScheduling
@PropertySource("classpath:jdbc.properties")
public class FileScanService {

	@Autowired
	Environment env;
	
	private static List<String> imageFomat = new ArrayList<String>();
	
	static{
		imageFomat.add("png");
		imageFomat.add("gif");
		imageFomat.add("jpg");
		imageFomat.add("bmp");
	}
	@Autowired
	HbaseDAO hbaseDAO;
	
    @Scheduled(fixedRate = 1000*60*60)
    public void scanFile() throws IOException{
//    	this.fileOper("");
    }
    
    public void fileOper(String path) throws IOException{
    	if(null == path || "".equals(path)){
			path = env.getProperty("temp_dir");
		}
		File file = new File(path);
		File[] files = file.listFiles();
		if(files == null || files.length == 0){
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
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
}
