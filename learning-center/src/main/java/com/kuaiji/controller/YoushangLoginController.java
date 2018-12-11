package com.kuaiji.controller;

import com.kuaiji.utils.CommonUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class YoushangLoginController {
	
	@RequestMapping("/youshanglogin")
	public String accfunLogin(String userName, HttpServletRequest request, Model model) {
		
		userName = userName.replace(" ", "+");
        
		String pwd = "JDeO-1612-02D-1107-8711";
        String from = request.getRemoteAddr();
        String strTest = getUserLoginXml(userName, from);
        
        String value = CommonUtil.encrypt(strTest, pwd);
        
		return "redirect:"+value;
	}

    private static String getUserLoginXml(String username, String from)
    {
        StringBuilder sbXml = new StringBuilder(100);
        sbXml.append("<?xml version=\"1.0\" encoding = \"utf-8\"?>");
        sbXml.append("<request>");
        sbXml.append("<type>userLogin</type>");
        sbXml.append("<data>");
        sbXml.append("<attr name=\"userAccount\">" + username + "</attr>");
        sbXml.append("<attr name=\"time\">" + System.currentTimeMillis() + "</attr>");
        sbXml.append("<attr name=\"productType\"></attr>");
        sbXml.append("<attr name=\"userType\">" + 2 + "</attr>");
        sbXml.append("<attr name=\"serviceId\"></attr>");
        sbXml.append("<attr name=\"from\">" + from + "</attr>");
        sbXml.append("</data>");
        sbXml.append("</request>");
        return sbXml.toString();
    }

}
