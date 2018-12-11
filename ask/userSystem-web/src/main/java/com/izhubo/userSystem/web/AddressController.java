package com.izhubo.userSystem.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
@Controller
public class AddressController {
	
	@Resource
	protected MongoTemplate mainMongo;
	
	private static final String PROVINCE_CITY_SPLIT = " ";
	
	/**
	 * 根据电话号码获取地址
	 * @Description: 根据电话号码获取地址
	 * @date 2015年10月19日 上午10:27:47 
	 * @param @param mobile
	 * @param @return Map map["mobile"]=手机号,map["province_city"]=省市,map["province"]=省,map["city"]=市
	 */
	public Map<String , String> addressByMobile(String mobile){
		Map<String , String> map = new HashMap<String , String>();
		map.put("province_city", "");
		map.put("province", "");
		map.put("city", "");
		map.put("mobile", mobile);
		if(StringUtils.isNotBlank(mobile) && mobile.length() == 11){
			DBObject dm_mobile =  mainMongo.getCollection("dm_mobile").findOne(
											new BasicDBObject("mobile_num" ,mobile.substring(0,7)) , 
											new BasicDBObject("province_city" ,1)
										);
			if(dm_mobile != null){
				String province_city = dm_mobile.get("province_city").toString();
				if(StringUtils.isNotBlank(province_city)){
					String[] arr = province_city.split(PROVINCE_CITY_SPLIT);
					
					if(arr.length==1)
					{
						 map.put("province_city", province_city);
						 map.put("province", province_city);
						 map.put("city", province_city);
					}
					if(arr.length==2)
					{
					  map.put("province_city", province_city);
					  map.put("province", arr[0]);
					  map.put("city", arr[1]);
					}
				}
			}
			
			
		}
		return map;
	}
	
}
