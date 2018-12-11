package com.izhubo.web.server

import static com.izhubo.rest.common.util.WebUtils.$$

import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang3.StringUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.util.JSONUtil
import com.izhubo.web.BaseController
import com.izhubo.web.api.Web
import com.izhubo.web.vo.UserinfoMySignInfoByNcIdH5VO
import com.mongodb.DBCollection
import com.wordnik.swagger.annotations.ApiImplicitParam
import com.wordnik.swagger.annotations.ApiImplicitParams
import com.wordnik.swagger.annotations.ApiOperation
/**
 * 意见反馈
* @ClassName: FeedBackController
* @Description: 意见反馈
* @author shihongjie
* @date 2015年8月22日 上午11:34:54
*
 */
@RestWithSession
@RequestMapping("/feedback")
class FeedBackController extends BaseController {
	
	private DBCollection feedback(){
		return mainMongo.getCollection("feedback");
	}
	
	private DBCollection schoolfeedback(){
		return mainMongo.getCollection("school_feedback");
	}
	private DBCollection schoolfeedbacktype(){
		return mainMongo.getCollection("school_feedback_type");
	}
	
	/**
	 * 校区反馈
	 */
	@ResponseBody
	@RequestMapping(value = "school_feedback/*-*", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "校区反馈", httpMethod = "POST"  , notes = "学习中心-报名表详情-h5")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "content", value = "问题内容", required = true, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "type_id", value = "问题类型", required = true, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "type_name", value = "问题类型", required = true, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "mobile", value = "手机号码", required = true, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "files", 
			value = "图片数组-可参照提交问题接口-将List<Map>转成JSON提交,Map中必须含有:文件原名称(original_file_name),文件大小(file_size),文件地址(pic_url),文件id(_id)", 
			required = true, dataType = "String", paramType = "query")
	])
	def school_feedback(HttpServletRequest request){
		//反馈内容
		String content = request["content"];
		//问题类型
		String type_id = request["type_id"];
		String type_name = request["type_name"];
		//手机号码
		String mobile = request["mobile"];
		//图片数组
		String files = request["files"];
		
		if(
			StringUtils.isNoneBlank(content) && StringUtils.isNoneBlank(type_id) && 
			StringUtils.isNoneBlank(type_name) && StringUtils.isNoneBlank(mobile)
			){
			Integer user_id = Web.getCurrentUserId();
			Long now = System.currentTimeMillis() ;
			def sfd = $$(
				"_id" : user_id + "_" + now ,
				"content" : content , "type_id" : type_id , 
				"type_name" : type_name , "mobile" : mobile,
				"timestamp" : now,
				"user_id" : user_id
				);
			
			//处理上传图片
			List picList = new ArrayList();
			if(StringUtils.isNotBlank(files)){
				List filesList = JSONUtil.jsonToBean(files, ArrayList.class);
				if(filesList){
					filesList.each {Map fm->
						Map map = new HashMap();
						//文件原名称
						map["original_file_name"] = fm["original_file_name"];
						//文件大小
						map["file_size"] = fm["file_size"];
						//文件地址
						map["pic_url"] = fm["url"];
						//文件id
						map["_id"] = fm["_id"];
	
						picList.add(map);
					}
				}
			}
	
			sfd["topics_pic"] = picList;
			
			
			schoolfeedback().save(sfd);
			return getResultOK();
		}
		
		return getResultParamsError();
	}
	
	/**
	 * 校区返回类型
	 */
	@ResponseBody
	@RequestMapping(value = "school_feedback_type/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "校区反馈问题类型列表", httpMethod = "GET" , notes = "List<Map<String , String>> {_id : _id , name : name}")
	def school_feedback_type(HttpServletRequest request){
		def list = schoolfeedbacktype().find($$("dr" : 0) , $$("_id" : 1 , "name" : 1)).sort($$("sort" : 1 , "_id" : 1)).limit(MAX_LIMIT).toArray();
		return getResultOK(list);
	}
	
	/**
	 * 意见反馈提交
	 * @Description: 意见反馈提交
	 * @date 2015年8月22日 上午11:56:19 
	 * @param @param request
	 * @param @return 
	 * @throws
	 */
	def upload(HttpServletRequest request){
		String content = request["content"];
		if(StringUtils.isNotBlank(content)){
			feedback().save(
				$$(
					"_id" : UUID.randomUUID().toString() ,
					"content" : content ,
					"timestamp" : System.currentTimeMillis(),
					"user_id" : Web.getCurrentUserId()
					)
				);
			return getResultOKS();
		}
		return getResultParamsError();
	}
	
}
