package com.izhubo.web

import static com.izhubo.rest.common.util.WebUtils.$$

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

import com.izhubo.model.Code;
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.web.vo.AHHomeCommodityListVO
import com.mongodb.DBCollection;
import com.wordnik.swagger.annotations.ApiOperation

@RestWithSession
@RequestMapping(value = ["/aurl", "/api"])
class AUrlController extends BaseController {
	
	/** 每日推送更多接口 */
	@Value("#{application['day.push.more.url']}")
	private String day_push_more_url ;
	
	/** 课程列表接口 */
	@Value("#{application['course.list.url']}")
	private String course_list_url ;
	
	/**	H5地址库 */
	public DBCollection ah_url() {
		return mainMongo.getCollection("ah_url");
	}
	
	/**
	 * 每日推送更多接口
	 */
	@ResponseBody
	@RequestMapping(value = "getDayPushMoreUrl", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "每日推送更多接口", httpMethod = "GET", notes = "每日推送更多接口")
	def getDayPushMoreUrl(){
		return getResultOK(day_push_more_url);
	}
	
	/**
	 * 课程列表接口 
	 */
	@ResponseBody
	@RequestMapping(value = "getCourseListUrl", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "课程列表接口 ", httpMethod = "GET", notes = "课程列表接口 ")
	def getCourseListUrl(){
		return getResultOK(course_list_url);
	}
	
	@ResponseBody
	@RequestMapping(value = "getH5Url", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "H5地址列表", httpMethod = "GET" ,notes = "1001-咨询地址;1002-学习中心地址;1003-课程列表地址;1004-商城地址;1005-学生签到打卡地址;1006-老师打卡地址.(按照顺序返回,可根据id或者顺序)")
	def getH5Url(HttpServletRequest request){
		println(ah_url().find(null, $$("_id" : 1 , "name" : 1 , "url" : 1)).sort($$("_id" : 1)).toArray())
		return getResultOK(ah_url().find(null, $$("_id" : 1 , "name" : 1 , "url" : 1)).sort($$("_id" : 1)).toArray());
	}
	
	@ResponseBody
	@RequestMapping(value = ["getH5Url_v200","constantValue"], method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "H5地址列表_v200(修改格式)", httpMethod = "GET" ,notes = "1001-咨询地址;1002-学习中心地址;1003-课程列表地址;1004-商城地址;1005-学生签到打卡地址;1006-老师打卡地址.(按照顺序返回,可根据id或者顺序)")
	def getH5Url_v200(HttpServletRequest request){
		List h5UrlList = ah_url().find(null, $$("_id" : 1 , "name" : 1 , "url" : 1)).sort($$("_id" : 1)).toArray();
		List<Map<String,Object>> resultList= new ArrayList<Map<String,Object>>();
		for(item in h5UrlList){
			Map<String,Object> newMap = new HashMap<String,Object>();
			newMap.ckey=item.get("_id")
			newMap.name=item.get("name")
			newMap.cvalue=item.get("url")
			resultList<<newMap
		}
		return getResultOK(resultList);
	}

}
