package com.izhubo.web.server

import javax.servlet.http.HttpServletRequest;

import com.izhubo.rest.anno.RestWithSession
import com.izhubo.web.BaseController

/**
 * 其他
* @ClassName: OtherController 
* @Description: 其他
* @author shihongjie
* @date 2015年9月18日 上午11:17:45 
*
 */
@RestWithSession
class OtherController  extends BaseController{
	
	/**
	 * 获取系统时间
	 * @Description:  获取系统时间
	 * @date 2015年9月18日 上午11:18:36 
	 * @param @return 
	 * @throws
	 */
	def currentTime(){
		return getResultOKS(System.currentTimeMillis());
	}
	


	
}
