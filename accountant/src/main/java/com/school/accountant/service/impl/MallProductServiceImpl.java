package com.school.accountant.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.school.accountant.common.SystemConfig;
import com.school.accountant.service.MallProductService;
import com.school.accountant.util.EncryptionUtils;
import com.school.accountant.util.HttpUtil;
import com.school.accountant.util.RedisUtil;
import com.school.accountant.vo.HttpServiceResult;
import com.school.accountant.vo.MallProductVo;

@Service
public class MallProductServiceImpl implements MallProductService {

	@Value("${SHIXUN_ALL_PRODUCTS}")
	private String SHIXUN_ALL_PRODUCTS;
	
	@Value("${SHIXUN_USER_PRODUCTS}")
	private String SHIXUN_USER_PRODUCTS;
	
	@Autowired
	private RedisUtil redisUtil;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public List queryAllProducts() {
		/*String result = redisUtil.get(SystemConfig.SHIXUN_ALL_PRODUCT_KEY);
		logger.debug("从redis获取实训系统所有账套信息="+result);
		if(StringUtils.isNotBlank(result)){
			JSONArray arr = JSONObject.parseArray(result);
			List products = JSONObject.toJavaObject(arr,List.class);
			return products;
		}else{*/
			String result = HttpUtil.doGet4Json(SHIXUN_ALL_PRODUCTS);
			logger.debug("获取实训系统所有账套信息接口返回结果="+result);
			if(StringUtils.isNotBlank(result)){
				JSONObject json = JSONObject.parseObject(result);
				String code = json.getString("code");
				if(StringUtils.isNotBlank(code) && "1".equals(code.trim())){
					HttpServiceResult datas = JSONObject.toJavaObject(json, HttpServiceResult.class);
					JSONArray arr = (JSONArray) json.get("data");
					String list = arr.toJSONString();
					//redisUtil.set(SystemConfig.SHIXUN_ALL_PRODUCT_KEY, list, 60*60*24);//账套信息缓存1天
					return (List)datas.getData();
				}
			}
			return new ArrayList<>();
		/*}*/
	}

	@Override
	public List queryUserProducts(String loginName) {
		if(StringUtils.isBlank(loginName)){
			return new ArrayList<>();
		}
		logger.info(SHIXUN_USER_PRODUCTS);
		String encrypt=EncryptionUtils.md5Hex(loginName+SystemConfig.KJCITY_MD5_KEY).toUpperCase();
		String shixun_user_products_temp = SHIXUN_USER_PRODUCTS.replaceAll("@key", encrypt);
		shixun_user_products_temp = shixun_user_products_temp.replaceAll("@loginName", loginName);
		String result = HttpUtil.doGet4Json(shixun_user_products_temp);
		logger.debug("获取实训系统用户["+loginName+"]已开通账套信息接口返回结果="+result);
		
		JSONObject json = JSONObject.parseObject(result);
		String code = json.getString("code");
		if(StringUtils.isNotBlank(code) && "1".equals(code.trim())){
			HttpServiceResult datas = JSONObject.toJavaObject(json, HttpServiceResult.class);			
			return (List)datas.getData();
		}else{
			return new ArrayList<>();
		}
	}
}
