package com.izhubo.web.server

import com.izhubo.rest.anno.Rest;
import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang.StringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.ServletRequestUtils
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.commons.CommonsMultipartResolver

import com.izhubo.common.doc.Param
import com.izhubo.common.util.KeyUtils
import com.izhubo.model.Code
import com.izhubo.model.Nodes
import com.izhubo.model.TopicContentType
import com.izhubo.model.TopicEndType
import com.izhubo.model.TopicEvaluationType
import com.izhubo.model.TopicsType
import com.izhubo.model.UserType
import com.izhubo.model.node.ChatBaseMsg
import com.izhubo.rest.anno.Rest
import com.izhubo.rest.common.util.JSONUtil
import com.izhubo.rest.common.util.NumberUtil
import com.izhubo.utils.RegExUtils
import com.izhubo.web.BaseController
import com.izhubo.web.api.Web
import com.izhubo.web.msg.MsgController
import com.izhubo.web.vo.TopicReplyVO
import com.mongodb.BasicDBList
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
@Rest
@TypeChecked(TypeCheckingMode.SKIP)
class IOSLogController extends BaseController{
	
	public DBCollection logs() {
		return logMongo.getCollection("ioslogs");
	}
	
	

	
	def upload_log(HttpServletRequest request){
		String content = request["content"];
		if(StringUtils.isNotBlank(content)){
			logs().save(
				$$(
					"_id" : UUID.randomUUID().toString() ,
					"content" : content ,
					"timestamp" : System.currentTimeMillis(),
					)
				);
			return getResultOKS();
		}
		return getResultParamsError();
	}
	
	@TypeChecked(TypeCheckingMode.SKIP)
	def get_log(HttpServletRequest request){

    	
		List loglist = logs().find().limit(100).toArray();
		return getResultOKS(loglist);
	}

}
