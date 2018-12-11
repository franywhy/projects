package com.izhubo.credit.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.reflect.TypeToken;
import com.izhubo.credit.vo.HttpServiceResult;

 
/**
 * 学分运算同步类</p>
 * 
 * @author lintf 
 *
 */
public class SyncUtilRest {
  
 	
 	/**
 	 * Reg:运算报名表 Attend:考勤  Score:成绩单
 	 */
	public  final String CREDITSYNC_TYPE_REG = "Reg";
	/**
 	 * Reg:运算报名表 Attend:考勤  Score:成绩单
 	 */
	public  final String CREDITSYNC_TYPE_ATTEND = "Attend";
	/**
 	 * Reg:运算报名表 Attend:考勤  Score:成绩单
 	 */
	public  final String CREDITSYNC_TYPE_SCORE = "Score";
	
	public String sync(String startTime,String endTime,String type) {
		String note= "";
		String url=NcSyncConstant.getHqonlinesyncUrl()+"creditSync";
		String param="startTime="+startTime+"&endTime="+endTime+"&type="+type+"&secretKey="+ MD5Util.getMD5Code(    NcSyncConstant.getHqonlinesyncKey());		
		 note=sendPost(url,param);
		return note;
		
		
	}
	 
	
  public   String sendPost(String url,String param ) {
		
	  String result;
		try {
				
			result = HttpRequest.sendPost( url, param,null);
			HttpServiceResult s = JsonUtil.fromJson(result, new TypeToken<HttpServiceResult>() {}.getType());
			return s.getMsg();
		} catch (Exception e) {
			return null;
		}
		
	  
	  
	  
		 
		
	}
}
 



