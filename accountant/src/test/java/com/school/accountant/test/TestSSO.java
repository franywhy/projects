package com.school.accountant.test;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.school.accountant.util.EncryptionUtils;
import com.school.accountant.util.HttpUtil;

public class TestSSO {

	public static void main(String[] args) {
		String url = "http://177.77.83.248:8084/inner/login?mobileNo=13824429749&passWord=MTIzNDU2&clientType=web&versionCode=1";
		//String url = "http://177.77.83.248:8084/inner/userMobileNo?token=8098beb68000015da6de9f9e80000001";
		/*String url = "http://177.77.83.248:8084/inner/login";
		String pass = EncryptionUtils.base64Encode("123456"); 
		System.out.println(pass);
		JSONObject params = new JSONObject(); 
		params.put("mobileNo","13824429749");
		params.put("passWord",pass);
		params.put("clientType","web");
		params.put("versionCode",1);*/

		String result = HttpUtil.doPostWithJson4SSO(url);
		//String result = HttpUtil.doGet4Json(url);
		//{"code":200,"message":"操作成功","data":{"token":"8098beb68000015da75359fc80000001"}}
		//{"code":200,"message":"操作成功","data":{"mobileNo":"13824429749"}}
		System.out.println(result);
	}
}
