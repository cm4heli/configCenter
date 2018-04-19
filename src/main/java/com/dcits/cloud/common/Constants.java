package com.dcits.cloud.common;

/**
 * 常量类
 * 
 * @author 陈明 2016年11月8日
 */
public class Constants {

	/**
	 * 创建服务请求类型常量
	 */
	// 请求参数类型
	public static final int REQUEST_TYPE = 0;

	// 响应参数类型
	public static final int RESPONSE_TYPE = 1;

	/**
	 * 查询模式类型
	 */
	public static Integer search_type_hbase = 1;

	public static Integer search_type_inceptor = 2;
	
	public static Integer search_type_es = 3;
	
	public static Integer search_type_oracle = 4;
	
	/**操作结果状态**/
	public static String  op0000 = "0000" ;//操作成功
	public static String  op0001 = "0001" ;//服务地址已经存在，请重新编辑
	
	/**
	 * aip服务地址
	 */
	public static String PROXY_SERVICE_IP;
	
	public static String PROXY_SERVICE_PORT;
	
	public static String AIP_SERVICE_URL;
	
	public static Integer initialSize;
	public static Integer minIdle;
	public static Integer maxActive;
	public static Long setMaxWait;
	public static Long timeBetweenEvictionRunsMillis;
	public static Long minEvictableIdleTimeMillis;
	public static String validationQuery;
	public static Boolean testWhileIdle;
	public static Boolean testOnBorrow;
	public static Boolean testOnReturn;
	public static Boolean poolPreparedStatements;
	
	/**
	 * 执行代码
	 */
	//成功
	public static Integer EXEC_SUCCESS = 0;
	
	//失败
	public static Integer EXEC_FAILURE = -1;
	/**
	 * 是否需要服务代理模式，0表示不需要，1表示需要
	 */
	public static Integer SSPSERVER_MODE;
	/**
	 * 是否注册
	 */
	//已经注册挂接
	public static final Integer REGISTER = 1;
	
	//未挂接
	public static final Integer NOT_REGISTER = 0;
	
	
	/**
	 * 服务使用的类型
	 */
	
	
	public static final Integer COMMON_API = 1;
	
	public static final Integer AIPV3_API = 2;
	
	public static final Integer INNER_API = 3;
	
	
	/**
	 * 服务是否显示总页数
	 */
	
	//显示总页数
	public static final Integer SHOW_TOTAL_PAGE = 1;
	
	//不显示总页数
	public static final Integer NOT_SHOW_TOTAL_PAGE = 0;
}
