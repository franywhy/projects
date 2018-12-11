package com.hq.learningapi.controller;

import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import com.hq.learningapi.pojo.AppCourseBannerPOJO;
import com.hq.learningapi.pojo.AppMarketCoursePOJO;
import com.hq.learningapi.pojo.UserInfoPOJO;
import com.hq.learningapi.service.*;
import com.hq.learningapi.util.BusinessIdUtils;
import com.hq.learningapi.util.JSONUtil;
import com.hq.learningapi.util.SSOTokenUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/14 0014.
 * @author EVan
 */
@Controller
@RequestMapping("/api")
public class DiscoveryController2 extends AbstractRestController {

    @Autowired
    private AppCourseBannerService appCourseBannerService;
    @Autowired
    private AppMarketCourseService appMarketCourseService;
    @Autowired
    private AppConfigService appConfigService;
    @Autowired
    private CourseOliveService courseOliveService;
    @Autowired
    private HeadlineService headlineService;

    @ApiOperation(value = "获取发现首页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/discovery", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<Map<String, Object>>> discovery(HttpServletRequest request) {
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", "");
            Long userId;
            String businessId;
            if(StringUtils.isNotBlank(token)) {
                UserInfoPOJO userInfo = SSOTokenUtils.getUserInfo(request,token);
                userId = userInfo.getUserId() == null ? null : userInfo.getUserId();
                businessId = SSOTokenUtils.getBussinessId(token);
            } else {
                userId = 0L;
                businessId = BusinessIdUtils.getBusinessId(request);
            }
            if(null == userId) {
                return this.error(TransactionStatus.USER_TOKEN_NOT_FOUND);
            }
            if(StringUtils.isBlank(businessId)) {
                return this.error(TransactionStatus.UNKNOWN_BUSINESS_ID);
            }
            //产品线
            List<Long> productIdList = appCourseBannerService.queryProductIdListByBisinessId(businessId);
            Map<String, Object> map = new HashMap<>();
            map.put("user_id",userId);
            map.put("page_num",0);
            map.put("page_size",4);
            map.put("productIdList",productIdList);
            
            //公开课列表
            List<Map> courseOliveList = courseOliveService.queryMapList(map, token);
            //banner列表
            List<AppCourseBannerPOJO> bannerList = appCourseBannerService.queryBannerList(productIdList);
            //热门课程
            List<AppMarketCoursePOJO> courseList = appMarketCourseService.queryMostHotCourseList(productIdList);
            //会计头条
            List<Map<String,Object>> headlineList = headlineService.queryMapList(map, token);

            String headlineValue = appConfigService.queryValueByKey(6000L);
            String courseOliveValue = appConfigService.queryValueByKey(6001L);
            String schoolValue = appConfigService.queryValueByKey(6003L);
            String cultureValue = appConfigService.queryValueByKey(6004L);
            String courseValue = appConfigService.queryValueByKey(6002L);

            Map<String,Object> headlineMap = JSONUtil.jsonToMap(headlineValue);
            Map<String,Object> courseOliveMap = JSONUtil.jsonToMap(courseOliveValue);
            Map<String,Object> schoolMap = JSONUtil.jsonToMap(schoolValue);
            Map<String,Object> cultureMap = JSONUtil.jsonToMap(cultureValue);
            Map<String,Object> courseMap = JSONUtil.jsonToMap(courseValue);

            headlineMap.put("headlineList",headlineList);
            courseOliveMap.put("courseOliveList",courseOliveList);
            courseMap.put("courseList",courseList);

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("bannerList",bannerList);
            resultMap.put("headlineMap",headlineMap);
            resultMap.put("courseOliveMap",courseOliveMap);
            resultMap.put("schoolMap",schoolMap);
            resultMap.put("cultureMap",cultureMap);
            resultMap.put("courseMap",courseMap);
            return this.success(resultMap);
        } catch (Throwable t) {
            TRACER.error("\nUnKnown Error", t);
        }
        return this.error(TransactionStatus.INTERNAL_SERVER_ERROR);
    }

}
