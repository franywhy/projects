package com.izhubo.web.user

import java.awt.image.BufferedImage
import javax.servlet.http.HttpServletRequest
import org.springframework.web.multipart.MultipartFile

import com.izhubo.common.util.KeyUtils
import com.izhubo.rest.anno.RestWithSession;
import com.izhubo.web.BaseController
import com.izhubo.web.api.Web
import com.izhubo.web.interceptor.OAuth2SimpleInterceptor

@RestWithSession
class UserController extends BaseController {
	
	/**
	 * 用户退出upload_pic
	 * @Description: 用户退出
	 * @date 2015年8月22日 下午4:14:13 
	 * @param @param req
	 * @param @return 
	 * @throws
	 */
	def logout(HttpServletRequest req){
		def token = OAuth2SimpleInterceptor.parseToken(req)
		if(token){
			mainRedis.delete(KeyUtils.accessToken(token));
//			mainRedis.delete(KeyUtils.USER.token(Web.getCurrentUserId()))
		}
		return [code:1];
	}
	
	def con(){
		return getResultOKS();
	}
	
	
	//图片处理 暂时拿掉
	
	
	
}
