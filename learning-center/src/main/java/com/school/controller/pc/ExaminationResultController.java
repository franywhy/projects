package com.school.controller.pc;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.school.pojo.CourseAbnormalFreeAssessmentPOJO;
import com.school.pojo.CourseAbnormallRegistrationPOJO;
import com.school.pojo.ExaminationResultPOJO;
import com.school.service.CourseAbnormallRegistrationService;
import com.school.service.ExaminationResultService;
import com.school.utils.BusinessIdUtils;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.school.controller.AbstractBaseController;
import com.school.pojo.UserInfoPOJO;
import com.school.web.model.WrappedResponse;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/learningCenter/web/examinationResult")
@Api(description = "成绩登记pc接口")
public class ExaminationResultController extends AbstractBaseController{

    @Autowired
    private CourseAbnormallRegistrationService courseAbnormallRegistrationService;

    @Autowired
    private ExaminationResultService examinationResultService;

    @ApiOperation(value = "统考成绩页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code=200,message="成功",response=ExaminationResultPOJO.class),
    })
    @RequestMapping(value = "/listPage", method = RequestMethod.GET)
    public ModelAndView listPage(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = this.createModelAndView(request, response, true);
        mav.setViewName("learnCenter/result_list");

        Map<String,Object> map = new HashMap<String,Object>();
        UserInfoPOJO userInfo = this.getUserInfo(request,response);
        String businessId = BusinessIdUtils.getBusinessId(request);
        map.put("userId",userInfo.getUserId());
        map.put("businessId",businessId);
        mav.addObject("data",examinationResultService.queryList(map));
        return mav;
    }

    @ApiOperation(value = "统考登记页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code=200,message="成功",response=CourseAbnormallRegistrationPOJO.class),
    })
    @RequestMapping(value = "/addPage", method = RequestMethod.GET)
    public ModelAndView addPage(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = this.createModelAndView(request, response, true);
        mav.setViewName("learnCenter/result");

        Map<String,Object> map = new HashMap<String,Object>();
        UserInfoPOJO userInfo = this.getUserInfo(request,response);
        String businessId = BusinessIdUtils.getBusinessId(request);
        map.put("userId",userInfo.getUserId());
        map.put("businessId",businessId);
        mav.addObject("data",courseAbnormallRegistrationService.queryExamPojoList(map));
        return mav;
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
