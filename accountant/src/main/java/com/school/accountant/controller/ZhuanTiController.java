package com.school.accountant.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/zhuanti")
public class ZhuanTiController {
	
	private final Logger logger = LoggerFactory.getLogger(ZhuanTiController.class);
	
	@RequestMapping("/2016ry")
	public String ry2016(HttpServletRequest request, Model model){
		return "zhuanti/2016ry/index.html";
	}
	
	@RequestMapping("/2017cjms")
	public String cjms2017(HttpServletRequest request, Model model){
		return "zhuanti/2017cjms/index.html";
	}
	
	@RequestMapping("/2017lcjh")
	public String lcjh2017(HttpServletRequest request, Model model){
		return "zhuanti/2017lcjh/index.html";
	}
	
	@RequestMapping("/2017zjkj")
	public String zjkj2017(HttpServletRequest request, Model model){
		return "zhuanti/2017zjkj/index.html";
	}
	
	@RequestMapping("/2017zjzc")
	public String zjzc2017(HttpServletRequest request, Model model){
		return "zhuanti/2017zjzc/index.html";
	}
	
	@RequestMapping("/cjkj")
	public String cjkj(HttpServletRequest request, Model model){
		return "zhuanti/cjkj/index.html";
	}
	
	@RequestMapping("/cpa")
	public String cpa(HttpServletRequest request, Model model){
		return "zhuanti/cpa/cpa.html";
	}

	@RequestMapping("/cyzg")
	public String cyzg(HttpServletRequest request, Model model){
		return "zhuanti/cyzg/index.html";
	}

	@RequestMapping("/fudao")
	public String fudao(HttpServletRequest request, Model model){
		return "zhuanti/fudao/index.html";
	}

	@RequestMapping("/fzsw")
	public String fzsw(HttpServletRequest request, Model model){
		return "zhuanti/fzsw/index.html";
	}

	@RequestMapping("/jlr")
	public String jlr(HttpServletRequest request, Model model){
		return "zhuanti/jlr/index.html";
	}

	@RequestMapping("/v9kj")
	public String v9kj(HttpServletRequest request, Model model){
		return "zhuanti/v9kj/index.html";
	}

	@RequestMapping("/xdkj")
	public String xdkj(HttpServletRequest request, Model model){
		return "zhuanti/xdkj/index.html";
	}
	
}
