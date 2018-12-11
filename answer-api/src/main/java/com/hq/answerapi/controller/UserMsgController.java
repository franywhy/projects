package com.hq.answerapi.controller;

import com.hq.answerapi.config.LocalConfigEntity;
import com.hq.answerapi.pojo.*;
import com.hq.answerapi.util.JSONUtil;
import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import com.hq.http.HttpConnManager;
import com.hq.http.HttpPlainResult;
import com.hq.http.HttpResultDetail;
import com.hq.http.HttpResultHandler;
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
@RequestMapping("/api/msg")
public class UserMsgController extends AbstractRestController {

    private static  final int TYPE_SYSTEM_LIST = 1;
    private static  final int TYPE_LEARNINGMESSAGE_LIST = 2;
    @Autowired
    private LocalConfigEntity config;

    @Autowired
    private HttpConnManager httpConnManager;

    private String schoolId = null;

    @ApiOperation(value = "获取员工用户系统消息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页数据量", required = false, dataType = "Integer", paramType = "query")
    })
    @RequestMapping(value = "/getSystemMessageByUserId",method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<String>> getSystemMessageByUserId(HttpServletRequest request){
        return this.getMessageByUserId(request,TYPE_SYSTEM_LIST);
    }

    private ResponseEntity<WrappedResponse<String>> getMessageByUserId(HttpServletRequest request,int type){
        try {
            Integer page = ServletRequestUtils.getIntParameter(request, "page", 1);
            Integer size = ServletRequestUtils.getIntParameter(request, "size", 10);
            if(page<1 || size<1){
                return this.error("参数错误!",TransactionStatus.INTERNAL_SERVER_ERROR);
            }
            Integer userId = this.getUserId(request);
            List<MsgContextDetail4LearningTask> list = new ArrayList<>();
            List<MsgContextDetail4LearningTask> list1 = new ArrayList<>();
            List<Map<String,Object>> countList = new ArrayList<>();
            Map<String,Object> dataMap = new HashMap<>();
            int count = 1;

            if (userId != 0L && null != userId){
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("user_id", "teacher");
                HttpPlainResult result = httpConnManager.invoke(HttpMethod.GET, config.getMsgSystemHost()+"/msg/getMessageByUserId", map,"kuaiji_app");
                TRACER.info(result.getResult());
                HttpResultDetail<UserMsgPOJO> entry = HttpResultHandler.handle(result,UserMsgPOJO.class);
                if(entry.isOK()) {
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
                        }
                    }



                    if (type == TYPE_SYSTEM_LIST || type == TYPE_LEARNINGMESSAGE_LIST) {
                        Collections.sort(list, new Comparator<MsgContextDetail4LearningTask>() {
                            @Override
                            public int compare(MsgContextDetail4LearningTask o1, MsgContextDetail4LearningTask o2) {
                                return (int) (o2.getPushTime() - o1.getPushTime());
                            }
                        });
                        //对list进行分页
                        Integer start = (page-1)*size;
                        Integer end = start+size-1;
                        Integer countSize = msgContextList.size();
                        if(start<=countSize){
                            if(end>countSize){
                                end=countSize-1;
                            }
                            for (int i=start;i<=end;i++){
                                list1.add(list.get(i));
                            }
                        }
                        return this.success(list1, TransactionStatus.OK);
                    } else {
                        if (dataMap != null) {
                            for (Map.Entry<String, Object> entrySet : dataMap.entrySet()) {
                                Map<String,Object> resultMap = new HashMap<>();
                                resultMap.put("date", entrySet.getKey());
                                resultMap.put("count", entrySet.getValue());
                                countList.add(resultMap);
                            }
                        }
                        return this.success(countList, TransactionStatus.OK);
                    }
                } else if(entry.isClientError()){
                    return this.error(entry.getResponseMessage(), entry.getResponseStatus());
                }else if(entry.isServerError()){
                    return this.fail(entry.getResponseMessage(), entry.getResponseStatus());
                }
                return this.error(TransactionStatus.INTERNAL_SERVER_ERROR);
            }
            else {
                return this.error("获取用户信息失败!",TransactionStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Throwable t) {
            TRACER.error("\nUnKnown Error", t);
        }
        return this.error(TransactionStatus.INTERNAL_SERVER_ERROR);
    }


    private Integer getUserId (HttpServletRequest request) {
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", "");
            if (StringUtils.isBlank(token)) {
                return 0;
            }
            HashMap<String, Object> map = new HashMap<String, Object>();
            if (token != null) {
                map.put("token", token);
            }
            schoolId = this.getSchoolId(request);
            HttpPlainResult result = httpConnManager.invoke(HttpMethod.GET, config.getSsoHost()+"/inner/userInfo", map,schoolId);
            TRACER.info(result.getResult());
            HttpPlainResult result2 = httpConnManager.invoke(HttpMethod.GET, config.getSsoHost()+"/inner/userMobileNo", map,schoolId);
            TRACER.info(result2.getResult());
            HttpResultDetail<UserInfoPOJO> entry = HttpResultHandler.handle(result,UserInfoPOJO.class);
            HttpResultDetail<UserInfoPOJO> entry2 = HttpResultHandler.handle(result2,UserInfoPOJO.class);
            if(entry.isOK() && entry2.isOK()){
                entry.getResult().setMobileNo(entry2.getResult().getMobileNo());
                return entry.getResult().getUid();
            }else if(entry.isClientError()){
                return 0;
            }else if(entry.isServerError()){
                return 0;
            }
        } catch (Throwable t) {
            TRACER.error("\nUnKnown Error", t);
        }
        return 0;
    }
}
