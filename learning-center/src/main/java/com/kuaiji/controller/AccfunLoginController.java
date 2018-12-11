package com.kuaiji.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.kuaiji.utils.MsgDigestUtil;
import com.school.utils.HttpUtils;

@Controller
public class AccfunLoginController {
	
	@RequestMapping("/accfunlogin")
	public String accfunLogin(String courseId, String stuNo, HttpServletRequest request, Model model) {
		
		courseId = courseId.replace(" ", "+");
        stuNo = stuNo.replace(" ", "+");
        
        String value = "http://rspm.accfun.com/api.html?timestamp&partner=gdhqjy";
        String result = HttpUtils.httpRequest(value);
        JSONObject object = JSONObject.parseObject(result);
        
        String ts = object.getString("ts");
        String targets = "accreditKey=iOQIjvAFFD&courseId=" + courseId + "&partner=gdhqjy&stuNo=" + stuNo + "&ts=" + ts + "ooiZdl6RRqAVIAhbuOoIuBtpQZmXtE";
        String sign = MsgDigestUtil.MD5.digest2HEX(targets);
        
        value = "http://rspm.accfun.com/api.html?index&accreditKey=iOQIjvAFFD&courseId=" + courseId + "&partner=gdhqjy&stuNo=" + stuNo + "&ts=" + ts + "&sign=" + sign + "";
            
		return "redirect:"+value;
	}
	
}
