package com.school.controller.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.school.controller.AbstractBaseController;
import com.school.service.LiveCourseService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
public class AppMaterialPreviewController extends AbstractBaseController {
	
	@Autowired
	private LiveCourseService liveCourseService;
	
	@ApiOperation(value = "获取-我希望-资料预览-预览内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "detailId", value = "资料id", required = true, dataType = "String", paramType = "query")
    })
	@RequestMapping(value = "/material/preview", method = RequestMethod.GET)
    public ModelAndView getMaterialContent(HttpServletRequest request, HttpServletResponse response) {
        
        String detailIdStr = request.getParameter("detailId");
        
        Long detailId = Long.parseLong(detailIdStr);
        String content = liveCourseService.getMaterialContent(detailId);
        
        ModelAndView mav = new ModelAndView();//this.createModelAndView(request, response, true);
        mav.setViewName("publicfile/database");
        
        if(null != content){
        	mav.addObject("content", content);
        }else{
        	mav.addObject("content", "");
        }
        return mav;
    }
}
