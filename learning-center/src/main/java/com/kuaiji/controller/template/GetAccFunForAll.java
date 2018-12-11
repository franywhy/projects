package com.kuaiji.controller.template;

import com.kuaiji.entity.AppProvince;
import com.kuaiji.entity.UserApp;
import com.kuaiji.service.AppProvinceService;
import com.kuaiji.service.UserAppService;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class GetAccFunForAll implements TemplateMethodModelEx {

	@Autowired
	private UserAppService userAppService;
	
	@Autowired
	private AppProvinceService appProvinceService;
	
	@Override
	public Object exec(List arguments) throws TemplateModelException { //按照全国的方式开通的单点登录，目前只开通了A001税务
		
		String url = "<a href=\"javascript:;\" class=\"btn radius-5px\" onclick=\"pageScript.showLayer();\">立即体验</a>";

		Long userId = Long.parseLong(arguments.get(1).toString());
		UserApp userApp = userAppService.findByCode(arguments.get(0).toString(), userId);
		if(null != userApp) {
			String schoolCode = userApp.getSchoolCode();
			String province = (null == schoolCode || schoolCode.isEmpty())? "JH208" : schoolCode.substring(0, 5);
			AppProvince appProvince = appProvinceService.findByProvince(province);
			if (null != appProvince)
			{
				url = "<a href=\"/accfunloginv2?domainCode=" + appProvince.getKjlProvinceCode() + "&courseId=" + userApp.getCourseid() + "&stuNo=" + userApp.getUsername() + "\" class=\"btn radius-5px\" target=\"_blank\">立即体验</a>";
			}
		}
		return url;
	}

}
