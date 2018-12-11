package com.school.controller.app;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.school.dao.MallClassDao;
import com.school.dao.SysUserDao;
import com.school.service.MyCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
@RequestMapping("/learningCenter/app")
public class AppUserstopController extends AbstractBaseController {
	@Autowired
	private UserstopService userstopService;
    @Autowired
    private MyCourseService myCourseService;
	
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
		
		if(null != userstopList) return this.success(userstopList);
		
		return this.fail("服务器内部错误", null);
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
		
		return this.success();
		
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
		
		if(null != classtypeListMap) return this.success(classtypeListMap);
		
		return this.fail("服务器内部错误", null);
	}

	@ApiOperation(value = "获取右上角点击文案_v410")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query")
    })
	@RequestMapping(value = "/contextList", method = RequestMethod.GET)
	public ResponseEntity<WrappedResponse> contextList(HttpServletRequest request, HttpServletResponse response) {
		UserInfoPOJO userInfo = this.getUserInfo(request,response);
        Map<String,String> userStopMap = new HashMap<>();
        Map<String,String> teacherMap = new HashMap<>();
        List<Map<String,String>> contextList = new ArrayList<>();
        //前端根据type类型确认是跳H5还是选择原生态  h5不用升级,现在0和1都是跳原生,有可能接口会升级
        userStopMap.put("pushText","休学");
        userStopMap.put("type","0");
        teacherMap.put("pushText","联系老师");
        teacherMap.put("type","1");
        contextList.add(userStopMap);
        contextList.add(teacherMap);
        return this.success(contextList);
	}

	@ApiOperation(value = "联系老师_v410")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userplanId", value = "学员规划id", required = true, dataType = "String", paramType = "query"),
    })
	@RequestMapping(value = "/contactTeacher", method = RequestMethod.GET)
	public ResponseEntity<WrappedResponse> contactTeacher(HttpServletRequest request, HttpServletResponse response) {
        String businessId = BusinessIdUtils.getBusinessId(request);
		UserInfoPOJO userInfo = this.getUserInfo(request,response);
        String userplanIdStr = request.getParameter("userplanId");
        if(null == userplanIdStr) return this.error("参数提交有误:userplanId");
        Long userplanId = Long.parseLong(userplanIdStr);
        //联系班主任
        String wxCode = myCourseService.getWxCode(userplanId,businessId);
        Map<String,String> teacherMap = new HashMap<>();
        if(null != wxCode && !wxCode.equals("")){
            teacherMap.put("contactTeacher",wxCode);
        }else{
            teacherMap.put("contactTeacher",null);
        }
        return this.success(teacherMap);
	}
}
