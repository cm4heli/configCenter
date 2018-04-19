 
//定义一个API配置库用来存放配置信息
def anjjzjParamMaping=[init:'0']
beans {
        //anjjzj为服务访问名称,如http://localhost:9999/cloud/anjjzj
        anjjzj  java.util.HashMap,anjjzjParamMaping 
        //请求参数名称和类型
        anjjzjParamMaping['xm'] = 'java.lang.String' 
        anjjzjParamMaping['xb'] = 'java.lang.String' 
        //固定写法，编写api sql查询语句
        anjjzjParamMaping['sql']= 'SELECT BATCH_NO as bathNo, YHZGX as yhzgx FROM RK_GONGAJ_CZRK where contains(BATCH_NO , \"regexp \'.*:xm.*\'\") and contains(yhzgx , \"regexp \'.*:xb.*\'\")   limit 5 ' 
        
}