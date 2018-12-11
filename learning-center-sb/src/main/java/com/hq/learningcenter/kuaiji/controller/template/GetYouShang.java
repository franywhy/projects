package com.hq.learningcenter.kuaiji.controller.template;

import com.hq.learningcenter.kuaiji.entity.UserApp;
import com.hq.learningcenter.kuaiji.service.UserAppService;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class GetYouShang implements TemplateMethodModelEx {

	@Autowired
	private UserAppService userAppService;
	
	@Override
	public Object exec(List arguments) throws TemplateModelException {
		
		String url = "<a href=\"javascript:;\" class=\"btn radius-5px\" onclick=\"pageScript.showLayer();\">立即体验</a>";

		Long userId = Long.parseLong(arguments.get(1).toString());
		UserApp userApp = userAppService.findByCode(arguments.get(0).toString(), userId);
		if(null != userApp) {
			url = "<a href=\"/youshanglogin?userName=" + userApp.getUsername() + "\" class=\"btn radius-5px\" target=\"_blank\">立即体验</a>";
		}
		return url;
	}
	
}
