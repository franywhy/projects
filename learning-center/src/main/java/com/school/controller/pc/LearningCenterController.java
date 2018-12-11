package com.school.controller.pc;

import com.school.controller.AbstractBaseController;
import com.school.pojo.UserInfoPOJO;
import com.school.service.MyCourseService;
import com.school.utils.BusinessIdUtils;
import com.school.utils.ClientEnum;
import com.school.web.model.WrappedResponse;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/learningCenter/web")
public class LearningCenterController extends AbstractBaseController {

    @Autowired
    private MyCourseService myCourseService;

    @ApiOperation(value = "返回首页页面")
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView home(HttpServletRequest request, HttpServletResponse response) {
        Long startTime = System.currentTimeMillis();

        UserInfoPOJO userInfo = this.getUserInfo(request, response);

        String businessId = BusinessIdUtils.getBusinessId(request);

        Map<String, Object> result = myCourseService.getCourse(userInfo, businessId, ClientEnum.WEB);

        ModelAndView mav = this.createModelAndView(request, response, true);
        mav.setViewName("learnCenter/curriculum_list");

        mav.addObject("data", result);

        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/web/home " + (endTime - startTime) + "ms");
        if ((endTime - startTime) > 1000) {
            logger.error("/learningCenter/web/home " + (endTime - startTime) + "ms");
        }

        return mav;
    }

    @ApiOperation(value = "获取我的课程列表",
            notes = "courseType 课程类型 : 0:直播课程,1:录播课程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/home/swagger", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getCourseList(HttpServletRequest request, HttpServletResponse response) {
        Long startTime = System.currentTimeMillis();
        UserInfoPOJO userInfo = this.getUserInfo(request, response);

        String businessId = this.getBusinessId(request);

        Map<String, Object> result = myCourseService.getCourse(userInfo, businessId, ClientEnum.WEB);
        if (null != result) {
            return this.success(result);
        }
        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/web/home/swagger " + (endTime - startTime) + "ms");
        if ((endTime - startTime) > 1000) {
            logger.error("/learningCenter/web/home/swagger " + (endTime - startTime) + "ms");
        }
        return this.fail("服务器内部错误", null);
    }
}
