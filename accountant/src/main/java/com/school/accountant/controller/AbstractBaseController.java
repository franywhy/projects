package com.school.accountant.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.ModelAndView;

import com.school.accountant.entity.ProductType;
import com.school.accountant.service.ProductTypeService;

/**
 * 控制器的抽象基类。
 */
public abstract class AbstractBaseController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private static String STATIC_COMMON = "/static";
    
    @Value("${image.domain}")
	private String imageDomain;
    
    @Value("${learningCenterUrl}")
	private String learningCenterUrl;
    
    @Autowired
    public ProductTypeService productTypeService;

    /**
     * 创建一个ModelAndView对象，对象中包含了当前登录用户的数据。
     * @param request 当前HttpServletRequest对象
     */
    protected ModelAndView createModelAndView(HttpServletRequest request, HttpServletResponse response) {
    	
    	ModelAndView mav = new ModelAndView();
    	List<ProductType> productTypes = productTypeService.firstLevel();
    	List<Integer> fatherIds = new ArrayList<Integer>(productTypes.size());
    	for (ProductType productType : productTypes) {
			fatherIds.add(productType.getTypeid());
		}
    	mav.addObject("STATIC_COMMON", STATIC_COMMON);
    	mav.addObject("imageDomain", imageDomain);
    	mav.addObject("firstLevel", productTypes);
    	mav.addObject("secondLevel", productTypeService.secondLevel(fatherIds));
    	mav.addObject("learningCenterUrl", learningCenterUrl);
        return mav;
    }

}
