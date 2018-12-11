package com.hq.learningcenter.school.controller.app;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hq.learningcenter.school.controller.AbstractBaseController;
import com.hq.learningcenter.utils.ClientEnum;
import com.hq.learningcenter.utils.BusinessIdUtils;
import com.hq.learningcenter.school.pojo.UserInfoPOJO;
import com.hq.learningcenter.school.service.MyCourseService;
import com.hq.learningcenter.school.web.model.WrappedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/learningCenter/app")
public class AppLearningCenterController extends AbstractBaseController {
	
	@Autowired
	private MyCourseService myCourseService;
	
	@ApiOperation(value = "获取我的课程列表", 
			notes = "courseType 课程类型 : 0:直播课程,1:录播课程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query")
    })
	@RequestMapping(value = "/getCourseList", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getCourseList(HttpServletRequest request, HttpServletResponse response) {
		Long startTime = System.currentTimeMillis();

        UserInfoPOJO userInfo = this.getUserInfo(request,response);
        
        String businessId = BusinessIdUtils.getBusinessId(request);
        
        Map<String,Object> result = myCourseService.getCourse(userInfo, businessId, ClientEnum.APP);
        
        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/app/getCourseList " + (endTime - startTime) + "ms");
        if ((endTime - startTime) > 1000) {
            logger.error("/learningCenter/app/getCourseList " + (endTime - startTime) + "ms");
        }
        
        if(null != result){
        	return this.success(result);
        }
        return this.fail("服务器内部错误", null);
    }
}
