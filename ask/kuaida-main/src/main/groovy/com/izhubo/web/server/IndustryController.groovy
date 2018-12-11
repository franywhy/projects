package com.izhubo.web.server

import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import com.izhubo.rest.anno.Rest
import com.izhubo.web.BaseController
import com.mongodb.DBCollection;

@Rest
@TypeChecked(TypeCheckingMode.SKIP)
class IndustryController extends BaseController {
	
	private DBCollection industry(){
		return mainMongo.getCollection("industry");
	}
	
	/**
	 * 行业档案和标签
	 * @Description: 行业档案和标签
	 * @date 2015年6月10日 下午7:26:40 
	 * @param @return 
	 * @throws
	 */
	def industry_list(){
		List industrys = industry().find().limit(1000).toArray();
		return getResultOK(industrys);
	}
	
}
