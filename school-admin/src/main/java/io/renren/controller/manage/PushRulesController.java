package io.renren.controller.manage;

import com.alibaba.fastjson.JSONObject;
import io.renren.controller.AbstractController;
import io.renren.entity.manage.MessageCard;
import io.renren.service.CourseOliveService;
import io.renren.service.manage.AppMessageService;
import io.renren.service.manage.CustomCardService;
import io.renren.service.manage.HeadlineService;
import io.renren.utils.Constant;
import io.renren.utils.DateUtils;
import io.renren.utils.JSONUtil;
import io.renren.utils.R;
import io.renren.utils.http.HttpClientUtil4_3;
import org.apache.commons.codec.binary.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/12/28 0028.
 * @author Evan
 */
@Controller
@RequestMapping("/pushrules")
public class PushRulesController extends AbstractController {

    @Value("#{application['pom.msg.domain']}")
    private String MSG_DOMAIN;

    private static final String CITYTREE = "cityTree";

    private static final String APPLABELLAYERSHOW = "appLabelLayerShow";

    private static final String SENDUSERMESSAGE_URL = "/msg/sendUserMessage";

    private static final String SENDUSERMESSAGEFIND_URL = "/msg/sendUserMessageFind";

    @Autowired
    private CourseOliveService courseOliveService;

    @Autowired
    private CustomCardService customCardService;

    @Autowired
    private HeadlineService headlineService;

    @Autowired
    private AppMessageService appMessageService;

    /**
     * 推送 or 更新推送
     * @param reqJson
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/push")
    public R list(@RequestBody JSONObject reqJson, HttpServletRequest request){
        int messageType = reqJson.getIntValue("messageType");
        Long messageId = reqJson.getLongValue("messageId");
        List<String> groupChannelList = new ArrayList<>(Arrays.asList(reqJson.getJSONArray("channelIds").toArray(new String[0])));
        Date pushTime = reqJson.getDate("pushTime");
        String msgRecommend = reqJson.getString("msgRecommend");
        boolean isVisitor = reqJson.getBoolean("isVisitor");
        String rules = reqJson.getString("rules");
        List<String> channelsJsonList = new ArrayList<>();
        if(isVisitor) {
            channelsJsonList.add("visitor");
        }
        List<String> groupChannelFindList = new ArrayList<>(groupChannelList);
        try{
            String message = appMessageService.isExistSavePushTime(pushTime);
            if(message != null ){
                return R.error(message);
            }
            if(Constant.Message.COURSEOLIVE.getValue() == messageType) {
                if(CITYTREE.equals(rules) || APPLABELLAYERSHOW.equals(rules)) {
                    groupChannelFindList.add("public");
                }
                return pushCourseOlive(messageId, channelsJsonList, groupChannelList, groupChannelFindList, pushTime, msgRecommend);
            } else if (Constant.Message.CUSTOMCARD.getValue() == messageType) {
                return pushCustomCard(messageId, channelsJsonList, groupChannelList, pushTime);
            } else if(Constant.Message.APPMESSAGE.getValue() == messageType) {
                return pushAppMessage(messageId, channelsJsonList, groupChannelList, pushTime);
            } else if (Constant.Message.HEADLINE_FIND.getValue() == messageType) {
                return pushHeadline(messageId, channelsJsonList, groupChannelFindList, pushTime, msgRecommend);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return R.error("服务器内部错误");
        }
        return R.error("messageType is unknown");
    }

    /**
     * 删除推送
     * @return
     */
    @ResponseBody
    @RequestMapping("/delMessage")
    public R delMessage(@RequestBody JSONObject reqJson){
        int messageType = reqJson.getIntValue("messageType");
        Long messageId = reqJson.getLongValue("messageId");
        String pushMsgId = reqJson.getString("pushMsgId");
        String pushFindMsgId = reqJson.getString("pushFindMsgId");

        if(StringUtils.isBlank(pushMsgId) && StringUtils.isBlank(pushFindMsgId)) {
            return R.error("消息ID不能为空");
        }
        Map<String,String> params = new HashMap<>();
        String result = "";
        try {
            if(StringUtils.isNotBlank(pushMsgId)) {
                params.put("msg_id", pushMsgId);
                result = HttpClientUtil4_3.sendHttpPost(MSG_DOMAIN+"/msg/delMessage", params);
            }
            if(StringUtils.isNotBlank(pushFindMsgId)) {
                params.put("msg_id", pushFindMsgId);
                result = HttpClientUtil4_3.sendHttpPost(MSG_DOMAIN+"/msg/delMessage", params);
            }
            if(StringUtils.isNotBlank(result)) {
                JSONObject object = JSONObject.parseObject(result);
                if(200 == object.getIntValue("code")) {
                    if(Constant.Message.COURSEOLIVE.getValue() == messageType) {
                        courseOliveService.updatePushStatus(messageId, Constant.Push.NOT.getValue(), null, null, null, 0);
                    } else if (Constant.Message.CUSTOMCARD.getValue() == messageType) {
                        customCardService.updatePushStatus(messageId, Constant.Push.NOT.getValue(), null, null);
                    } else if (Constant.Message.APPMESSAGE.getValue() == messageType) {
                        appMessageService.updatePushStatus(messageId, Constant.Push.NOT.getValue(), null, null);
                    } else if (Constant.Message.HEADLINE_FIND.getValue() == messageType) {
                        headlineService.updatePushStatus(messageId, Constant.Push.NOT.getValue(), null, null, 0);
                    }
                    return R.ok(object.getString("message"));
                }
                return R.error(object.getString("message"));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return R.error("服务器内部错误");
        }
        return R.error("操作失败");
    }

    /**
     * 更新推荐状态
     * @return
     */
    @ResponseBody
    @RequestMapping("/updateMsgRecommend")
    public R updateMsgRecommend(@RequestBody JSONObject reqJson){
        int messageType = reqJson.getIntValue("messageType");
        Long messageId = reqJson.getLongValue("messageId");
        String pushFindMsgId = reqJson.getString("pushFindMsgId");
        String msgRecommend = reqJson.getString("msgRecommend");

        if(StringUtils.isBlank(pushFindMsgId)) {
            return R.error("消息ID不能为空");
        }
        try {
            MessageCard msgCardFind = null;
            if(Constant.Message.COURSEOLIVE_FIND.getValue() == messageType) {
                Map<String, Object> map = courseOliveService.queryMsgMap(messageId);
                map.put("oliveStartTime",((Date)map.get("oliveStartTime")).getTime());
                map.put("oliveEndTime",((Date)map.get("oliveEndTime")).getTime());
                map.put("appStatus",Integer.parseInt(msgRecommend));
                msgCardFind = getCourseoLiveFindMessageCard(map, ((Date)map.get("pushTime")).getTime());
            } else if (Constant.Message.HEADLINE_FIND.getValue() == messageType) {
                Map<String, Object> map = headlineService.queryMsgMap(messageId);
                map.put("appStatus",Integer.parseInt(msgRecommend));
                msgCardFind = getHeadlineFindMessageCard(map, ((Date)map.get("pushTime")).getTime());
            }
            Map<String,String> params = new HashMap<>();
            params.put("msg_id", pushFindMsgId);
            params.put("msg", JSONUtil.objToGson(msgCardFind));
            String result = HttpClientUtil4_3.sendHttpPost(MSG_DOMAIN+"/msg/updateMessage", params);
            if(StringUtils.isNotBlank(result)) {
                JSONObject object = JSONObject.parseObject(result);
                if(200 == object.getIntValue("code")) {
                    if(Constant.Message.COURSEOLIVE_FIND.getValue() == messageType) {
                        courseOliveService.updateAppStatus(new Long[]{messageId}, Integer.parseInt(msgRecommend));
                    } else if (Constant.Message.HEADLINE_FIND.getValue() == messageType) {
                        headlineService.updateAppStatus(new Long[]{messageId}, Integer.parseInt(msgRecommend));
                    }
                    return R.ok(object.getString("message"));
                }
                return R.error(object.getString("message"));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return R.error("服务器内部错误");
        }
        return R.error("操作失败");
    }

    /**
     * 推送系统消息
     * @param messageId
     * @param pushTime
     * @return
     */
    private R pushAppMessage(Long messageId, List<String> channelsJsonList, List<String> groupChannelList, Date pushTime) throws Exception {
        Map<String, Object> map = appMessageService.queryMsgMap(messageId);
        MessageCard msgCard = new MessageCard();
        msgCard.setMsgId(map.get("pushMsgId") == null ? null : map.get("pushMsgId").toString());
        msgCard.setMsgType(Constant.Message.APPMESSAGE.getValue());
        msgCard.setTitle(map.get("title")+"");
        msgCard.setDescribe(map.get("title")+"");
        String pushText =  map.get("contentHtml")+"";
        msgCard.setPushText(pushText);
        map.remove("contentHtml");
        msgCard.setMsgData(map);
        msgCard.setPushTime(pushTime.getTime());

        Map<String,String> result = sendUpdateMessage(msgCard,channelsJsonList,groupChannelList,SENDUSERMESSAGE_URL);
        if (null == result) {
            return R.error("操作失败");
        } else {
            if("200".equals(result.get("code"))) {
                appMessageService.updatePushStatus(messageId, Constant.Push.SUCCESS.getValue(), pushTime, result.get("pushMsgId"));
                return R.ok(result.get("message"));
            }
            return R.error(result.get("message"));
        }
    }

    /**
     * 推送自定义卡片
     * @param messageId
     * @param pushTime
     * @return
     */
    private R pushCustomCard(Long messageId, List<String> channelsJsonList, List<String> groupChannelList, Date pushTime) throws Exception {
        Map<String, Object> map = customCardService.queryMsgMap(messageId);
        MessageCard msgCard = new MessageCard();
        msgCard.setMsgId(map.get("pushMsgId") == null ? null : map.get("pushMsgId").toString());
        msgCard.setMsgType(Constant.Message.CUSTOMCARD.getValue());
        msgCard.setTitle(map.get("title")+"");
        msgCard.setDescribe(map.get("subtitle")+"");
        msgCard.setPushText(map.get("title")+"");
        msgCard.setMsgData(map);
        msgCard.setPushTime(pushTime.getTime());

        Map<String,String> result = sendUpdateMessage(msgCard,channelsJsonList,groupChannelList,SENDUSERMESSAGE_URL);
        if (null == result) {
            return R.error("操作失败");
        } else {
            if("200".equals(result.get("code"))) {
                customCardService.updatePushStatus(messageId, Constant.Push.SUCCESS.getValue(), pushTime, result.get("pushMsgId"));
                return R.ok(result.get("message"));
            }
            return R.error(result.get("message"));
        }
    }

    /**
     * 推送公开课
     * @param messageId
     * @param pushTime
     * @return
     */
    private R pushCourseOlive(Long messageId, List<String> channelsJsonList, List<String> groupChannelList, List<String> groupChannelFindList, Date pushTime, String msgRecommend) throws Exception {
        Map<String, Object> map = courseOliveService.queryMsgMap(messageId);
        map.put("oliveStartTime",((Date)map.get("oliveStartTime")).getTime());
        map.put("oliveEndTime",((Date)map.get("oliveEndTime")).getTime());
        map.put("appStatus",Integer.parseInt(msgRecommend));
        MessageCard msgCard = getCourseoLiveMessageCard(map, pushTime.getTime());
        MessageCard msgCardFind = getCourseoLiveFindMessageCard(map, pushTime.getTime());

        String message = "";
        Map<String,String> resultFind;
        Map<String,String> result = sendUpdateMessage(msgCard, channelsJsonList, groupChannelList, SENDUSERMESSAGE_URL);
        if (null == result) {
            return R.error("操作失败");
        } else {
            if("200".equals(result.get("code"))) {
                resultFind = sendUpdateMessage(msgCardFind, channelsJsonList, groupChannelFindList, SENDUSERMESSAGEFIND_URL);
                if (null == resultFind) {
                    message = "操作失败，请及时补推";
                } else if(!"200".equals(resultFind.get("code"))) {
                    message = resultFind.get("message") + "，请及时补推";
                }
            } else {
                return R.error(result.get("message"));
            }
        }

        if(StringUtils.isBlank(message)) {
            courseOliveService.updatePushStatus(messageId, Constant.Push.SUCCESS.getValue(), pushTime, result.get("pushMsgId"), resultFind.get("pushMsgId"), Integer.parseInt(msgRecommend));
            return R.ok(resultFind.get("message"));
        }
        //卡片推送回滚
        Map<String, String> param = new HashMap<>(1);
        param.put("msg_id", result.get("pushMsgId"));
        String delResult = HttpClientUtil4_3.sendHttpPost(MSG_DOMAIN+"/msg/delMessage", param);
        if(StringUtils.isNotBlank(delResult)) {
            JSONObject object = JSONObject.parseObject(delResult);
            if (200 != object.getIntValue("code")) {
                courseOliveService.updatePushStatus(messageId, Constant.Push.FAILURE.getValue(), null, result.get("pushMsgId"), null, 0);
                return R.ok("卡片推送成功："+object.getString("message")+"<br>发现推送失败："+message);
            }
        }
        return R.error(message);
    }

    /**
     * 推送会计头条
     * @param messageId
     * @param pushTime
     * @return
     */
    private R pushHeadline(Long messageId, List<String> channelsJsonList, List<String> groupChannelList, Date pushTime, String msgRecommend) throws Exception {
        Map<String, Object> map = headlineService.queryMsgMap(messageId);
        map.put("appStatus",Integer.parseInt(msgRecommend));
        MessageCard msgCardFind = getHeadlineFindMessageCard(map, pushTime.getTime());

        Map<String,String> result = sendUpdateMessage(msgCardFind,channelsJsonList,groupChannelList,SENDUSERMESSAGEFIND_URL);
        if (null == result) {
            return R.error("操作失败");
        } else {
            if("200".equals(result.get("code"))) {
                headlineService.updatePushStatus(messageId, Constant.Push.SUCCESS.getValue(), pushTime, result.get("pushMsgId"), Integer.parseInt(msgRecommend));
                return R.ok(result.get("message"));
            }
            return R.error(result.get("message"));
        }
    }

    /**
     * 发送/更新推送消息
     * @param msgCard
     * @return
     */
    private Map<String,String> sendUpdateMessage(MessageCard msgCard, List<String> channelsJsonList, List<String> groupChannelList, String url) throws Exception {
        Map<String,String> params = new HashMap<>();
        if(null != channelsJsonList && channelsJsonList.size() > 0) {
            String channelsJson = channelsJsonList.get(0);
            for (int i=1; i<channelsJsonList.size(); i++) {
                channelsJson += "," + channelsJsonList.get(i);
            }
            params.put("channels_json", channelsJson);
        }
        if(null != groupChannelList && groupChannelList.size() > 0) {
            String groupChannels = groupChannelList.get(0);
            for (int i=1; i<groupChannelList.size(); i++) {
                groupChannels += "," + groupChannelList.get(i);
            }
            params.put("group_channels", groupChannels);
        }
        params.put("send_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(msgCard.getPushTime()));
        params.put("msg", JSONUtil.objToGson(msgCard));

        String pushMsgId = msgCard.getMsgId();
        if(StringUtils.isBlank(pushMsgId)) {
            params.put("send_person", getUser().getNickName());
            params.put("title", msgCard.getTitle());
            params.put("content", msgCard.getDescribe());
        } else {
            params.put("msg_id",pushMsgId);
            url = "/msg/updateMessage";
        }

        String result = HttpClientUtil4_3.sendHttpPost(MSG_DOMAIN+url, params);
        logger.info(MSG_DOMAIN+url+" MsgSystem result: ",result);
        if(StringUtils.isNotBlank(result)) {
            Map<String, String> map = new HashMap<>();
            JSONObject object = JSONObject.parseObject(result);
            int code = object.getIntValue("code");
            if(200 == code && StringUtils.isBlank(pushMsgId)) {
                pushMsgId = object.getJSONObject("data").getString("msg_id");
            }
            map.put("code",code+"");
            map.put("message",object.getString("message"));
            map.put("pushMsgId",pushMsgId);
            return map;
        }
        return null;
    }

    private MessageCard getCourseoLiveMessageCard(Map<String, Object> map, Long pushTime) {
        MessageCard msgCard = new MessageCard();
        msgCard.setMsgId(map.get("pushMsgId") == null ? null : map.get("pushMsgId").toString());
        msgCard.setMsgType(Constant.Message.COURSEOLIVE.getValue());
        msgCard.setTitle(map.get("cardTitle")+"");
        msgCard.setDescribe(map.get("cardSubtitle")+"");
        msgCard.setPushText(map.get("oliveTitle") + "，这节直播课一定对你帮助不小，点我即可立即观看！");
        msgCard.setMsgData(map);
        msgCard.setPushTime(pushTime);
        return msgCard;
    }

    private MessageCard getCourseoLiveFindMessageCard(Map<String, Object> map, Long pushTime) {
        MessageCard msgCard = new MessageCard();
        msgCard.setMsgId(map.get("pushFindMsgId") == null ? null : map.get("pushFindMsgId").toString());
        msgCard.setMsgType(Constant.Message.COURSEOLIVE_FIND.getValue());
        msgCard.setTitle(map.get("cardTitle")+"");
        msgCard.setDescribe(map.get("cardSubtitle")+"");
        msgCard.setPushText(map.get("oliveTitle") + "，这节直播课一定对你帮助不小，点我即可立即观看！");
        msgCard.setMsgData(map);
        msgCard.setPushTime(pushTime);
        return msgCard;
    }

    private MessageCard getHeadlineFindMessageCard(Map<String, Object> map, Long pushTime) {
        MessageCard msgCard = new MessageCard();
        msgCard.setMsgId(map.get("pushFindMsgId") == null ? null : map.get("pushFindMsgId").toString());
        msgCard.setMsgType(Constant.Message.HEADLINE_FIND.getValue());
        msgCard.setTitle(map.get("title")+"");
        msgCard.setDescribe(map.get("subtitle")+"");
        msgCard.setPushText(map.get("title")+"");
        msgCard.setMsgData(map);
        msgCard.setPushTime(pushTime);
        return msgCard;
    }
}
