package com.school.accountant.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.school.accountant.common.SystemConfig;
import com.school.accountant.service.MallProductService;
import com.school.accountant.service.UserService;
import com.school.accountant.vo.PracticeLearnVo;
import com.school.accountant.vo.SSOResult;
import com.school.accountant.vo.UserInfo;

@Controller
@RequestMapping("/shixun")
public class ShiXunController extends AbstractBaseController{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MallProductService mallProductService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response){
		ModelAndView modelAndView = super.createModelAndView(request, response);
		
		//请求实训项目接口，获取所有账套信息
		List result = mallProductService.queryAllProducts();
		
		String loginName = "";
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute(SystemConfig.SESSION_USER_KEY);
		if(userInfo != null && StringUtils.isNotBlank(userInfo.getMobileNo())){
			loginName = userInfo.getMobileNo();
		}else{
			String token = (String) request.getSession().getAttribute(SystemConfig.SESSION_TOKEN_KEY);
			if(StringUtils.isNotBlank(token)){
				SSOResult sSOMobileResult = userService.userMobileNo(token);
				if(sSOMobileResult != null && StringUtils.isNotBlank(sSOMobileResult.getCode()) && SystemConfig.SSO_SUCCESS_CODE.equals(sSOMobileResult.getCode())){
					JSONObject mobileDataObj = (JSONObject) JSONObject.toJSON(sSOMobileResult.getData());
					//"mobileNo": "13824429749"
					loginName = mobileDataObj.getString("mobileNo");
				}
			}
		}
		logger.debug("loginName="+loginName);
		String practiceids = "";//用户已开通的账套id
		
		//请求实训项目接口，获取个人已开通的账套信息
		List<?> practiceLearnVolist = mallProductService.queryUserProducts(loginName);
		for(int i=0; i<practiceLearnVolist.size(); i++){
			JSONObject obj = (JSONObject)practiceLearnVolist.get(i);
			PracticeLearnVo vo = JSONObject.toJavaObject(obj, PracticeLearnVo.class);
			practiceids += vo.getPracticeid() + ",";
		}
		
		modelAndView.addObject("allProductlist", result);
		modelAndView.addObject("practiceLearnVolist", practiceLearnVolist);
		modelAndView.addObject("practiceids", practiceids);
		modelAndView.setViewName("shixun/index.ftl");
		return modelAndView;
	}
	
}
