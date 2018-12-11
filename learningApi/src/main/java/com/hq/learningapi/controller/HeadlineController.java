package com.hq.learningapi.controller;

import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import com.hq.learningapi.pojo.HeadlinePOJO;
import com.hq.learningapi.pojo.UserInfoPOJO;
import com.hq.learningapi.service.AppCourseBannerService;
import com.hq.learningapi.service.HeadlineService;
import com.hq.learningapi.service.LikeUserService;
import com.hq.learningapi.util.BusinessIdUtils;
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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/28 0028.
 * @author Evan
 */
@Controller
@RequestMapping("/headline")
public class HeadlineController extends AbstractRestController {

    @Autowired
    private HeadlineService headlineService;
    @Autowired
    private DiscoveryController discoveryController;
    @Autowired
    private LikeUserService likeUserService;
    @Autowired
    private AppCourseBannerService appCourseBannerService;

    @ApiOperation(value = "获取会计头条列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "开始时间 Token", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间 Token", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = false, dataType = "int", defaultValue="1", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", required = false, dataType = "int", defaultValue="10", paramType = "query")
    })
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<List<Map>>> list(HttpServletRequest request) {
        try {
            String token = ServletRequestUtils.getStringParameter(request,"token","");
            String startTime = ServletRequestUtils.getStringParameter(request, "startTime", "");
            String endTime = ServletRequestUtils.getStringParameter(request, "endTime", "");
            int pageNum = ServletRequestUtils.getIntParameter(request, "pageNum", 1);
            int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", 10);
            String userId;
            String businessId;
            if(StringUtils.isNotBlank(token)) {
                UserInfoPOJO userInfo = SSOTokenUtils.getUserInfo(request,token);
                userId = userInfo.getUserId() == null ? null : userInfo.getUserId()+"";
                businessId = SSOTokenUtils.getBussinessId(token);
            } else {
                userId = "visitor";
                businessId = BusinessIdUtils.getBusinessId(request);
            }
            if(StringUtils.isBlank(userId)) {
                return this.error(TransactionStatus.USER_TOKEN_NOT_FOUND);
            }
            if(StringUtils.isBlank(businessId)) {
                return this.error(TransactionStatus.UNKNOWN_BUSINESS_ID);
            }
            String headlinePredicateCode = "return ((Double)map.get(\"msgType\")).intValue()==10;";

            String headlineComparatorCode = "return ((Double)map2.get(\"pushTime\")).compareTo((Double)map1.get(\"pushTime\"));";

            Map<String, Object> map = new HashMap<>();
            map.put("user_id",userId);
            if(StringUtils.isNotBlank(startTime)) {
                map.put("start_time", URLEncoder.encode(startTime,"UTF-8"));
            }
            if(StringUtils.isNotBlank(endTime)) {
                map.put("end_time",URLEncoder.encode(endTime,"UTF-8"));
            }
            map.put("page_num",pageNum);
            map.put("page_size",pageSize);
            map.put("predicate",headlinePredicateCode);
            map.put("comparator",headlineComparatorCode);
            List<Map> list = discoveryController.getMsgRecommend(token,businessId, map);
            if (list == null) {
                list = new ArrayList<>();
            }
            return this.success(list);
        } catch (Throwable t) {
            TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "获取会计头条列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = false, dataType = "int", defaultValue="1", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", required = false, dataType = "int", defaultValue="10", paramType = "query")
    })
    @RequestMapping(value = "/list_V2", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<List<Map>>> list_V2(HttpServletRequest request) {
        try {
            String token = ServletRequestUtils.getStringParameter(request,"token","");
            int pageNum = ServletRequestUtils.getIntParameter(request, "pageNum", 1);
            int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", 10);
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
            map.put("page_num",(pageNum-1)*pageSize);
            map.put("page_size",pageSize);
            map.put("productIdList",productIdList);
            List<Map<String,Object>> list = headlineService.queryMapList(map, token);
            if (list == null) {
                list = new ArrayList<>();
            }
            return this.success(list);
        } catch (Throwable t) {
            TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "获取会计头条详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "headlineId", value = "会计头条ID", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<HeadlinePOJO>> details(HttpServletRequest request) {
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", "");
            Long headlineId = ServletRequestUtils.getLongParameter(request, "headlineId", -1);

            if (headlineId == -1) {
                return this.error("会计头条ID不可为空");
            }
            Long userId = -1L;
            if(StringUtils.isNotBlank(token)) {
                UserInfoPOJO userInfo = SSOTokenUtils.getUserInfo(request,token);
                userId = userInfo.getUserId();
            }
            HeadlinePOJO headline = headlineService.queryPojoObject(headlineId);
            //内容类型  0：视频，1：语音，2：观点，3：文章
            //点赞类型  0：评论，1：观点PK-支持，2：观点PK-反对
            if(headline.getContentType() == 2) {
                headline.setPkSupportIsLike(likeUserService.isLikeUserExist(headlineId,userId,1)? 1:0);
                headline.setPkOpposeIsLike(likeUserService.isLikeUserExist(headlineId,userId,2)? 1:0);
                headline.setPkSupport(likeUserService.queryTotal(headlineId,1));
                headline.setPkOppose(likeUserService.queryTotal(headlineId,2));
                headline.setPkTotalNumber(headline.getPkSupport()+headline.getPkOppose());
            }
            return this.success(headline);
        } catch (Throwable t) {
            TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
