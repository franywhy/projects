package com.school.controller;


import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.school.controller.AbstractBaseController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/test")
public class TestController extends AbstractBaseController {
	
	@ApiOperation(value = "获取-我希望 资料预览")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "materialId", value = "资料id", required = true, dataType = "String", paramType = "query")
    })
	@RequestMapping(value = "/test1", method = RequestMethod.GET)
    public ModelAndView getHopeMaterial(HttpServletRequest request, HttpServletResponse response) {
        
        
		ModelAndView mav = this.createModelAndView(request, response, false);
        mav.setViewName("test");
        return mav;
    }
	
	public static void main(String[] args) {
        //获取前月的最后一天
        Calendar cale = Calendar.getInstance();   
        cale.set(Calendar.DAY_OF_MONTH, 0);
        System.out.println(cale.getTime());
        cale.set(Calendar.DAY_OF_MONTH, 1);
        System.out.println(cale.getTime());
	}
	
	public static int funtion(int i, int j){
		return (i+j)*i;
	}
}
