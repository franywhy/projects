package com.hq.learningcenter.school.controller.pc;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hq.learningcenter.school.common.annotation.LcLog;
import com.hq.learningcenter.school.controller.AbstractBaseController;
import com.hq.learningcenter.school.pojo.UserInfoPOJO;
import com.hq.learningcenter.school.service.LiveService;
import com.hq.learningcenter.school.service.ProductService;
import com.hq.learningcenter.school.web.model.WrappedResponse;
import com.hq.learningcenter.utils.LogEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/learningCenter/app")
public class LiveController extends AbstractBaseController {
	
	@Autowired
	private LiveService liveService;
	
	@Autowired
	private ProductService productService;
	
	@LcLog(value="获取直播间直播地址:/live/liveRoom", type= LogEnum.LIVE)
	@ApiOperation(value = "获取直播间直播地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "classplanLiveId", value = "排课计划明细id", required = true, dataType = "String", paramType = "query")
    })
	@RequestMapping(value = "/live/liveRoom", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getLiveRoom(String classplanLiveId, HttpServletRequest request, HttpServletResponse response) {
		Long startTime = System.currentTimeMillis();
		UserInfoPOJO userInfo = this.getUserInfo(request,response);
        
        //String classplanLiveId = request.getParameter("classplanLiveId");
        if(null == classplanLiveId) return this.error("参数提交有误:classplanLiveId");       
        
        /*调起即构桌面程序的判断
         * String businessId = BusinessIdUtils.getBusinessId(request);
         * SysProductEntity product =productService.queryByclassplanLiveId(classplanLiveId);
         *Map<String,Object> result = null;
		 *if ("".equals(product) || null == product){
		 *	result = liveService.queryLiveUrl(classplanLiveId, userInfo);
		 *} 
		 *else {
		 *	if (product.getType() == 2) {
		 *		result = liveService.getZegoLiveUrl(classplanLiveId, userInfo, businessId);
		 *	} else {
		 *		result = liveService.queryLiveUrl(classplanLiveId, userInfo);
		 *	}
		 *}
         */
        Map<String,Object> result = null;
        result = liveService.queryLiveUrl(classplanLiveId, userInfo);
        if(null != result){
        	return this.success(result);
        }
		Long endTime = System.currentTimeMillis();
		logger.info("/learningCenter/app/live/liveRoom " + (endTime - startTime) + "ms");
		if ((endTime - startTime) > 1000) {
			logger.error("/learningCenter/app/live/liveRoom " + (endTime - startTime) + "ms");
		}
        return this.fail("服务器内部错误", null);
    }
	
	@LcLog(value="获取直播间回放地址:/live/replay", type=LogEnum.REPLAY)
	@ApiOperation(value = "获取直播间回放地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "classplanLiveId", value = "排课计划明细id", required = true, dataType = "String", paramType = "query")
    })
	@RequestMapping(value = "/live/replay", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getReplayUrl(String classplanLiveId, HttpServletRequest request, HttpServletResponse response) {
		Long startTime = System.currentTimeMillis();
		UserInfoPOJO userInfo = this.getUserInfo(request,response);
        
        //String classplanLiveId = request.getParameter("classplanLiveId");
        if(null == classplanLiveId) return this.error("参数提交有误:classplanLiveId");
        
        Map<String,Object> result = liveService.queryReplayUrl(classplanLiveId,userInfo);
        if(null != result){
        	return this.success(result);
        }
		Long endTime = System.currentTimeMillis();
		logger.info("/learningCenter/app/live//replay" + (endTime - startTime) + "ms");
		if ((endTime - startTime) > 1000) {
			logger.error("/learningCenter/app/live/replay " + (endTime - startTime) + "ms");
		}
        return this.fail("服务器内部错误", null);
    }
}
