package com.izhubo.web.version

import static com.izhubo.rest.common.util.WebUtils.$$
import static com.izhubo.rest.common.doc.MongoKey.*;
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.ServletRequestUtils
import com.izhubo.web.BaseController;
import com.mongodb.DBCollection;
import com.wordnik.swagger.annotations.ApiOperation
import com.wordnik.swagger.annotations.ApiImplicitParam
import com.wordnik.swagger.annotations.ApiImplicitParams
import com.wordnik.swagger.annotations.ApiOperation

@Controller
@RequestMapping("/version")
class VersionController extends BaseController {


	public DBCollection app_version() {
		return mainMongo.getCollection("app_version");
	}

	public DBCollection app_version_users() {
		return mainMongo.getCollection("app_version_users");
	}

	/**
	 * android_version
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "android_version", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "android_version", httpMethod = "GET"  ,  notes = "android_version")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "user_id", value = "用户id", required = true, dataType = "String", paramType = "query")
	])
	def android_version(HttpServletRequest request){
		//先判断最新的升级，是不是灰度测试，如果是灰度测试，就判断当前用户是否是测试用户，如果是，返回，如果不是，则向前搜索，找到比当前版本第一个版本的
		def list = app_version().find($$("type","android")).sort($$("version_code" : -1)).toArray();

		Integer user_id = ServletRequestUtils.getIntParameter(request , "user_id" , 0);
		if(list)
		{
			def newsestVersion = list.get(0);
			if(newsestVersion.get("is_grey_up").equals(1))
			{
				if(app_version_users().count($$("user_id",user_id).append("version_code",newsestVersion.get("version_code"))).value>0)
				{
					getResultOK(newsestVersion);
				}
				else
				{
					def version = getNewestVersionNoGrey("android");
					getResultOK(version);
				}
			}
			else
			{

				getResultOK(newsestVersion);
			}
		}
		else{
			getResultOK();
		}
	}

	/**
	 * android_version
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "ios_version", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "ios_version", httpMethod = "GET"  ,  notes = "android_version")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "user_id", value = "用户id", required = true, dataType = "String", paramType = "query")
	])
	def ios_version(HttpServletRequest request){
		//先判断最新的升级，是不是灰度测试，如果是灰度测试，就判断当前用户是否是测试用户，如果是，返回，如果不是，则向前搜索，找到比当前版本第一个版本的
		def list = app_version().find($$("type","ios")).sort($$("version_code" : -1)).toArray();
		Integer user_id = ServletRequestUtils.getIntParameter(request , "user_id" , 0);

		if(list)
		{
			def newsestVersion = list.get(0);
			if(newsestVersion.get("is_grey_up").equals(1))
			{
				if(app_version_users().count($$("user_id",user_id).append("version_code",newsestVersion.get("version_code"))).value>0)
				{
					getResultOK(newsestVersion);
				}
				else
				{
					def version = getNewestVersionNoGrey("ios");
					getResultOK(version);
				}
			}
			else
			{

				getResultOK(newsestVersion);
			}
		}
		else{
			getResultOK();
		}


	}

	def getNewestVersionNoGrey(String type)
	{
		def list = app_version().find($$("type",type).append("is_grey_up",0)).sort($$("version_code" : -1)).toArray();
		def newsestVersion = list.get(0);
		return newsestVersion;
	}

}
