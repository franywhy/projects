package com.izhubo.web.idcard

import static com.izhubo.rest.common.util.WebUtils.$$

import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang3.StringUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

import com.izhubo.rest.anno.RestWithSession
import com.izhubo.web.BaseController
import com.izhubo.web.api.Web
import com.mongodb.DBCollection
import com.wordnik.swagger.annotations.ApiImplicitParam
import com.wordnik.swagger.annotations.ApiImplicitParams
import com.wordnik.swagger.annotations.ApiOperation

@RestWithSession
class IDCardController extends BaseController{
	
	
	private DBCollection idcard_log(){
		return logMongo.getCollection("idcard_log");
	}
	
	//TODO 返回值
	@ResponseBody
	@RequestMapping(value = "update", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "更新身份证信息", httpMethod = "POST", notes = "更新身份证信息 ")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "_id", value = "NC扫码得到的唯一ID", required = true, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "idcard_num", value = "身份证号码", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "idcard_name", value = "身份证姓名", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "idcard_sex", value = "身份证性别", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "idcard_birthday", value = "身份证出生日期", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "idcard_address", value = "身份证住址", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "remark", value = "备注信息,可以保存第三方扫描返回的结果", required = false, dataType = "String", paramType = "query")
	])
	def update(HttpServletRequest req){
		//nc生成的位置值
		String nc_id = req["_id"];
		//身份证号码
		String idcard_num = req["idcard_num"];
		//身份证姓名
		String idcard_name = req["idcard_name"];
		//身份证性别
		String idcard_sex = req["idcard_sex"];
		//身份证出生日期
		String idcard_birthday = req["idcard_birthday"];
		//身份证住址
		String idcard_address = req["idcard_address"];
		//remark
		String remark = req["remark"];
		
		if(StringUtils.isBlank(nc_id)){
			return ["code" : 30406 , "data" : "参数异常"];
		}
		
		//用户id
		Integer user_id = Web.getCurrentUserId();
		//更新
		if(idcard_log().update(
			$$("_id" : "_id"), 
			$$('$set' : 
				$$(
					"idcard_num" : idcard_num , "idcard_name" : idcard_name , 
					"remark" : remark , "update_time" : System.currentTimeMillis(),
					"idcard_sex" : idcard_sex , "idcard_birthday" : idcard_birthday,
					"idcard_address" : idcard_address,
					"user_id" : user_id
					)
				)
			).getN() == 1){
			return ["code" : 1];
		}else{
			return ["code" : 0 , "data" : "操作异常!"];
		}
		
	}
}
