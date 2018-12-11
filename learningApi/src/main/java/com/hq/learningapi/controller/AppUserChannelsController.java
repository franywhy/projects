package com.hq.learningapi.controller;

import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import com.hq.learningapi.pojo.UserInfoPOJO;
import com.hq.learningapi.service.AppUserChannelsService;
import com.hq.learningapi.util.SSOTokenUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LiuHai on 2017/12/29
 */

@Controller
@RequestMapping("/api")
public class AppUserChannelsController extends AbstractRestController {

    @Autowired
    private AppUserChannelsService appUserChannelsService;

    @ApiOperation(value = "获取用户频道")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/userChannels", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getUserChannels(HttpServletRequest request) {
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", "");
            TRACER.info(String.format("\nRequest Token:%s", token));


            Map<String, Object> resultMap = new HashMap<>();
            List<String> channelIdList = new ArrayList<>();
            if(null != token && !"".equals(token)){
            	UserInfoPOJO userInfo = SSOTokenUtils.getUserInfo(request, token);
                Long userId = userInfo.getUserId();
                if(null != userId && userId > 0){
                	channelIdList = this.appUserChannelsService.queryChannelIdListByUserId(userId);
                	
                	resultMap.put("userId", userId);
            		resultMap.put("channelIds", channelIdList);
            		return this.success(resultMap);
                }else{
                	resultMap.put("userId", 0);
                	channelIdList.add("visitor");
                	resultMap.put("channelIds", channelIdList);
            		return this.success(resultMap);
                }
            }else{
            	resultMap.put("userId", 0);
            	channelIdList.add("visitor");
            	resultMap.put("channelIds", channelIdList);
        		return this.success(resultMap);

            }
            
            
        } catch (Throwable t) {
            TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
}
