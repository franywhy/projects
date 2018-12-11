package com.izhubo.web.server

import static com.izhubo.rest.common.util.WebUtils.$$
import static com.izhubo.rest.common.doc.MongoKey.*;
import javax.servlet.http.HttpServletRequest

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

import com.izhubo.web.BaseController;
import com.mongodb.DBCollection;
import com.wordnik.swagger.annotations.ApiOperation

@Controller
@RequestMapping("/home")
class HomeController extends BaseController {
	
	
	/** banner */
	public DBCollection banner() {
		return mainMongo.getCollection("banner");
	}
	/** 课程列表 */
	public DBCollection bannerCourse() {
		return mainMongo.getCollection("banner_course");
	}
	
	/**
	 * 首页推荐列表
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "bannerList", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "首页banner推荐List", httpMethod = "GET"  ,  notes = "首页banner推荐List")
	def bannerList(HttpServletRequest request){
		
		def list = banner().find(null , $$("url" : 1 , "pic" : 1 , "_id" : 0)).sort($$("sort" : 1)).limit(10).toArray();
		
		return getResultOK(list);
	}
	/**
	 * 首页推荐列表
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "courseList", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "首页面授课程推荐列表", httpMethod = "GET"  ,  notes = "首页面授课程推荐列表")
	def courseList(HttpServletRequest request){
		
		def list = bannerCourse().find(null , $$("url" : 1 , "pic" : 1 , "_id" : 0)).sort($$("sort" : 1)).limit(4).toArray();
		
		return getResultOK(list);
	}
	
	
	
}
