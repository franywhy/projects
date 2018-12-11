package com.izhubo.web

import static com.izhubo.rest.common.util.WebUtils.$$

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.apache.commons.lang.StringUtils
import org.springframework.beans.factory.annotation.Value

import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.util.JSONUtil;
import com.izhubo.web.api.Web
import com.mongodb.DBCollection

@RestWithSession
class ShareController extends BaseController {
	
	private DBCollection shareData(){
		return mainMongo.getCollection("share_data");
	}
	
	//初始化文件服务器地址
	String file_url ;
	@Value("#{application['pic.domain']}")
	void setFileUrl(String file_url){
		this.file_url = file_url;
	}
	
	
	
	/**
	 * 保存分享的图片
	 * @param request
	 * @param response
	 * @return
	 */
	def saveSharePic(HttpServletRequest request,HttpServletResponse response){
		String share_url = request["share_url"];
		String file_info = request["file_info"];
		if(StringUtils.isBlank(share_url)){
			return getResultParamsError();
		}
		
		Map fileInfoMap = null ;
		if(StringUtils.isNotBlank(file_info)){
			fileInfoMap = JSONUtil.jsonToMap(file_info);
		}
		
		int user_id = Web.getCurrentUserId();
		long share_time = System.currentTimeMillis();
		String _id = UUID.randomUUID().toString();
		def share = $$("_id" : _id , "user_id" : user_id , "share_url" : share_url , "share_time" : share_time , "file_info" : fileInfoMap);
		shareData().save(share);
		return getResultOK(_id);
	}
	
	
	
	
}
