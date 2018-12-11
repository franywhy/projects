package com.izhubo.web.idcard

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.apache.commons.lang3.StringUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

import com.izhubo.rest.anno.Rest
import com.izhubo.rest.common.util.QRcode
import com.izhubo.web.BaseController
import com.izhubo.web.api.Web
import com.mongodb.DBCollection
import com.wordnik.swagger.annotations.ApiImplicitParam
import com.wordnik.swagger.annotations.ApiImplicitParams
import com.wordnik.swagger.annotations.ApiOperation

@Rest
@TypeChecked(TypeCheckingMode.SKIP)
class NCIDCardController extends BaseController{
	
	
	private DBCollection idcard_log(){
		return logMongo.getCollection("idcard_log");
	}
	
	//TODO 返回值
	@ResponseBody
	@RequestMapping(value = "save", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "生成身份证记录", httpMethod = "POST", notes = "生成身份证记录")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "_id", value = "NC扫码得到的唯一ID", required = true, dataType = "String", paramType = "query")
	])
	def save(HttpServletRequest req , HttpServletResponse response){
		//nc生成的位置值
		String nc_id = req["_id"];
		
		if(StringUtils.isBlank(nc_id)){
			return ["code" : 30406 , "data" : "参数异常"];
		}
		
		//用户id
		Integer user_id = Web.getCurrentUserId();
		//更新
		Long now = System.currentTimeMillis();
		idcard_log().save($$(
			"_id" : nc_id , "timestamp" : now , "update_time" : now ,
			"idcard_num" : null , "idcard_name" : null ,
			"remark" : null , "user_id" : null,
			"idcard_sex" : null , "idcard_birthday" : null,
			"idcard_address" : null
			));
//		res.sendRedirect("http://www.baidu.com");
		response.addHeader('Content-Type',"image/png")
		return QRcode.PrintQR2Steam(nc_id, response.getOutputStream());
//		return ["code" : 1];
	}
	
	//TODO 返回值
	@ResponseBody
	@RequestMapping(value = "get", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "获取身份证信息", httpMethod = "POST", notes = "获取身份证信息")
	@ApiImplicitParams([
	                    @ApiImplicitParam(name = "_id", value = "NC扫码得到的唯一ID", required = true, dataType = "String", paramType = "query")
	                    ])
	def get(HttpServletRequest req){
		//nc生成的位置值
		String nc_id = req["_id"];
		
		if(StringUtils.isBlank(nc_id)){
			return ["code" : 30406 , "data" : "参数异常"];
		}
		def idlog = idcard_log().findOne($$("_id" : nc_id));
		return ["code" : 1 , "data" : idlog];
	}
	
	
}
