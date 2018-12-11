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

import com.hq.learningapi.pojo.AppCourseBannerPOJO;
import com.hq.learningapi.pojo.UserInfoPOJO;
import com.hq.learningapi.service.AppCourseBannerService;
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
public class AppCourseBannerController extends AbstractRestController {
	@Autowired
	private AppCourseBannerService appCourseBannerService;
	
	@ApiOperation(value = "获取课程Banner")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "businessId", value = "业务线id", required = true, dataType = "String", paramType = "query")
	})
    @RequestMapping(value = "/courseBanner", method = RequestMethod.GET)
	public ResponseEntity<WrappedResponse> getCourseBanner(HttpServletRequest request) {
		try {
			String businessId = ServletRequestUtils.getStringParameter(request, "businessId", "");
			if(null != businessId){
				List<Long> productIdList = this.appCourseBannerService.queryProductIdListByBisinessId(businessId);
				List<AppCourseBannerPOJO> bannerList = this.appCourseBannerService.queryBannerList(productIdList);
				Map<String, Object> resultMap = new HashMap<>();
				if(null != bannerList && bannerList.size() > 0){
					resultMap.put("bannerList", bannerList);
					return this.success(resultMap);
				}else{
					resultMap.put("bannerList", null);
					return this.success(resultMap);
				}
			}
			return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
		} catch (Throwable t) {
			TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
