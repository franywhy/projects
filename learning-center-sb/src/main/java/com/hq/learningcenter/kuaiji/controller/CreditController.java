package com.hq.learningcenter.kuaiji.controller;

import com.hq.learningcenter.config.LocalConfig;
import com.hq.learningcenter.config.SsoConfig;
import com.hq.learningcenter.school.controller.AbstractBaseController;
import com.hq.learningcenter.school.pojo.UserInfoPOJO;
import com.hq.learningcenter.utils.Constants;
import com.hq.learningcenter.utils.EncryptionUtils;
import com.hq.learningcenter.utils.HttpUtils;
import com.hq.learningcenter.utils.SSOTokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 会计-我的学分 控制器
 *
 */
@Controller
@RequestMapping("/learningCenter/web")
public class CreditController extends AbstractBaseController {

	@Autowired
	private LocalConfig localConfig;
	@Autowired
	private SsoConfig ssoConfig;

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
	@Value("")
	private void setCreditUrl(){
		creditUrl = localConfig.getCreditGetmycreditUrl();
	}
	
    private static String ncIdUrl = "";
	@Value("")
	private void setNcIdUrl(){
		ncIdUrl = ssoConfig.getSsoNcidUrl();
	}
}
