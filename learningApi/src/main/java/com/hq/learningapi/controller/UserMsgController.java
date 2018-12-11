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
import com.hq.learningapi.util.DateUtils;
import com.hq.learningapi.util.JSONUtil;
import com.hq.learningapi.util.SSOTokenUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.*;

/**
 * 请求消息系统获取用户消息相关
 * Created by DL on 2018/1/16.
 */
@Controller
@RequestMapping("/api")
public class UserMsgController extends AbstractRestController {

    private static final int TYPE_SYSTEM_LIST = 1;
    private static final int TYPE_LEARNINGMESSAGE_LIST = 2;
    private static final int TYPE_SYSTEM_COUNT = 3;
    private static final int TYPE_LEARNINGMESSAGE_COUNT = 4;
    @Autowired
    private LocalConfigEntity config;

    @Autowired
    private HttpConnManager httpConnManager;

    private String schoolId = null;


    @ApiOperation(value = "获取单个消息详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "msgId", value = "消息id msgId", required = true, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/getMessageByMsgId", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<String>> getMessageByMsgId(HttpServletRequest request) {
        try {
            String msgId = ServletRequestUtils.getStringParameter(request, "msgId", "");
            Long userId = this.getUserId(request);
            if (userId != 0L && null != userId) {
                HashMap<String, Object> map = new HashMap<String, Object>();
//              map.put("user_id", "11014935");
                map.put("msg_id", msgId);
                HttpPlainResult result = httpConnManager.invoke(HttpMethod.GET, config.getMsgSystemHost() + "/msg/getMessage", map, schoolId);
                TRACER.info(result.getResult());
                HttpResultDetail<SingleMsgPOJO> entry = HttpResultHandler.handle(result, SingleMsgPOJO.class);
                if (entry.isOK()) {
                    MsgContextDetail4TodayLearning msgContextDetail4TodayLearning = JSONUtil.jsonToBean(entry.getResult().getMessage().replaceAll("\\\\", ""), MsgContextDetail4TodayLearning.class);
                    msgContextDetail4TodayLearning.setMsgId(entry.getResult().getCode());
                    return this.success(msgContextDetail4TodayLearning, TransactionStatus.OK);
                } else if (entry.isClientError()) {
                    return this.error(entry.getResponseMessage(), entry.getResponseStatus());
                } else if (entry.isServerError()) {
                    return this.fail(entry.getResponseMessage(), entry.getResponseStatus());
                }
                return this.error(TransactionStatus.INTERNAL_SERVER_ERROR);
            } else {
                return this.fail("获取用户信息失败!", TransactionStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Throwable t) {
            TRACER.error("\nUnKnown Error", t);
        }
        return this.error(TransactionStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "获取用户未读消息总数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "开始时间 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间 Token", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/getNotReadMessagesCountByUserId", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<String>> getNotReadMessagesCountByUserId(HttpServletRequest request) {
        try {
            String startTime = ServletRequestUtils.getStringParameter(request, "startTime", "");
            String endTime = ServletRequestUtils.getStringParameter(request, "endTime", "");
            Long userId = this.getUserId(request);
            if (userId != 0L && null != userId) {
                HashMap<String, Object> map = new HashMap<String, Object>();
//                    map.put("user_id", "11014935");
                map.put("user_id", userId);
                map.put("start_time", URLEncoder.encode(startTime, "UTF-8"));
                map.put("end_time", URLEncoder.encode(endTime, "UTF-8"));
                HttpPlainResult result = httpConnManager.invoke(HttpMethod.GET, config.getMsgSystemHost() + "/msg/getNotReadMessagesCountByUserId", map, schoolId);
                TRACER.info(result.getResult());
                HttpResultDetail<UserNotReadMsgCountPOJO> entry = HttpResultHandler.handle(result, UserNotReadMsgCountPOJO.class);
                if (entry.isOK()) {
                    return this.success(entry.getResult(), TransactionStatus.OK);
                } else if (entry.isClientError()) {
                    return this.error(entry.getResponseMessage(), entry.getResponseStatus());
                } else if (entry.isServerError()) {
                    return this.fail(entry.getResponseMessage(), entry.getResponseStatus());
                }
                return this.error(TransactionStatus.INTERNAL_SERVER_ERROR);
            } else {
                return this.fail("获取用户信息失败!", TransactionStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Throwable t) {
            TRACER.error("\nUnKnown Error", t);
        }
        return this.error(TransactionStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "设置用户消息为已读")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "msgId", value = "消息id msgId", required = true, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/setMessageReaded", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<String>> setMessageReaded(HttpServletRequest request) {
        try {
            String msgId = ServletRequestUtils.getStringParameter(request, "msgId", "");
            Long userId = this.getUserId(request);
            if (StringUtils.isBlank(msgId)) {
                return this.error("消息msgId  不能为空");
            }
            if (userId != 0L && null != userId) {
                HashMap<String, Object> map = new HashMap<String, Object>();
//              map.put("user_id", "11014935");
                map.put("user_id", userId);
                map.put("msg_id", msgId);
                HttpPlainResult result = httpConnManager.invoke(HttpMethod.POST, config.getMsgSystemHost() + "/msg/setMessageReaded", map, schoolId);
                TRACER.info(result.getResult());
                HttpResultDetail<String> entry = HttpResultHandler.handle(result, String.class);
                if (entry.isOK()) {
                    return this.success(entry.getResult(), TransactionStatus.OK);
                } else if (entry.isClientError()) {
                    return this.error(entry.getResponseMessage(), entry.getResponseStatus());
                } else if (entry.isServerError()) {
                    return this.fail(entry.getResponseMessage(), entry.getResponseStatus());
                }
                return this.error(TransactionStatus.INTERNAL_SERVER_ERROR);
            } else {
                return this.error("获取用户信息失败!", TransactionStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Throwable t) {
            TRACER.error("\nUnKnown Error", t.getMessage());
        }
        return this.error(TransactionStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "获取用户系统消息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "开始时间 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间 Token", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/getSystemMessageByUserId", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<String>> getSystemMessageByUserId(HttpServletRequest request) {
        return this.getMessageByUserId(request, TYPE_SYSTEM_LIST);
    }

    @ApiOperation(value = "获取用户学习任务消息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "开始时间 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间 Token", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/getLearningMessageByUserId", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<String>> getLearningMessageByUserId(HttpServletRequest request) {
        return this.getMessageByUserId(request, TYPE_LEARNINGMESSAGE_LIST);
    }

    @ApiOperation(value = "获取每天学习任务消息的数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "开始时间 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间 Token", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/getLearningMessageCount", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<String>> getLearningMessageCount(HttpServletRequest request) {
        return this.getMessageByUserId(request, TYPE_LEARNINGMESSAGE_COUNT);
    }

    @ApiOperation(value = "获取每天系统消息的数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "开始时间 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间 Token", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/getSystemMessageCount", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<String>> getSystemMessageCount(HttpServletRequest request) {
        return this.getMessageByUserId(request, TYPE_SYSTEM_COUNT);
    }


    @ApiOperation(value = "获取今日学习消息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "开始时间 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间 Token", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/getTodayMessageByUserId", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<String>> getTodayMessageByUserId(HttpServletRequest request) {
        try {
            String startTime = ServletRequestUtils.getStringParameter(request, "startTime", "");
            String endTime = ServletRequestUtils.getStringParameter(request, "endTime", "");
            String token = ServletRequestUtils.getStringParameter(request, "token", "");
            Long userId = this.getUserId(request);
            List<MsgContextDetail4TodayLearning> list = new ArrayList<>();

            if (userId != 0L && null != userId) {
                HashMap<String, Object> map = new HashMap<String, Object>();
//              map.put("user_id", "10905013");
                map.put("user_id", userId);
                map.put("start_time", URLEncoder.encode(startTime, "UTF-8"));
                map.put("end_time", URLEncoder.encode(endTime, "UTF-8"));
                HttpPlainResult result = httpConnManager.invoke(HttpMethod.GET, config.getMsgSystemHost() + "/msg/getMessageByUserId", map, schoolId);
                TRACER.info(result.getResult());
                HttpResultDetail<UserMsgPOJO> entry = HttpResultHandler.handle(result, UserMsgPOJO.class);
                if (entry.isOK()) {
                    List<UserMsgContextPOJO> msgContextList = entry.getResult().getList();
                    if (msgContextList.size() > 0) {
                        for (UserMsgContextPOJO pojo : msgContextList) {
                            ReceiveMsgContextDetailPOJO receiveMsgContextDetailPOJO = JSONUtil.jsonToBean(pojo.getMsg().replaceAll("\\\\", ""), ReceiveMsgContextDetailPOJO.class);
                            MsgContextDetail4TodayLearning msgContextDetail4TodayLearning = new MsgContextDetail4TodayLearning();
                            if (receiveMsgContextDetailPOJO.getMsgType() != 9) {
                                if (receiveMsgContextDetailPOJO.getMsgType() == 3) {
                                    Map<String, Object> msgData = receiveMsgContextDetailPOJO.getMsgData();
                                    msgData.put("detailsUrl", config.getApph5Url() + "/course?oliveId=" + msgData.get("oliveId") + "&token=" + token);
                                }
                                //今日学习卡片
                                if (receiveMsgContextDetailPOJO.getMsgType() == 1) {
                                    //获取课堂资料的第一个文件地址
                                    Map<String, Object> msgData = receiveMsgContextDetailPOJO.getMsgData();
                                    String coursewareValue = (String) msgData.get("courseware");
                                    if (null != coursewareValue && "" != coursewareValue) {
                                        String[] coursewareUrl = coursewareValue.split(",");
                                        String courseware = coursewareUrl[0];
                                        msgData.put("courseware", courseware);
                                    }
                                    //获取本期预习的第一个文件地址
                                    String prepareValue = (String) msgData.get("prepare");
                                    if (null != prepareValue && "" != prepareValue) {
                                        String[] prepareUrl = prepareValue.split(",");
                                        String prepare = prepareUrl[0];
                                        msgData.put("prepare", prepare);
                                    }
                                    //获取上期复习的第一个文件地址
                                    String reviewValue = (String) msgData.get("review");
                                    if (null != reviewValue && "" != reviewValue) {
                                        String[] reviewUrl = reviewValue.split(",");
                                        String review = reviewUrl[0];
                                        msgData.put("review", review);
                                    }
                                }

                                msgContextDetail4TodayLearning.setMsgId(pojo.getMsg_id());
                                msgContextDetail4TodayLearning.setIsReaded(pojo.getIs_readed());
                                msgContextDetail4TodayLearning.setTitle(receiveMsgContextDetailPOJO.getTitle());
                                msgContextDetail4TodayLearning.setMsgData(receiveMsgContextDetailPOJO.getMsgData());
                                msgContextDetail4TodayLearning.setDescribe(receiveMsgContextDetailPOJO.getDescribe());
                                msgContextDetail4TodayLearning.setMsgType(receiveMsgContextDetailPOJO.getMsgType());
                                msgContextDetail4TodayLearning.setPushTime(receiveMsgContextDetailPOJO.getPushTime());
                                list.add(msgContextDetail4TodayLearning);
                            }
                        }
                    }
                    Collections.sort(list, new Comparator<MsgContextDetail4TodayLearning>() {
                        @Override
                        public int compare(MsgContextDetail4TodayLearning o1, MsgContextDetail4TodayLearning o2) {
                            return (int) (o2.getPushTime() - o1.getPushTime());
                        }
                    });
                    return this.success(list, TransactionStatus.OK);
                } else if (entry.isClientError()) {
                    return this.error(entry.getResponseMessage(), entry.getResponseStatus());
                } else if (entry.isServerError()) {
                    return this.fail(entry.getResponseMessage(), entry.getResponseStatus());
                }
                return this.error(TransactionStatus.INTERNAL_SERVER_ERROR);
            } else {
                return this.error("获取用户信息失败!", TransactionStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Throwable t) {
            TRACER.error("\nUnKnown Error", t);
        }
        return this.error(TransactionStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "统计用户每天消息数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "开始时间 Token", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间 Token", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/getDayMessageCount", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<String>> getDayMessageCount(HttpServletRequest request) {
        try {
            String startTime = ServletRequestUtils.getStringParameter(request, "startTime", "");
            String endTime = ServletRequestUtils.getStringParameter(request, "endTime", "");
            Long userId = this.getUserId(request);
            if (userId != 0L && null != userId) {
                HashMap<String, Object> map = new HashMap<String, Object>();
//              map.put("user_id", "10010294");
                map.put("user_id", userId);
                map.put("start_time", URLEncoder.encode(startTime, "UTF-8"));
                map.put("end_time", URLEncoder.encode(endTime, "UTF-8"));
                HttpPlainResult result = httpConnManager.invoke(HttpMethod.GET, config.getMsgSystemHost() + "/msg/getMessageByUserIdGroup", map, schoolId);
                TRACER.info(result.getResult());
                HttpResultDetail<ReceiceCountP0JO> entry = HttpResultHandler.handle(result, ReceiceCountP0JO.class);
                if (entry.isOK()) {
                    return this.success(entry.getResult(), TransactionStatus.OK);
                } else if (entry.isClientError()) {
                    return this.error(entry.getResponseMessage(), entry.getResponseStatus());
                } else if (entry.isServerError()) {
                    return this.fail(entry.getResponseMessage(), entry.getResponseStatus());
                }
                return this.error(TransactionStatus.INTERNAL_SERVER_ERROR);
            } else {
                return this.error("获取用户信息失败!", TransactionStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Throwable t) {
            TRACER.error("\nUnKnown Error", t);
        }
        return this.error(TransactionStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<WrappedResponse<String>> getMessageByUserId(HttpServletRequest request, int type) {
        try {
            String startTime = ServletRequestUtils.getStringParameter(request, "startTime", "");
            String endTime = ServletRequestUtils.getStringParameter(request, "endTime", "");
            Long userId = this.getUserId(request);
            List<MsgContextDetail4LearningTask> list = new ArrayList<>();
            List<Map<String, Object>> countList = new ArrayList<>();
            Map<String, Object> dataMap = new HashMap<>();
            int count = 1;

            if (userId != 0L && null != userId) {
                HashMap<String, Object> map = new HashMap<String, Object>();
//                    map.put("user_id", "10905013");
                map.put("user_id", userId);
                map.put("start_time", URLEncoder.encode(startTime, "UTF-8"));
                map.put("end_time", URLEncoder.encode(endTime, "UTF-8"));
                HttpPlainResult result = httpConnManager.invoke(HttpMethod.GET, config.getMsgSystemHost() + "/msg/getMessageByUserId", map, schoolId);
                TRACER.info(result.getResult());
                HttpResultDetail<UserMsgPOJO> entry = HttpResultHandler.handle(result, UserMsgPOJO.class);
                if (entry.isOK()) {
                    List<UserMsgContextPOJO> msgContextList = entry.getResult().getList();
                    if (msgContextList.size() > 0) {
                        for (UserMsgContextPOJO pojo : msgContextList) {
                            ReceiveMsgContextDetailPOJO receiveMsgContextDetailPOJO = JSONUtil.jsonToBean(pojo.getMsg().replaceAll("\\\\", ""), ReceiveMsgContextDetailPOJO.class);
                            MsgContextDetail4LearningTask msgContextDetail4LearningTask = new MsgContextDetail4LearningTask();
                            msgContextDetail4LearningTask.setMsgId(pojo.getMsg_id());
                            msgContextDetail4LearningTask.setIsReaded(pojo.getIs_readed());
                            msgContextDetail4LearningTask.setPushText(receiveMsgContextDetailPOJO.getPushText());
                            msgContextDetail4LearningTask.setTitle(receiveMsgContextDetailPOJO.getTitle());
                            msgContextDetail4LearningTask.setDescribe(receiveMsgContextDetailPOJO.getDescribe());
                            msgContextDetail4LearningTask.setMsgType(receiveMsgContextDetailPOJO.getMsgType());
                            msgContextDetail4LearningTask.setPushTime(receiveMsgContextDetailPOJO.getPushTime());
                            //判断是不是系统消息
                            if (type == TYPE_SYSTEM_LIST) {
                                if (receiveMsgContextDetailPOJO.getMsgType() == 9) {
                                    list.add(msgContextDetail4LearningTask);
                                }
                            }
                            if (type == TYPE_LEARNINGMESSAGE_LIST) {
                                if (msgContextDetail4LearningTask.getMsgType() != 9) {
                                    list.add(msgContextDetail4LearningTask);
                                }
                            }
                            if (type == TYPE_SYSTEM_COUNT) {
                                if (receiveMsgContextDetailPOJO.getMsgType() == 9) {
                                    String dateStr = DateUtils.getDateString4LongTime(receiveMsgContextDetailPOJO.getPushTime());
                                    Integer num = (Integer) dataMap.get(dateStr);
                                    if (num != null) {
                                        dataMap.put(dateStr, ++num);
                                    } else {
                                        dataMap.put(dateStr, count);
                                    }
                                }
                            }
                            if (type == TYPE_LEARNINGMESSAGE_COUNT) {
                                if (msgContextDetail4LearningTask.getMsgType() != 9) {
                                    String dateStr = DateUtils.getDateString4LongTime(receiveMsgContextDetailPOJO.getPushTime());
                                    Integer num = (Integer) dataMap.get(dateStr);
                                    if (num != null) {
                                        dataMap.put(dateStr, ++num);
                                    } else {
                                        dataMap.put(dateStr, count);
                                    }
                                }
                            }
                        }
                    }
                    if (type == TYPE_SYSTEM_LIST || type == TYPE_LEARNINGMESSAGE_LIST) {
                        Collections.sort(list, new Comparator<MsgContextDetail4LearningTask>() {
                            @Override
                            public int compare(MsgContextDetail4LearningTask o1, MsgContextDetail4LearningTask o2) {
                                return (int) (o2.getPushTime() - o1.getPushTime());
                            }
                        });
                        return this.success(list, TransactionStatus.OK);
                    } else {
                        if (dataMap != null) {
                            for (Map.Entry<String, Object> entrySet : dataMap.entrySet()) {
                                Map<String, Object> resultMap = new HashMap<>();
                                resultMap.put("date", entrySet.getKey());
                                resultMap.put("count", entrySet.getValue());
                                countList.add(resultMap);
                            }
                        }
                        return this.success(countList, TransactionStatus.OK);
                    }
                } else if (entry.isClientError()) {
                    return this.error(entry.getResponseMessage(), entry.getResponseStatus());
                } else if (entry.isServerError()) {
                    return this.fail(entry.getResponseMessage(), entry.getResponseStatus());
                }
                return this.error(TransactionStatus.INTERNAL_SERVER_ERROR);
            } else {
                return this.error("获取用户信息失败!", TransactionStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Throwable t) {
            TRACER.error("\nUnKnown Error", t);
        }
        return this.error(TransactionStatus.INTERNAL_SERVER_ERROR);
    }


    private Long getUserId(HttpServletRequest request) {
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", "");
            if (StringUtils.isBlank(token)) {
                return 0L;
            }
            HashMap<String, Object> map = new HashMap<String, Object>();
            if (token != null) {
                map.put("token", token);
            }
            schoolId = this.getSchoolId(request);
            UserInfoPOJO userInfo = SSOTokenUtils.getUserInfo(request, token);
            if (userInfo != null) {
                return userInfo.getUserId();
            }
            /*HttpPlainResult result = httpConnManager.invoke(HttpMethod.GET, config.getSsoHost()+"/inner/userInfo", map,schoolId);
            TRACER.info(result.getResult());
            HttpResultDetail<UserInfoPOJO> entry = HttpResultHandler.handle(result,UserInfoPOJO.class);
            if(entry.isOK()) {
                userId = Long.valueOf(entry.getResult().getUserId());
                return OKSTERING;
            } else if(entry.isClientError()){
                return entry.getResponseMessage();
            }else if(entry.isServerError()){
                return entry.getResponseMessage();
            }
            return "\nUnKnown Error";
            */
        } catch (Throwable t) {
            TRACER.error("\nUnKnown Error", t);
        }
        return 0L;
    }
}
