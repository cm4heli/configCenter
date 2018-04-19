package com.dcits.cloud.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.hadoop.hbase.util.Bytes;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.dcits.cloud.common.Constants;
import com.dcits.cloud.model.ServiceInfo;
import com.dcits.cloud.model.ServiceParamInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Utils {

	/**
	 * 获取文件所有内容，返回字符串类型
	 * 
	 * @param filePath
	 *            文件路径
	 * @return
	 * @throws IOException
	 * @author 陈明 2016年11月4日
	 */
	public static String getFileInf(String filePath) throws IOException {
		FileReader fr = new FileReader(filePath);
		BufferedReader br = new BufferedReader(fr);
		StringBuffer strb = new StringBuffer();
		String str;
		// 逐行读取
		while ((str = br.readLine()) != null) {
			strb.append(str);
		}

		return strb.toString();
	}

	/**
	 * 生成数据库唯一键ID
	 * 
	 * @return
	 */
	public static String generateKey() {
		return UUID.randomUUID().toString();
	}

	/**
	 * JSON 转为map格式
	 * 
	 * @param param
	 * @return
	 * @author 陈明 2016年11月7日
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> jsonToMap(String param) {
		Map<String, Object> paramMap = new HashMap<String, Object>();

		try {

			paramMap = (Map<String, Object>) JSONObject.fromObject(param);
		} catch (Exception e) {
			// TODO
		}
		return paramMap;
	}

	/**
	 * json转list
	 * 
	 * @param param
	 * @return
	 * @author 陈明 2016年11月7日
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> jsonToList(String param) {
		List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
		try {
			paramList = (List<Map<String, Object>>) JSONArray.fromObject(param);
		} catch (Exception e) {
			// TODO
		}

		return paramList;
	}

	/**
	 * json字符串转化为bean
	 * 
	 * @param jsonStr
	 * @param T
	 * @return
	 * @author 陈明 2016年11月7日
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	@SuppressWarnings("unused")
	public static <T> T jsonToBean(Map<String, Object> beanMap, Class<T> beanclass)
			throws InstantiationException, IllegalAccessException {
		T javabean = beanclass.newInstance();
		Method[] methods = beanclass.getDeclaredMethods();
		for (Method method : methods) {
			try {
				if (method.getName().startsWith("set")) {
					Class<?>[] type = method.getParameterTypes();

					String field = method.getName();
					field = field.substring(field.indexOf("set") + 3);
					field = field.toLowerCase().charAt(0) + field.substring(1);
					if (type[0].getName().equals("java.lang.Integer")) {
						method.invoke(javabean, Integer.valueOf(beanMap.get(field).toString()));
					}
					method.invoke(javabean, new Object[] { beanMap.get(field) });
				}
			} catch (Exception e) {
			}
		}

		return javabean;
	}

	/**
	 * 利用发射获得对应的bean对象
	 * 
	 * @param list
	 * @param beanclass
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @author 陈明 2017年1月10日
	 */
	public static <T> List<T> listToBean(List<Map<String, Object>> list, Class<T> beanclass)
			throws InstantiationException, IllegalAccessException {

		List<T> returnList = new ArrayList<T>();
		for (Map<String, Object> eachBean : list) {
			returnList.add(Utils.jsonToBean(eachBean, beanclass));
		}
		return returnList;
	}

	public static String sendPOST(String url, Map<String, Object> data) {
		HttpResponse httpResponse = null;

		String responseStr = "";
		HttpClient client = new DefaultHttpClient();

		HttpPost post = new HttpPost(url);

		List<NameValuePair> namelist = new ArrayList<NameValuePair>();

		for (Entry<String, Object> entry : data.entrySet()) {
			Object value = entry.getValue();
			if (null == value) {
				namelist.add(new BasicNameValuePair(entry.getKey(), ""));
			} else {
				namelist.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
			}
		}
		try {
			if (null != data) {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(namelist, "UTF-8");
				post.setEntity(entity);
			}
			// 执行post请求
			httpResponse = client.execute(post);
			HttpEntity entity = httpResponse.getEntity();
			byte[] bytes = EntityUtils.toByteArray(entity);
			responseStr = Bytes.toString(bytes);
		} catch (IOException e) {
			Map<String, Object> errorMap = new HashMap<String, Object>();
			errorMap.put("success", false);
			errorMap.put("msg", e.getMessage());
			return errorMap.toString();
		} finally {
			post.releaseConnection();
		}
		return responseStr;
	}

	/**
	 * 获取文件数据
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 * @author 陈明 2017年2月20日
	 */
	public static List<String> readTxt(String path) throws IOException {
		List<String> fileContent = new ArrayList<String>();
		try {
			File f = new File(path);
			if (f.isFile() && f.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(f), "UTF-8");
				BufferedReader reader = new BufferedReader(read);
				String line;
				while ((line = reader.readLine()) != null) {
					fileContent.add(line);
				}
				read.close();
			}
		} catch (Exception e) {
			System.out.println("读取文件内容操作出错");
			e.printStackTrace();
		}
		return fileContent;
	}
	
	public static  long getStartRowNo(long startIndex,
			long pageSize){
		if(startIndex==1 || startIndex == 0){
			return 0;
		}else{
			return (startIndex-1)*pageSize;
		}
	}
	public static long getEndRowNo(long startIndex,
			long pageSize){
		return getStartRowNo(startIndex, pageSize)+pageSize + 1;
	}

	
	/**
	 * 工具方法，将oracle全部是大写的数据转换为对应的数据
	 * @param serviceInfo
	 * @param dataList
	 * @return
	 */
	public static List<Map<String, Object>> parseData(ServiceInfo serviceInfo, List<Map<String, Object>> dataList) {
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		
		List<Integer> numList = new ArrayList<Integer>();
		Map<Integer,ServiceParamInfo> paramMap = new HashMap<Integer,ServiceParamInfo>();
		for(ServiceParamInfo param : serviceInfo.getParams()){
			if (param.getParameter_type() == Constants.RESPONSE_TYPE) {
				paramMap.put(param.getParameter_order(), param);
				numList.add(param.getParameter_order());
			}
		}
		Collections.sort(numList);
		List<ServiceParamInfo> orderParam = new ArrayList<ServiceParamInfo>();
		
		for(Integer orderNum : numList){
			orderParam.add(paramMap.get(orderNum));
		}
		
		for(Map<String,Object> dataMap : dataList){
			Map<String,Object> newMap = new LinkedHashMap<String,Object>();
			for (ServiceParamInfo param : orderParam) {
				if (param.getParameter_type() == Constants.RESPONSE_TYPE) {
			    	if(dataMap.containsKey(param.getParameter_name().toUpperCase())){
			    		newMap.put(param.getParameter_name(), dataMap.get(param.getParameter_name().toUpperCase()));
			    	}
			    }
			}
			returnList.add(newMap);
		}
		return returnList;
	}
	
	
	/**
	 * 获取当前时间
	 * @return
	 */
	public static String getCurrentDate(){
		SimpleDateFormat simFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return simFormat.format(new Date(System.currentTimeMillis()));
	}
	
}
