package com.hq.learningapi.controller;

import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import com.hq.http.HttpConnManager;
import com.hq.http.HttpPlainResult;
import com.hq.http.HttpResultDetail;
import com.hq.http.HttpResultHandler;
import com.hq.learningapi.config.LocalConfigEntity;
import com.hq.learningapi.pojo.*;
import com.hq.learningapi.service.*;
import com.hq.learningapi.util.BusinessIdUtils;
import com.hq.learningapi.util.JSONUtil;
import com.hq.learningapi.util.SSOTokenUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/14 0014.
 *
 * @author EVan
 */
@Controller
@RequestMapping("/api")
public class DiscoveryController extends AbstractRestController {

    @Autowired
    private LocalConfigEntity config;
    @Autowired
    private HttpConnManager httpConnManager;
    @Autowired
    private AppCourseBannerService appCourseBannerService;
    @Autowired
    private AppMarketCourseService appMarketCourseService;
    @Autowired
    private AppConfigService appConfigService;
    @Autowired
    private LikeUserService likeUserService;
    @Autowired
    private HeadlineService headlineService;
    @Autowired
    private CourseOliveService courseOliveService;

    @ApiOperation(value = "获取发现首页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/discovery_V410", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<Map<String, Object>>> discovery(HttpServletRequest request) {
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", "");
            String userId;
            String businessId;
            if (StringUtils.isNotBlank(token)) {
                UserInfoPOJO userInfo = SSOTokenUtils.getUserInfo(request, token);
                userId = userInfo.getUserId() == null ? null : userInfo.getUserId() + "";
                businessId = SSOTokenUtils.getBussinessId(token);
            } else {
                userId = "visitor";
                businessId = BusinessIdUtils.getBusinessId(request);
            }
            if (StringUtils.isBlank(userId)) {
                return this.error(TransactionStatus.USER_TOKEN_NOT_FOUND);
            }
            if (StringUtils.isBlank(businessId)) {
                return this.error(TransactionStatus.UNKNOWN_BUSINESS_ID);
            }
            String courseOlivePredicateCode = "return ((Double)map.get(\"msgType\")).intValue()==11;";

            String headlinePredicateCode = "return ((Double)map.get(\"msgType\")).intValue()==10;";

            String comparatorCode = "LinkedTreeMap msgData1 = (LinkedTreeMap)map1.get(\"msgData\");" +
                    "                        LinkedTreeMap msgData2 = (LinkedTreeMap)map2.get(\"msgData\");" +
                    "                        int app1 = ((Double)msgData1.get(\"appStatus\")).intValue();" +
                    "                        int app2 = ((Double)msgData2.get(\"appStatus\")).intValue();" +
                    "                        if(app1 != app2) {" +
                    "                            return app1 > app2 ? -1 : 1;" +
                    "                        } else {" +
                    "                            return ((Double)map2.get(\"pushTime\")).compareTo((Double)map1.get(\"pushTime\"));" +
                    "                        }";

            Map<String, Object> map = new HashMap<>();

            map.put("user_id", userId);
            map.put("page_num", 1);
            map.put("page_size", 4);
            map.put("comparator", comparatorCode);

            map.put("predicate", courseOlivePredicateCode);
            /************************** 公开课--消息系统获取--start ******************************/
            //List<Map> courseOliveList = getMsgRecommend(token,businessId, map);
            /************************** 公开课--消息系统获取--end ******************************/

            List<Long> productIdList = appCourseBannerService.queryProductIdListByBisinessId(businessId);
            List<AppCourseBannerPOJO> bannerList = appCourseBannerService.queryBannerList(productIdList);

            /************************** 公开课--接口获取--start ******************************/
            Map<String, Object> courseParam = new HashMap<>();
            courseParam.put("user_id", userId);
            courseParam.put("page_num", 0);
            courseParam.put("page_size", 4);
            courseParam.put("productIdList", productIdList);
            //公开课列表
            List<Map> courseOliveList = courseOliveService.queryMapList(courseParam, token);
            /**************************** 公开课--接口获取--end ****************************/

            List<Long> productList = appMarketCourseService.queryProductIdListByBisinessId(businessId);
            List<AppMarketCoursePOJO> courseList = appMarketCourseService.queryMostHotCourseList(productList);

            map.put("predicate", headlinePredicateCode);
            List<Map> headlineList = getMsgRecommend(token, businessId, map);

            String headlineValue = appConfigService.queryValueByKey(6000L);
            String courseOliveValue = appConfigService.queryValueByKey(6001L);
            String courseValue = appConfigService.queryValueByKey(6002L);
            String schoolValue = appConfigService.queryValueByKey(6003L);
            String cultureValue = appConfigService.queryValueByKey(6004L);

            Map<String, Object> headlineMap = JSONUtil.jsonToMap(headlineValue);
            Map<String, Object> courseOliveMap = JSONUtil.jsonToMap(courseOliveValue);
            Map<String, Object> courseMap = JSONUtil.jsonToMap(courseValue);
            Map<String, Object> schoolMap = JSONUtil.jsonToMap(schoolValue);
            Map<String, Object> cultureMap = JSONUtil.jsonToMap(cultureValue);

            headlineMap.put("headlineList", headlineList);
            courseOliveMap.put("courseOliveList", courseOliveList);
            courseMap.put("courseList", courseList);

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("bannerList", bannerList);
            resultMap.put("headlineMap", headlineMap);
            resultMap.put("courseOliveMap", courseOliveMap);
            resultMap.put("courseMap", courseMap);
            resultMap.put("schoolMap", schoolMap);
            resultMap.put("cultureMap", cultureMap);
            return this.success(resultMap);
        } catch (Throwable t) {
            TRACER.error("\nUnKnown Error", t);
        }
        return this.error(TransactionStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 获取推荐消息
     *
     * @return
     */
    public List<Map> getMsgRecommend(String token, String businessId, Map<String, Object> map) throws URISyntaxException, IOException {
        List<Map> list = new ArrayList<>();
        HttpPlainResult result = httpConnManager.invoke(HttpMethod.POST, config.getMsgSystemHost() + "/msg/getMessageByUserIdFind", map, businessId);
        HttpResultDetail<UserMsgPagePOJO> entry = HttpResultHandler.handle(result, UserMsgPagePOJO.class);
        if (entry.isOK()) {
            List<UserMsgContextPOJO> msgContextList = entry.getResult().getList();
            if (msgContextList != null && msgContextList.size() > 0) {
                for (UserMsgContextPOJO pojo : msgContextList) {
                    ReceiveMsgContextDetailPOJO receiveMsgContextDetailPOJO = JSONUtil.jsonToBean(pojo.getMsg().replaceAll("\\\\", ""), ReceiveMsgContextDetailPOJO.class);
                    Map<String, Object> msgData = receiveMsgContextDetailPOJO.getMsgData();
                    //会计头条
                    if (receiveMsgContextDetailPOJO.getMsgType() == 10) {
                        //内容类型  0：视频，1：语音，2：观点，3：文章
                        String detailsUrl = null;
                        Long pkTotalNumber = 0L;
                        Integer readNumber = 0;
                        Integer commentNumber = 0;
                        Long headlineId = Long.parseLong(msgData.get("headlineId").toString());
                        HeadlinePOJO headline = headlineService.queryPojoObjectForNumber(headlineId);
                        if (headline != null) {
                            readNumber = headline.getReadNumber();
                            commentNumber = headline.getCommentNumber();
                            detailsUrl = "/entry?headlineId=" + headlineId + "&token=" + token;
                            pkTotalNumber = likeUserService.queryTotal(headlineId, -1);
                        }
                        /*switch ((int)msgData.get("contentType")) {
                            case 0:
                                detailsUrl="/opinion?headlineId="+headlineId+"&token="+token;
                                break;
                            case 1:detailsUrl="/opinion?headlineId="+headlineId+"&token="+token;
                                break;
                            case 2:
                                detailsUrl="/opinion?headlineId="+headlineId+"&token="+token;
                                pkTotalNumber = likeUserService.queryTotal(headlineId, -1);
                                break;
                            case 3:detailsUrl="/opinion?headlineId="+headlineId+"&token="+token;
                                break;
                            default:detailsUrl="";pkTotalNumber = 0L;
                        }*/
                        double hours = (double) (System.currentTimeMillis() - receiveMsgContextDetailPOJO.getPushTime()) / 3600 / 1000;
                        if (hours < 1) {
                            msgData.put("pushTime", (int) (hours * 60) + "分钟前");
                        } else if (hours < 24) {
                            msgData.put("pushTime", (int) hours + "小时前");
                        } else {
                            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
                            msgData.put("pushTime", sdf.format(receiveMsgContextDetailPOJO.getPushTime()));
                        }
                        msgData.put("detailsUrl", config.getApph5Url() + detailsUrl);
                        msgData.put("pkTotalNumber", pkTotalNumber);
                        msgData.put("readNumber", readNumber);
                        msgData.put("commentNumber", commentNumber);
                    }
                    //公开课
                    else if (receiveMsgContextDetailPOJO.getMsgType() == 11) {
                        msgData.put("pushTime", receiveMsgContextDetailPOJO.getPushTime());
                        msgData.put("detailsUrl", config.getApph5Url() + "/course?oliveId=" + msgData.get("oliveId") + "&token=" + token);
                        msgData.remove("pushMsgId");
                    }
                    msgData.remove("pushFindMsgId");
                    list.add(msgData);
                }
            }
        }
        return list;
    }
}
