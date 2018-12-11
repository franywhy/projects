package com.izhubo.userSystem.utils;




import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.izhubo.rest.common.util.JSONUtil;
import com.izhubo.rest.common.util.http.HttpClientUtil4_3;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;


/**
 * 通过手机号码,获得该号码的归属地
 * http://www.ip138.com:8080/search.asp?action=mobile&mobile=13824429749
 * @author Administrator
 *
 */
public class PhoneNumberUtil {
	//正则表达式,抽取手机归属地
	public static final String REGEX_GET_MOBILE=
		"(?is)(<tr[^>]+>[\\s]*<td[^>]+>[\\s]*卡号归属地[\\s]*</td>[\\s]*<td[^>]+>([^<]+)</td>[\\s]*</tr>)"; //2:from
	//正则表达式,审核要获取手机归属地的手机是否符合格式,可以只输入手机号码前7位
	public static final String REGEX_IS_MOBILE=
		"(?is)(^1[3|4|5|8][0-9]\\d{4,8}$)";
	
	/**
	 * 获得手机号码归属地
	 * 
	 * @param mobileNumber
	 * @return
	 * @throws Exception
	 */
	public static String getMobileFrom(String mobileNumber,MongoTemplate mongoQuery){
		String Result = "";
		if(!veriyMobile(mobileNumber)){
			Result = "unknow unknow";
			return Result;
		}
		else
		{
			String phoneHeaderStr = mobileNumber.substring(0,7);

			 DBObject phoneResult =  mongoQuery.getCollection("dm_mobile").findOne(new BasicDBObject("mobile_num" ,phoneHeaderStr));
			 
			 if(phoneResult!=null)
			 {
				 Result = phoneResult.get("mobile_num").toString();
			 }
			 else
			 {
				 Result = "unknow unknow";
			 }
			 
			 
		  return Result;
		}
	}


	/**
	 * 从www.ip138.com返回的结果网页内容中获取手机号码归属地,结果为：省份 城市
	 * 
	 * @param htmlSource
	 * @return
	 */
	public static String parseMobileFrom(String htmlSource){
		Pattern p=null;
		Matcher m=null;
		String result=null;
		
		p=Pattern.compile(REGEX_GET_MOBILE);
		m=p.matcher(htmlSource);
		
		while(m.find()){
			if(m.start(2)>0){
				result=m.group(2);
				result=result.replaceAll("&nbsp;", " ");
			}
		}
		
		
		return result;
		
	}
	
	/**
	 * 验证手机号
	 * @param mobileNumber
	 * @return
	 */
	public static boolean veriyMobile(String mobileNumber){
		Pattern p=null;
		Matcher m=null;
		
		p=Pattern.compile(REGEX_IS_MOBILE);
		m=p.matcher(mobileNumber);
		
		return m.matches();
	}
	
	/**
	 * 测试
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
	}

}