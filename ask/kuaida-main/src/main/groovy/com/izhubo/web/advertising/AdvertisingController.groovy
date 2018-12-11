package com.izhubo.web.advertising

import static com.izhubo.rest.common.doc.MongoKey.$set
import static com.izhubo.rest.common.util.WebUtils.$$

import javax.servlet.http.HttpServletRequest

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.ServletRequestUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

import com.izhubo.web.BaseController
import com.izhubo.web.vo.BaseResultVO
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.wordnik.swagger.annotations.ApiImplicitParam
import com.wordnik.swagger.annotations.ApiImplicitParams
import com.wordnik.swagger.annotations.ApiOperation

/**
 *
 * @ClassName: AdvertisingController
 * @Description: 冷广告页
 * @author zww
 * @date 2017年8月21日
 *
 */
@Controller
@RequestMapping("/advertising")
class AdvertisingController extends BaseController{
	
	public DBCollection advertising() {
		return mainMongo.getCollection("advertising");
	}
	
	public DBCollection advertisingRecord() {
		return mainMongo.getCollection("advertising_record");
	}
	
	private static Logger logger = LoggerFactory
	.getLogger(AdvertisingController.class);
	
	/**
	 * 获取所有广告页列表
	 * @date 2017年8月22日 
	 * @param @param request
	 */
		@ResponseBody
		@RequestMapping(value = "get_advertising_list", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
		@ApiOperation(value = "获取所有广告页列表", httpMethod = "GET",  notes = "获取所有广告页列表", response = BaseResultVO.class)
		@ApiImplicitParams([
			@ApiImplicitParam(name = "token", value = "token" , required = false , dataType = "String" , paramType = "query" )
		])
	def get_advertising_list(HttpServletRequest request){
		String token = request["token"].toString()
		Integer user_id = 0
		if("null".equals(token) || null == token) {
			user_id = 0;
		}
		else {
			user_id = getUserIdByToken(token);
		}
		
		def result_list = advertising().find($$("is_show":1,"dr":0),$$("_id" : 1,"title":1,"pic":1,"url":1,"countdown":1,"version":1,"is_show":1)).toArray();
		return getResultOK(result_list);
	}
	

	/**
	 * 获取最新的广告页
	 * @date 2017年8月22日
	 * @param @param request
	 */
		@ResponseBody
		@RequestMapping(value = "get_latest_advertising", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
		@ApiOperation(value = "获取最新的广告页", httpMethod = "GET",  notes = "获取最新的广告页", response = BaseResultVO.class)
		@ApiImplicitParams([
			@ApiImplicitParam(name = "token", value = "token" , required = false , dataType = "String" , paramType = "query" )
		])
	def get_latest_advertising(HttpServletRequest request){
//		Integer id = ServletRequestUtils.getIntParameter(request, "id");
		String token = request["token"].toString()
		Integer user_id = 0
		if("null".equals(token) || null == token) {
			user_id = 0;
		}
		else {
			user_id = getUserIdByToken(token);
		}
		def result_list = []
		def ad_list = advertising().find($$("dr":0),$$("_id" : 1,"title":1,"pic":1,"url":1,"countdown":1,"version":1,"is_show":1))sort($$("version":-1)).toArray();
		if(ad_list){
			String url;
			String checkUrl = ad_list.get(0).get("url");
			if(checkUrl.indexOf("?") != -1){
				url = ad_list.get(0).get("url")+"&token="+token;
			}else{
				url = ad_list.get(0).get("url")+"?token="+token;
			}


			ad_list.get(0).put("url", url);
			result_list = ad_list.get(0);
		}
		return getResultOK(result_list);
	}
	
		/**
		 * 保存广告页数据
		 * @date 2018年2月2日
		 * @param @param request
		 */
		@ResponseBody
		@RequestMapping(value = "saveAdvertisingData", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
		@ApiOperation(value = "保存广告页数据", httpMethod = "GET",  notes = "保存广告页数据", response = BaseResultVO.class)
		@ApiImplicitParams([
		                    @ApiImplicitParam(name = "token", value = "token" , required = false , dataType = "String" , paramType = "query" ),
							@ApiImplicitParam(name = "data", value = "data" , required = false , dataType = "String" , paramType = "query" )
		                    ])
		def saveAdvertisingData(HttpServletRequest request){
			String token = ServletRequestUtils.getStringParameter(request, "token");
			String data = ServletRequestUtils.getStringParameter(request, "data");
			Integer user_id = 0
			if("null".equals(token) || null == token) {
				user_id = 0;
			}
			else {
				user_id = getUserIdByToken(token);
			}
			def now= new Date()
			def longTime= now.time
			def record = $$("_id" : UUID.randomUUID().toString());
			record.put("user_id", user_id);
			record.put("data", data);
			record.put("create_time", longTime);
			advertisingRecord().save(record);
			return getResultOK();
		}
		
}
