package com.izhubo.web

import com.izhubo.common.util.KeyUtils

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.springframework.beans.factory.annotation.Value

import com.izhubo.common.util.KeyUtils.BUNUS
import com.izhubo.common.util.KeyUtils.TOPICES
import com.izhubo.common.util.KeyUtils.MSG
import com.izhubo.rest.anno.Rest
import com.izhubo.web.server.TopicsController
import com.izhubo.web.server.TopicsLoginController
@Rest
class KeyExpriedNotifyController extends BaseController{
	
	@Resource
	TopicsController topicsController;
	@Resource
	TopicsLoginController topicsLoginController;
	@Resource
	MessageController messageController;
	
	
	@Value("#{application['push.env']}")
	private int PUSH_ENV;
	
	def key_expried(HttpServletRequest request){
		//消息内容
		//消息对象
		//消息id
		String  ExpriedKey= request["ExpriedKey"];
		println "==============================key_key_expried ExpriedKey:" + ExpriedKey
		if(ExpriedKey.startsWith(TOPICES.TOPICES))
		{
			topicsController.topicTimeoutProcess(ExpriedKey);
		}else if(ExpriedKey.startsWith(TOPICES.TOPICES_ZIKAO)) {
			topicsController.topicTimeoutProcess(ExpriedKey)
		}else if(ExpriedKey.startsWith(TOPICES.TOPICES_CHAT_TIMEOUT)){
			topicsController.chatTimeoutProcess(ExpriedKey)
		}else if(ExpriedKey.startsWith(BUNUS.BUNUS_OPEN_TIMEOUT_KEY)){
			topicsLoginController.openRedTimeOut(ExpriedKey);
		}else if(ExpriedKey.startsWith(TOPICES.TOPICES_PJ_REMIND)){
	    	topicsController.topicPJTimeoutProcess(ExpriedKey);
		}else if(ExpriedKey.startsWith(MSG.MSG_TIME_JOB)){
			messageController.MsgJobTimeoutProcess(ExpriedKey);
		}
		else if(ExpriedKey.startsWith(KeyUtils.LIVE.LIVE_REMIND)){
			messageController.LiveRemindTimeoutProcess(ExpriedKey);
		}


		
		
		
	}
	
	

}
