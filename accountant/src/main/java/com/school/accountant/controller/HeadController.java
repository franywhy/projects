package com.school.accountant.controller;

import com.alibaba.fastjson.JSONObject;
import com.school.accountant.entity.ProductType;
import com.school.accountant.service.ProductTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/head/lib")
public class HeadController {

	@Autowired
    public ProductTypeService productTypeService;
	
	@ResponseBody
	@RequestMapping("/producttype")
	public Object productType(String callback) {
		JSONObject returnObj = new JSONObject();
		List<ProductType> productTypes = productTypeService.firstLevel();
    	List<Integer> fatherIds = new ArrayList<Integer>(productTypes.size());
    	for (ProductType productType : productTypes) {
			fatherIds.add(productType.getTypeid());
		}
		returnObj.put("firstLevel", productTypes);
		returnObj.put("secondLevel", productTypeService.secondLevel(fatherIds));
		if(StringUtils.isNotBlank(callback)){
			MappingJacksonValue jacksonValue = new MappingJacksonValue(returnObj.toJSONString());
			jacksonValue.setJsonpFunction(callback);
			return jacksonValue;
		}else{
			return returnObj.toJSONString();
		}
	}
}
