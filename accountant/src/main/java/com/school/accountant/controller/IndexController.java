package com.school.accountant.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.school.accountant.entity.NewsAdvert;
import com.school.accountant.service.NewsAdvertService;

@Controller
public class IndexController extends AbstractBaseController {
	
	private final Logger logger = LoggerFactory.getLogger(IndexController.class);
	
	@Autowired
	private NewsAdvertService webBannerService;
	
	@RequestMapping({"/","/index"})
	public ModelAndView view(HttpServletRequest request, HttpServletResponse response){
		int advertposition = 796;
		// banner
		List<NewsAdvert> bannerList = webBannerService.bannerList(advertposition);
		
		ModelAndView mav = createModelAndView(request, response);
		mav.setViewName("home/index.ftl");
		
		mav.addObject("banners", bannerList);
		return mav;
	}
	
}
