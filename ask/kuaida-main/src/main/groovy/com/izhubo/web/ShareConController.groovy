package com.izhubo.web

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.apache.commons.lang.StringUtils

import com.izhubo.rest.anno.Rest
import com.mongodb.DBCollection

@Rest
@TypeChecked(TypeCheckingMode.SKIP)
class ShareConController extends BaseController {
	
	private DBCollection shareData(){
		return mainMongo.getCollection("share_data");
	}
	
	
	/**
	 * 获取分享图片的路径
	 * @param request
	 * @param response
	 * @return
	 */
	def sharePicUrl(HttpServletRequest request,HttpServletResponse response){
		String share_id = request["share_id"];
		if(StringUtils.isBlank(share_id)){
			return getResultParamsError();
		}
		def share = shareData().findOne($$("_id" : share_id) , $$("share_url" : 1 , "user_id" : 1 , "share_time" : 1));
		if(share){
			share["open_time"] = System.currentTimeMillis();
		}
		return getResultOK(share);
	}
	
}
