package com.izhubo.web

import static com.izhubo.rest.common.util.WebUtils.$$

import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang.StringUtils
import org.springframework.stereotype.Controller
import org.springframework.web.bind.ServletRequestUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

import com.izhubo.web.vo.BaseResultVO
import com.mongodb.DBCollection
import com.wordnik.swagger.annotations.ApiImplicitParam
import com.wordnik.swagger.annotations.ApiImplicitParams
import com.wordnik.swagger.annotations.ApiOperation
/**
 * 恒企志
 * @author shihongjie
 *
 */


@Controller
@RequestMapping("/annals")
class AnnalsController extends BaseController {
	
	DBCollection annals_detail(){
		return mainMongo.getCollection('annals_detail');
	}
	DBCollection annals(){
		return mainMongo.getCollection('annals');
	}
	
	@ResponseBody
	@RequestMapping(value = "annals_list", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "恒企志专题列表", httpMethod = "GET",response = BaseResultVO.class,  notes = "恒企志专题列表")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "size", value = "每页size条数据,默认20条", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "page", value = "第page页,默认第1页", required = false, dataType = "int", paramType = "query"),
	])
	def annals_list(HttpServletRequest request){
		//分页
		int size = ServletRequestUtils.getIntParameter(request, "size", 20);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		def list = annals().find(
				$$("status" : 1),
				$$("_id" : 1 , "pic" : 1 , "title" : 1 , "content" : 1)
			).sort($$("sort" : -1 , "create_time" : 1)).skip((page - 1) * size).limit(size).toArray();
	
		return getResultOK(list);
	}
	
	@ResponseBody
	@RequestMapping(value = "annals_detail_list", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "恒企志文章列表", httpMethod = "GET",response = BaseResultVO.class,  notes = "恒企志专题列表")
	@ApiImplicitParams([
	                    @ApiImplicitParam(name = "annals_id", value = "专题ID", required = true, dataType = "string", paramType = "query"),
	                    @ApiImplicitParam(name = "size", value = "每页size条数据,默认30条", required = false, dataType = "int", paramType = "query"),
	                    @ApiImplicitParam(name = "page", value = "第page页,默认第1页", required = false, dataType = "int", paramType = "query"),
	                    @ApiImplicitParam(name = "csort", value = "排序规则1.正序 -1倒序  默认-1", required = false, dataType = "int", paramType = "query")
	                    ])
	def annals_detail_list(HttpServletRequest request){
		//分页
		int size = ServletRequestUtils.getIntParameter(request, "size", 30);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		int csort = ServletRequestUtils.getIntParameter(request, "csort", -1);
		String annals_id = ServletRequestUtils.getStringParameter(request, "annals_id");
		if(StringUtils.isNotBlank(annals_id)){
			def list = annals_detail().find(
					$$("status" : 1 , "annals_id" : annals_id),
					$$("_id" : 1 , "cover_pic" : 1 , "cover_type" : 1 , "cover_title" : 1 , "cover_author" : 1 , "cover_content" : 1 , "timestamp_format" : 1)
					).sort($$("timestamp" : csort)).skip((page - 1) * size).limit(size).toArray();
			
			return getResultOK(list);
		}
		return getResultParamsError();
	}
	
	@ResponseBody
	@RequestMapping(value = "annals_detail_info", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "恒企志文章详情", httpMethod = "GET",response = BaseResultVO.class,  notes = "恒企志专题列表")
	@ApiImplicitParams([
	                    @ApiImplicitParam(name = "annals_details_id", value = "文章ID", required = true, dataType = "string", paramType = "query")
	                    ])
	def annals_detail_info(HttpServletRequest request){
		String annals_details_id = ServletRequestUtils.getStringParameter(request, "annals_details_id");
		if(StringUtils.isNotBlank(annals_details_id)){
			def obj = annals_detail().findOne($$("_id" : annals_details_id) , $$("content_html" : 1 , "read_count" :1))
			if(obj){
				//阅读量
				Integer read_count = obj["read_count"] as Integer;
				if(read_count == null)read_count = 0;
				//增加阅读量
				read_count++;
				annals_detail().update($$("_id" : annals_details_id), $$('$set' : $$("read_count" : read_count)));
			}
			return getResultOK(obj);
		}
		return getResultParamsError();
	}
	
}
