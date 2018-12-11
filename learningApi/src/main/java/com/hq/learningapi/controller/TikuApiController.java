package com.hq.learningapi.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import com.hq.learningapi.service.TikuApiService;

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
public class TikuApiController extends AbstractRestController {

	@Autowired
	private TikuApiService tikuApiService;
	
	@ApiOperation(value = "根据课程服务id获取学员知识点掌握情况")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "courseFk", value = "课程服务id", required = true, dataType = "String", paramType = "query")
			})
	@RequestMapping(value = "/tiku/getPhaseList", method = RequestMethod.GET)
	public ResponseEntity<WrappedResponse> getPhaseList(HttpServletRequest request) {
		try {
			String token = ServletRequestUtils.getStringParameter(request, "token", "");
			String courseFk = ServletRequestUtils.getStringParameter(request, "courseFk", "");
			TRACER.info(String.format("\nRequest Token:%s,\nRequest courseFk:%s", token, courseFk));

			List<Map<String, Object>> result = tikuApiService.getPhaseList(request, token, courseFk);
			if (result.size() != 0) {
				if (null!=result.get(0).get("code") ) {
					if(result.get(0).get("code").equals(400)){
						return this.fail(result.get(0).get("message").toString(),TransactionStatus.INTERNAL_SERVER_ERROR);
					}else if(result.get(0).get("code").equals(401)){
						return this.fail(result.get(0).get("message").toString(),TransactionStatus.INTERNAL_SERVER_ERROR);
					}else{
						return this.fail(result.get(0).get("message").toString(),TransactionStatus.INTERNAL_SERVER_ERROR);
					}
				} else {
					return this.success(result);
				}
			} else {
				return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Throwable t) {
			TRACER.error("", t);
			return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
