package com.hq.learningapi.controller;

import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import com.hq.learningapi.config.LocalConfigEntity;
import com.hq.learningapi.pojo.CourseOlivePOJO;
import com.hq.learningapi.pojo.UserInfoPOJO;
import com.hq.learningapi.service.CourseOliveService;
import com.hq.learningapi.service.MallLiveRoomService;
import com.hq.learningapi.service.SysProductService;
import com.hq.learningapi.service.impl.RedisServiceImpl;
import com.hq.learningapi.util.BusinessIdUtils;
import com.hq.learningapi.util.SSOTokenUtils;
import com.hq.learningapi.util.UrlParameterUtil;
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
@RequestMapping("/courseolive")
public class CourseOliveController extends AbstractRestController {

    @Autowired
    private CourseOliveService courseOliveService;
    @Autowired
    private DiscoveryController discoveryController;
    @Autowired
    private MallLiveRoomService mallLiveRoomService;
    @Autowired
    private LocalConfigEntity localConfigEntity;
    @Autowired
    private RedisServiceImpl redisService;
    @Autowired
    private SysProductService sysProductService;

    private static final String K_VAULE = "k_value:";

    @ApiOperation(value = "获取公开课列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "开始时间 Token", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间 Token", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = false, dataType = "int", defaultValue="1", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", required = false, dataType = "int", defaultValue="10", paramType = "query")
    })
    @RequestMapping(value = "/list_V410", method = RequestMethod.GET)
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
            String courseOlivePredicateCode = "return ((Double)map.get(\"msgType\")).intValue()==11;";

            String courseOliveComparatorCode = "LinkedTreeMap msgData1 = (LinkedTreeMap)map1.get(\"msgData\");" +
                    "                LinkedTreeMap msgData2 = (LinkedTreeMap)map2.get(\"msgData\");" +
                    "                double ss1 = System.currentTimeMillis() - (Double)msgData1.get(\"oliveStartTime\");" +
                    "                double se1 = System.currentTimeMillis() - (Double)msgData1.get(\"oliveEndTime\");" +
                    "                double ss2 = System.currentTimeMillis() - (Double)msgData2.get(\"oliveStartTime\");" +
                    "                double se2 = System.currentTimeMillis() - (Double)msgData2.get(\"oliveEndTime\");" +
                    "                if(ss1 < 0 && ss2 < 0) {" +
                    "                    return ss1 > ss2 ? -1 : 1;" +
                    "                }" +
                    "                else if(se1 > 0 && se2 > 0) {" +
                    "                    return se1 < se2 ? -1 : 1;" +
                    "                }" +
                    "                else if(ss1 > 0 && se1 < 0 && ss2 > 0 && se2 < 0) {" +
                    "                    return ss1 < ss2 ? -1 : 1;" +
                    "                }" +
                    "                else if((ss1 > 0 && se1 < 0) || (ss1 < 0 && se2 > 0)) {" +
                    "                    return -1;" +
                    "                }" +
                    "                else if((ss2 > 0 && se2 < 0) || (ss2 < 0 && se1 > 0)) {" +
                    "                    return 1;" +
                    "                }" +
                    "                return 0;";

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
            map.put("predicate",courseOlivePredicateCode);
            map.put("comparator",courseOliveComparatorCode);
            //TODO
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


    @ApiOperation(value = "获取公开课详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "oliveId", value = "公开课ID", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/details_V410", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<CourseOlivePOJO>> details(HttpServletRequest request) {
        try {
            String token = ServletRequestUtils.getStringParameter(request,"token","");
            Long oliveId = ServletRequestUtils.getLongParameter(request, "oliveId", -1);
            if (oliveId == -1) {
                return this.error("公开课ID不可为空");
            }
            String userId;
            String nickName;
            if(StringUtils.isNotBlank(token)) {
                UserInfoPOJO userInfo = SSOTokenUtils.getUserInfo(request,token);
                userId = userInfo.getUserId() == null ? null : userInfo.getUserId()+"";
                nickName = userInfo.getNickName();
            } else {
                userId = "visitor";
                nickName = "visitor";
            }
            if (StringUtils.isBlank(userId)){
                return this.error(TransactionStatus.USER_TOKEN_NOT_FOUND);
            }

            Map<String,Object> courseMap = courseOliveService.queryPojoObject(oliveId);
            Integer authorityId = (Integer) courseMap.get("authorityId");
            boolean hasAuthority = true;
            if(authorityId != 1000) {
                if("visitor".equals(userId)) {
                    hasAuthority = false;
                } else {
                    hasAuthority = courseOliveService.checkAuthority(authorityId, Long.parseLong(userId));
                }
            }
            /* 通过公开课ID获取直播间 */
            Map<String, Object> liveRoom = mallLiveRoomService.queryByOliveId((Long) courseMap.get("oliveId"));
            /* 若没有可用直播间,返回204 */
            if (null == liveRoom) {
                return this.success(null, TransactionStatus.NO_CONTENT);
            }

            Map<String,Object> sysProduct = sysProductService.queryByProductId((Long) courseMap.get("productId"));
            String genseeDomain = (String) sysProduct.get("genseeDomain");
            String liveId = (String) liveRoom.get("gensee_live_id");
            String livenum = (String) liveRoom.get("gensee_live_num");

            if(hasAuthority) {
                Map<String, Object> result = new HashMap<>();
                result.put("nickname", nickName);
                String k = redisService.buildKValue(userId, K_VAULE, localConfigEntity.getGenseeVerifyAging());
                result.put("k", k);

                String replayUrl = (String) courseMap.get("replayUrl");
                //通过回放视频地址截取回放视频id
                String videoId = null;
                if(StringUtils.isNotBlank(replayUrl)) {
                    if(replayUrl.indexOf("webcast/site/vod/play") != -1) {
                        String[] replayUrlSplit = replayUrl.split("-");
                        videoId = replayUrlSplit[1];
                        replayUrl = UrlParameterUtil.spliceUrl(result, replayUrl);
                    }else if (replayUrl.indexOf("http://my.polyv.net/front/video") != -1){
                        String[] replayUrlSplit = replayUrl.split("=");
                        videoId = replayUrlSplit[1];
                    }else if(replayUrl.indexOf("http://my.polyv.net/front/video") == -1) {
                        replayUrl = localConfigEntity.getOliveReplayUrl() + replayUrl;
                    }
                }
                courseMap.put("videoId",videoId);
                courseMap.put("k",k);
                courseMap.put("replayUrl", replayUrl);
                courseMap.put("url", UrlParameterUtil.spliceUrl(result, localConfigEntity.getGenseeWebcastUrl().replace("{genseeDomain}",genseeDomain) + liveId));
            } else {
                courseMap.put("videoId","");
                courseMap.put("k","");
                courseMap.put("replayUrl", "");
                courseMap.put("url", "");
            }
            courseMap.put("hasAuthority",hasAuthority);
            courseMap.put("userId",userId);
            courseMap.put("nickName",nickName);
            courseMap.put("livenum", livenum);
            courseMap.put("genseeDomainName", genseeDomain);
            courseMap.put("serviceType", localConfigEntity.getGenseeServiceType());

            return this.success(courseMap);
        } catch (Throwable t) {
            TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
