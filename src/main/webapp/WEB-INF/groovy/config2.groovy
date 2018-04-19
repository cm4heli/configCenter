//固定写法引入Map
import java.util.HashMap
//定义一个API配置库用来存放配置信息
def anjjzjParamMaping2=[init:'0']
beans {
        //anjjzj为服务访问名称,如http://localhost:9999/cloud/test
        test  HashMap,anjjzjParamMaping2 
        //请求参数名称和类型
        anjjzjParamMaping2['xm'] = 'java.lang.String' 
        anjjzjParamMaping2['xb'] = 'java.lang.String' 
        //固定写法，编写api sql查询语句
        anjjzjParamMaping2['sql']= 'SELECT name  FROM new_test where name=\':xm\'   order by id   limit 5 ' 
        userInfo(com.dcits.cloud.model.UserInfo,loginName:'test') 
}

