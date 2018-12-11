package com.school.controller.pc;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.school.controller.AbstractBaseController;
import com.school.entity.UserstopEntity;
import com.school.pojo.UserInfoPOJO;
import com.school.pojo.UserstopPOJO;
import com.school.service.UserstopService;
import com.school.utils.BusinessIdUtils;
import com.school.utils.DateUtils;
import com.school.web.model.WrappedResponse;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/learningCenter/web")
public class UserstopController extends AbstractBaseController {
	@Autowired
	private UserstopService userstopService;
	
	@ApiOperation(value = "休学列表页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query")
    })
	@RequestMapping(value = "/userstopList", method = RequestMethod.GET)
	public ModelAndView userstopListPage(HttpServletRequest request, HttpServletResponse response) {
		UserInfoPOJO userInfo = this.getUserInfo(request,response);
		String businessId = BusinessIdUtils.getBusinessId(request);
		List<Long> productIdList = this.userstopService.queryProductId(businessId);
		
		//查询学员休学列表数据
		List<UserstopPOJO> userstopList = this.userstopService.quseryList(userInfo.getUserId(), productIdList);
		//查询班型列表
		List<Map<String, Object>> classtypeListMap = this.userstopService.queryClasstypeList(userInfo.getUserId(), productIdList);
		
		ModelAndView mav = this.createModelAndView(request, response, true);
		mav.setViewName("learnCenter/attendance");
		mav.addObject("data",userstopList);
		mav.addObject("classtypeData",classtypeListMap);
		return mav;
	}
	
	@ApiOperation(value = "休学列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query")
    })
	@RequestMapping(value = "/userstop/list", method = RequestMethod.GET)
	public ResponseEntity<WrappedResponse> list(HttpServletRequest request, HttpServletResponse response) {
		UserInfoPOJO userInfo = this.getUserInfo(request,response);
		String businessId = BusinessIdUtils.getBusinessId(request);
		List<Long> productIdList = this.userstopService.queryProductId(businessId);
		//查询学员休学列表数据
		List<UserstopPOJO> userstopList = this.userstopService.quseryList(userInfo.getUserId(), productIdList);
		
		return this.success(userstopList);
		
	}
	
	@ApiOperation(value = "操作休学列表记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "id", value = "休学记录Id", required = true, dataType = "String", paramType = "query")
    })
	@RequestMapping(value = "/userstop/update", method = RequestMethod.GET)
	public ResponseEntity<WrappedResponse> update(HttpServletRequest request, HttpServletResponse response) {
		
		String idStr = request.getParameter("id");
		if(null == idStr) return this.error("参数提交有误:id");
		if(StringUtils.isBlank(idStr)){
			return this.success("请选择一条记录！！！");
		}
		Long id = Long.parseLong(idStr);
		
		this.userstopService.update(id);
		
		return this.success();
	}
	
	@ApiOperation(value = "申请休学")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userplanId", value = "学员规划Id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "休学时间(yyyy-MM-dd HH:mm)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "复课时间(yyyy-MM-dd HH:mm)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "stopCause", value = "休学原因", required = true, dataType = "String", paramType = "query")
    })
	@RequestMapping(value = "/userstop/save", method = RequestMethod.GET)
	public ResponseEntity<WrappedResponse> save(HttpServletRequest request, HttpServletResponse response) {
		UserInfoPOJO userInfo = this.getUserInfo(request,response);
		
		String userplanIdStr = request.getParameter("userplanId");
		if(null == userplanIdStr) return this.error("参数提交有误:userplanId");
		String startTimeStr = request.getParameter("startTime");
		if(null == startTimeStr) return this.error("参数提交有误:startTime");
		String endTimeStr = request.getParameter("endTime");
		if(null == endTimeStr) return this.error("参数提交有误:endTime");
		String stopCauseStr = request.getParameter("stopCause");
		if(null == stopCauseStr) return this.error("参数提交有误:stopCause");
		
		if(StringUtils.isBlank(userplanIdStr)){
			return this.success("请输入班型！！！");
		}
		if(StringUtils.isBlank(startTimeStr)){
			return this.success("请输入休学时间！！！");
		}
		if(StringUtils.isBlank(endTimeStr)){
			return this.success("请输入复课时间！！！");
		}
		if(StringUtils.isBlank(stopCauseStr)){
			return this.success("请输入休学原因！！！");
		}
		Long userplanId = Long.parseLong(userplanIdStr);
		Date startTime = new Date();
		if(null != startTimeStr && !startTimeStr.equals("")){
			startTime = DateUtils.parse(startTimeStr,DateUtils.DATE_HOUR_MIN_PATTERN);
        }
		Date endTime = new Date();
		if(null != endTimeStr && !endTimeStr.equals("")){
			endTime = DateUtils.parse(endTimeStr,DateUtils.DATE_HOUR_MIN_PATTERN);
        }
		Long productId = this.userstopService.queryProductIdByUserplanId(userplanId);
		UserstopEntity userstopEntity = new UserstopEntity();
		userstopEntity.setUserplanId(userplanId);
		userstopEntity.setStartTime(startTime);
		userstopEntity.setEndTime(endTime);
		userstopEntity.setUserId(userInfo.getUserId());
		userstopEntity.setStatus(0);
		userstopEntity.setStopCause(stopCauseStr);

		userstopEntity.setProductId(productId);
		
		this.userstopService.save(userstopEntity);
		
		return this.success("操作成功！！！");
		
	}
	@ApiOperation(value = "班型列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query")
    })
	@RequestMapping(value = "/userstop/classtypeList", method = RequestMethod.GET)
	public ResponseEntity<WrappedResponse> classtypeList(HttpServletRequest request, HttpServletResponse response) {
		UserInfoPOJO userInfo = this.getUserInfo(request,response);
		String businessId = BusinessIdUtils.getBusinessId(request);
		
		List<Long> productIdList = this.userstopService.queryProductId(businessId);
		
		List<Map<String, Object>> classtypeListMap = this.userstopService.queryClasstypeList(userInfo.getUserId(), productIdList);
		
		return this.success(classtypeListMap);
		
	}
}
