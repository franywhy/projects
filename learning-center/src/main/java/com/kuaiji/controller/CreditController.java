package com.kuaiji.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.school.controller.AbstractBaseController;
import com.school.pojo.UserInfoPOJO;
import com.school.utils.Constants;
import com.school.utils.EncryptionUtils;
import com.school.utils.HttpUtils;
import com.school.utils.SSOTokenUtils;

/**
 * 会计-我的学分 控制器
 *
 */
@Controller
@RequestMapping("/learningCenter/web")
public class CreditController extends AbstractBaseController{

	@RequestMapping(value="/credit")
	public ModelAndView getCredit(HttpServletRequest request, HttpServletResponse response){
		
		ModelAndView mav = createModelAndView(request, response);
        mav.setViewName("learnCenter/credit");
		//String token = request.getParameter("token");
		String token = SSOTokenUtils.getToken(request, response);
		
		UserInfoPOJO userInfo = this.getUserInfoToken(request, token);
		Map<String,Object> result = SSOTokenUtils.getHttpResult(ncIdUrl, token , this.getBusinessId(request));
		if(null != result && StringUtils.isNotBlank((String)result.get("id"))){
			String ncId = (String)result.get("id");
			//String ncId = "1001A5100000002C68SR";//测试用
			//根据NC_ID调用学分系统接口获取我的学分
			String secretKey = EncryptionUtils.md5Hex(ncId + Constants.CREDIT_MD5_KEY).toLowerCase();
			String url = creditUrl.replaceAll("@studentid", ncId);
			url = url.replaceAll("@secretKey", secretKey);
			String httpResult = HttpUtils.httpRequest(url);
			mav.addObject("result", httpResult);
		}
		
		mav.addObject("userInfo", userInfo);
		return mav;
	}
	
    private static String creditUrl = "";
	@Value("#{application['pom.credit.getMyCredit.url']}")
	private void setCreditUrl(String url){
		this.creditUrl = url;
	}
	
    private static String ncIdUrl = "";
	@Value("#{application['pom.sso.ncId.url']}")
	private void setNcIdUrl(String url){
		this.ncIdUrl = url;
	}
}
