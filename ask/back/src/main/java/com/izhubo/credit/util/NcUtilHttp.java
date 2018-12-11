package com.izhubo.credit.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.reflect.TypeToken;
import com.izhubo.credit.vo.HttpServiceResult; 
import com.izhubo.credit.vo.RegistrationSYNCVO;

/**
 * NC不常用的从NCWS接口的取数</p>
 * 说明：从HTTP取的NC中的数据 一些不常用的数据 不频繁的取数的
 * @author lintf
 *
 */
public class NcUtilHttp {

	/**
	 * 根据pk_org 取得大区名 省份 城市 校区</p>
	 * 例子：取得大区名：NcorgMain.get(orgid).get("dqname");</p>
	 * 全部的 Map.key 如下:</p>
	 * 名称:    dqname  
	 * csname 
	* sfname  
	* xqname  </p>
	* pk_org:    dqorgid 
	* csorgid 
	* sforgid  
	* xqorgid  </p>
	* code:     dqcode 
	* cscode 
	* sfcode  
	* xqcode  
	 * 
	 * @return
	 * @throws Exception 
	 */
	public static Map<String,Map<String,String>> NcorgMain() throws Exception   {
		Map<String,Map<String,String>>  root= new HashMap<String,Map<String,String>> ();
		
		Map<String,String> sub=new HashMap<String,String>();
		
		sub.put("dqname","dqname");
		sub.put("csname","csname");
		sub.put("sfname","sfname");
		sub.put("xqname","xqname");
		sub.put("dqorgid","dqorgid");
		sub.put("csorgid","csorgid");
		sub.put("sforgid","sforgid");
		sub.put("xqorgid","xqorgid");
		sub.put("dqcode","dqcode");
		sub.put("cscode","cscode");
		sub.put("sfcode","sfcode");
		sub.put("xqcode","xqcode");
		String secretkey= NcSyncConstant.getNcSecretkey();
		 String resultJson;
		//try {
			resultJson = HttpRequest.sendPost( NcSyncConstant.getNcOrgMainUrl(), "secretKey="+secretkey,null);
		
			HttpServiceResult s = JsonUtil.fromJson(resultJson, new TypeToken<HttpServiceResult>() {}.getType());
			 
			List<Map<String,String>> voList = JsonUtil.fromJson(JsonUtil.toJson(s.getData()), new TypeToken<List<Map<String,String>>>() {}.getType());
		    if (voList!=null&&voList.size()>0){
		    for (Map<String,String> map:voList){
		    	String xqorgid= map.get("xqorgid");
		    	if (root.get(xqorgid)==null){
		    		root.put(xqorgid, map);
		    	}
		    }
		    }
		
		
		
		//} catch (Exception e) {
			// TODO Auto-generated catch block
	///		e.printStackTrace();
		//}
	 
		
		
		return root;
	}
	/**
	 * 从接口处取得老师名称 
	 * * 例子：取得大区名：TeacherNameMain.get(key).get("Map.key");</p>
	 * 全部的 Map.key 如下:</p>
	 * 	
	 * 	  name :老师名称
	 * 	 code :老师编号
	 * 	 psndoc_id :人员PK
	 * 	 psndoc_org :人员校区
	 * 	 teacher_id ：老师档案Pk
	 * 	 teacher_org :老师档案所在校区
	 * @param type 1就是只取有老师档案的老师 最终的map就是以老师档案的pk为 key,其情况下以psndoc_id为key
	 * @return
	 * @throws Exception
	 */
	public static Map<String,Map<String,String>> TeacherNameMain(String  type) throws Exception   {
		Map<String,Map<String,String>>  root= new HashMap<String,Map<String,String>> ();
		
		Map<String,String> sub=new HashMap<String,String>();
		
		sub.put("name","name");
		sub.put("code","code");
		sub.put("psndoc_id","psndoc_id");
		sub.put("psndoc_org","psndoc_org");
		sub.put("teacher_id","teacher_id");
		sub.put("teacher_org","teacher_org");
		 
		String secretkey= NcSyncConstant.getNcSecretkey();
		 String resultJson;
		//try {
			resultJson = HttpRequest.sendPost( NcSyncConstant.getNcTeacherNameMainUrl(), "type="+type+"&secretKey="+secretkey,null);
		
			HttpServiceResult s = JsonUtil.fromJson(resultJson, new TypeToken<HttpServiceResult>() {}.getType());
			 String key="psndoc_id";
			if ("1".equals(type)){
				key="teacher_id";
			}
			
			
			List<Map<String,String>> voList = JsonUtil.fromJson(JsonUtil.toJson(s.getData()), new TypeToken<List<Map<String,String>>>() {}.getType());
		    if (voList!=null&&voList.size()>0){
		    for (Map<String,String> map:voList){
		    	String teacherkey= map.get(key);
		    	if (root.get(teacherkey)==null){
		    		root.put(teacherkey, map);
		    	}
		    }
		    }
		
		
		
		//} catch (Exception e) {
			// TODO Auto-generated catch block
	///		e.printStackTrace();
		//}
	 
		
		
		return root;
	}
	
	
	
	
	
}
