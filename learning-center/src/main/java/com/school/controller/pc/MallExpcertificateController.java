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
public class MallExpcertificateController extends AbstractBaseController {

    @Autowired
    private MallExpcertificateService mallExpcertificateService;
    @ApiOperation(value = "我的证书页面")
    @RequestMapping(value = "/expcertificate", method = RequestMethod.GET )
    public ModelAndView expcertificatePage(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = this.createModelAndView(request, response, true);
        UserInfoPOJO userInfo = this.getUserInfo(request,response);
        //获取用户的证书
        MallExpcertificateEntity mallExpcertificateEntity = mallExpcertificateService.queryExpcertificateByMobile(userInfo.getUserId());
        mav.setViewName("learnCenter/expcertificate");
        mav.addObject("data",mallExpcertificateEntity);
        return mav;
    }


    //接口备用
    @ApiOperation(value = "中央财大获取证书接口")
    @RequestMapping(value = "/getExpcertificate", method = RequestMethod.GET )
    public ResponseEntity<WrappedResponse> getExpcertificate(HttpServletRequest request, HttpServletResponse response) {
        String IDCard = request.getParameter("IDCard");
        if(null == IDCard) return this.error("参数提交有误:IDCard");
        String userName = request.getParameter("userName");
        if(null == userName) return this.error("参数提交有误:userName");
        //获取用户的证书
        MallExpcertificateEntity mallExpcertificateEntity = mallExpcertificateService.queryExpcertificateByIDCard(IDCard,userName);
        return this.success(mallExpcertificateEntity);
    }
}
