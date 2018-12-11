package com.hq.learningapi.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hq.learningapi.pojo.UserInfoPOJO;
import com.hq.learningapi.util.SSOTokenUtils;
import com.hq.learningapi.util.http.HttpClientUtil4_3;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * Created by LiuHai on 2018/01/05
 *
 */

@Controller
@RequestMapping("/api")
public class AppUserMessageController extends AbstractRestController {
	
	@ApiOperation(value = "获取用户消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "start_time", value = "开始时间 yyyy-MM-dd HH:mm", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "end_time", value = "结束时间  yyyy-MM-dd HH:mm", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/userMessage", method = RequestMethod.GET)
	public ResponseEntity<WrappedResponse> getUserMessage(HttpServletRequest request) {
		try {
			String token = ServletRequestUtils.getStringParameter(request, "token", "");
			String start_time = ServletRequestUtils.getStringParameter(request, "end_time" , "");
			String end_time = ServletRequestUtils.getStringParameter(request, "start_time" , "");
			UserInfoPOJO userInfo = SSOTokenUtils.getUserInfo(request, token);
			Long userId = userInfo.getUserId();
			if(null != userId && userId > 0){
				String userMessasg = HttpClientUtil4_3.get("/msg/getMessageByUserId?start_time="+start_time+"&end_time="+end_time+"&user_id="+userId, null);
				Map<String, Object> resultMap = new HashMap<>();
				resultMap.put("userMessasg", userMessasg);
				return this.success(resultMap);
			}
			return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
		} catch (Throwable t) {
			TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
