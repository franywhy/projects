package com.school.controller.app;

import com.school.controller.AbstractBaseController;
import com.school.pojo.UserInfoPOJO;
import com.school.service.UserstopService;
import com.school.utils.BusinessIdUtils;
import com.school.web.model.WrappedResponse;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 测试
 * @author shihongjie
 */
@Controller
@RequestMapping("/demo/app")
public class DemoController extends AbstractBaseController {

	@Autowired
	private UserstopService userstopService;

	@ApiOperation(value = "启动测试-报表版本")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query")
	})
	@RequestMapping(value = "/demo", method = RequestMethod.GET)
	public ResponseEntity<WrappedResponse> demo(HttpServletRequest request, HttpServletResponse response) {

		return this.success("启动测试-报表版本");
	}


	@ApiOperation(value = "启动测试-报表版本-用户信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query")
	})
	@RequestMapping(value = "/demo2", method = RequestMethod.GET)
	public ResponseEntity<WrappedResponse> demo2(HttpServletRequest request, HttpServletResponse response) {
		UserInfoPOJO userInfo = this.getUserInfo(request,response);
		return this.success(userInfo);
	}

	@ApiOperation(value = "启动测试-报表版本-业务线")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query")
	})
	@RequestMapping(value = "/demo3", method = RequestMethod.GET)
	public ResponseEntity<WrappedResponse> demo3(HttpServletRequest request, HttpServletResponse response) {
		UserInfoPOJO userInfo = this.getUserInfo(request,response);
		String businessId = BusinessIdUtils.getBusinessId(request);
		return this.success(businessId);
	}

	@ApiOperation(value = "启动测试-报表版本-产品线")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query")
	})
	@RequestMapping(value = "/demo4", method = RequestMethod.GET)
	public ResponseEntity<WrappedResponse> demo4(HttpServletRequest request, HttpServletResponse response) {
		UserInfoPOJO userInfo = this.getUserInfo(request,response);
		String businessId = BusinessIdUtils.getBusinessId(request);
		List<Long> productIdList = this.userstopService.queryProductId(businessId);
		return this.success(productIdList.toString());
	}
	
}
