package com.izhubo.web

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.izhubo.rest.anno.RestWithSession;

@RestWithSession
class LoginOthController extends BaseController {
	
	/** md5Key*/
	private static final String MD5_KEY = "%^\$AF>.12*******";
	/** sso单点登录 */
	private static final String sso_login_url = "http://my.kjcity.com/WebService/SynchroMember.asmx";
	
	def loginsx(HttpServletRequest request,HttpServletResponse response){
		
	}
	
}
