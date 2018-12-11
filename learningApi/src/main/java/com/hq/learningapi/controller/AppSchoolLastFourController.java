package com.hq.learningapi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hq.learningapi.pojo.AppSchoolPOJO;
import com.hq.learningapi.pojo.UserInfoPOJO;
import com.hq.learningapi.service.AppSchoolLastFourService;
import com.hq.learningapi.util.BusinessIdUtils;
import com.hq.learningapi.util.SSOTokenUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * Created by LiuHai on 2018/01/05
 *
 */

@Controller
@RequestMapping("/api")
public class AppSchoolLastFourController extends AbstractRestController {
	@Autowired
	private AppSchoolLastFourService appSchoolLastFourService;
	
	@ApiOperation(value = "根据给定经纬度获取最近四个校区")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "businessId", value = "业务线id", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "longitude", value = "经度", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "latitude", value = "纬度", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/lastFourSchool", method = RequestMethod.GET)
	public ResponseEntity<WrappedResponse> getLastFourSchool(HttpServletRequest request) {
		try {
			String businessId = ServletRequestUtils.getStringParameter(request, "businessId", "");
			double longitude = Double.valueOf(ServletRequestUtils.getStringParameter(request, "longitude", ""));
			double latitude = Double.valueOf(ServletRequestUtils.getStringParameter(request, "latitude", ""));
			/*if(null != businessId){*/
				List<AppSchoolPOJO> schoolList = this.appSchoolLastFourService.querySchoolList(businessId, longitude, latitude);
				Map<String, Object> resultMap = new HashMap<>();
				if(null != schoolList && schoolList.size() > 0){
					resultMap.put("schoolList", schoolList);
					return this.success(resultMap);
				}else{
					resultMap.put("schoolList", null);
					return this.success(resultMap);
				}
		/*	}*/
			//return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
		} catch (Throwable t) {
			TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
