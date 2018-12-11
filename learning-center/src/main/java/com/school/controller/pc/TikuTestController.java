package com.school.controller.pc;

import com.school.controller.AbstractBaseController;
import com.school.entity.MallExpcertificateEntity;
import com.school.pojo.UserInfoPOJO;
import com.school.service.MallExpcertificateService;
import com.school.web.model.WrappedResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 我的证书入口
 * Created by DL on 2017/12/19.
 */
@Controller
@RequestMapping("/learningCenter/web")
public class TikuTestController extends AbstractBaseController {

    @ApiOperation(value = "题库测试页面")
    @RequestMapping(value = "/tikuTest", method = RequestMethod.GET )
    public ModelAndView expcertificatePage(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("learnCenter/tikuTest");
        return mav;
    }


}
