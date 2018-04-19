package com.dcits.cloud.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED)
public class DatabaseDAO {

	@Autowired
	private AipJdbcDAO aipJdbcDAO;
	
	private static Map<String,String> aip_code_map = new HashMap<String,String>();
	
	private static final String db_name = "dbcd47d5-e264-4e42-b4d5-cec8ae64561e";
	static{
		
		aip_code_map.put("5", "IP");
		aip_code_map.put("6", "PORT");
		aip_code_map.put("7", "DATABASE_TYPE");
		aip_code_map.put("8", "USERNAME");
		aip_code_map.put("9", "PASSWORD");
		aip_code_map.put(db_name, "DATABASE_NAME");
		aip_code_map.put("1-12", "owner");
		aip_code_map.put("e0bbd76d-83fb-41b5-91eb-46410f32ed16", "classify");
	}
	
	public List<Map<String,Object>> getAll() throws InstantiationException, IllegalAccessException{
		String sql = "select local_data.*,d.dict_value from (select a.info_code,a.info_name,b.value_attr_id,b.value_text,c.attr_name,c.attr_code from (select m.* from md_info m,mdm_model mdm  where m.info_model_id=mdm.model_id and mdm.model_code='Schema' and m.info_status=1) a,md_attr_value b,(select ma.* from mdm_attr ma,mdm_model mdm where ma.attr_model_id=mdm.model_id and  mdm.model_code='Schema') c where a.info_id=b.value_info_id and c.attr_id = b.value_attr_id) local_data left join fw_dict_info d on d.dict_id=local_data.value_text";
		
		List<Map<String,Object>> list = aipJdbcDAO.getTemplate().queryForList(sql, new HashMap<String,Object>());
		
		List<Map<String,Object>> tableList = new ArrayList<Map<String,Object>>();
		for(Map<String,Object> subMap : list){
			Map<String,Object> map = null;
			
			//如果表数据list为空，则加入一个map
			if(tableList.size() == 0){
				map = new HashMap<String, Object>();
				map.put("INFO_CODE", subMap.get("INFO_CODE"));
				map.put("INFO_NAME", subMap.get("INFO_NAME"));
				tableList.add(map);
			}
			else{
				boolean flag = false;
				for(int i = 0;i<tableList.size();i++){
					Map<String,Object> tableMap = tableList.get(i);
					
					if(subMap.get("INFO_CODE").toString().equals(tableMap.get("INFO_CODE"))){
						map = tableMap;
						break;
					}
					
					if(i == tableList.size() - 1){
						flag = true;
					}
				}
				
				if(flag){
					map = new HashMap<String, Object>();
					map.put("INFO_CODE", subMap.get("INFO_CODE"));
					map.put("INFO_NAME", subMap.get("INFO_NAME"));
					tableList.add(map);
				}
			}
			
			
			if ("dbtype".equals(subMap.get("ATTR_CODE"))) {
				map.put(subMap.get("ATTR_CODE").toString(), subMap.get("DICT_VALUE"));
			} else {
				map.put(subMap.get("ATTR_CODE").toString(), subMap.get("DICT_TEXT"));
			}
			if(map.containsKey(null)){
				map.remove(null);
			}
			
		}
		return tableList;
	}
}
