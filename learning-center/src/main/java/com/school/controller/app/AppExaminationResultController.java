package com.school.controller.app;

import com.school.pojo.CourseAbnormalFreeAssessmentPOJO;
import com.school.pojo.CourseAbnormallRegistrationPOJO;
import com.school.pojo.ExaminationResultPOJO;
import com.school.pojo.UserInfoPOJO;
import com.school.service.CourseAbnormallRegistrationService;
import com.school.service.ExaminationResultService;
import com.school.utils.BusinessIdUtils;
import com.school.web.model.WrappedResponse;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.school.controller.AbstractBaseController;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/learningCenter/app/examinationResult")
@Api(description = "成绩登记APP接口")
public class AppExaminationResultController extends AbstractBaseController{
    @Autowired
    private CourseAbnormallRegistrationService courseAbnormallRegistrationService;

    @Autowired
    private ExaminationResultService examinationResultService;

    @ApiOperation(value = "成绩列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
    })
    @ApiResponses({
            @ApiResponse(code=200,message="成功",response=ExaminationResultPOJO.class),
    })
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> list(HttpServletRequest request, HttpServletResponse response) {
        Map<String,Object> map = new HashMap<String,Object>();
        UserInfoPOJO userInfo = this.getUserInfo(request,response);
        String businessId = BusinessIdUtils.getBusinessId(request);
        map.put("userId",userInfo.getUserId());
        map.put("businessId",businessId);
        return this.success(examinationResultService.queryList(map));
    }

    @ApiOperation(value = "报考列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
    })
    @ApiResponses({
            @ApiResponse(code=200,message="成功",response=CourseAbnormallRegistrationPOJO.class),
    })
    @RequestMapping(value = "/registrationList", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> registrationList(HttpServletRequest request, HttpServletResponse response) {
        Map<String,Object> map = new HashMap<String,Object>();
        UserInfoPOJO userInfo = this.getUserInfo(request,response);
        String businessId = BusinessIdUtils.getBusinessId(request);
        map.put("userId",userInfo.getUserId());
        map.put("businessId",businessId);
        return this.success(courseAbnormallRegistrationService.queryExamPojoList(map));
    }

    @ApiOperation(value = "成绩登记")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "registrationId", value = "报考id", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "score", value = "分数", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "img", value = "图片路径", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> save(HttpServletRequest request, HttpServletResponse response) {
        UserInfoPOJO userInfo = this.getUserInfo(request,response);
        Long userId = userInfo.getUserId();
        Long registrationId = Long.parseLong(request.getParameter("registrationId"));
        Integer score = Integer.parseInt(request.getParameter("score"));
        String img = request.getParameter("img");
        examinationResultService.save(userId,registrationId,score,img);
        return this.success();
    }
}
