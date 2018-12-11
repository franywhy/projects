package com.school.accountant.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/school")
public class SchoolCampusController extends AbstractBaseController{
	
	@RequestMapping("")
	public ModelAndView view(HttpServletRequest request, HttpServletResponse response){
		ModelAndView mav = createModelAndView(request, response);
		mav.setViewName("school/index.ftl");
		return mav;
	}
	
	
	@RequestMapping("/article")
	public ModelAndView article(HttpServletRequest request, HttpServletResponse response){
		String id=request.getParameter("id");
		ModelAndView mav = createModelAndView(request, response);
		mav.setViewName("school/article.ftl");
		mav.addObject("id", id);
		return mav;
	}
	
	
	@RequestMapping("/schoolinfo")
	public ModelAndView schoolinfo(HttpServletRequest request, HttpServletResponse response){
		ModelAndView mav = createModelAndView(request, response);
		mav.setViewName("school/schoolinfo.ftl");
		return mav;
	}
	
}
