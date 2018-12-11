package com.school.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.school.dao.LcOffliveLogDao;
import com.school.entity.LcOffliveLogEntity;
import com.school.pojo.LcOffliveLogPOJO;
import com.school.pojo.UserInfoPOJO;
import com.school.service.LcOffliveLogService;
import com.school.service.MessageService;
import com.school.utils.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service("lcOffliveLogService")
public class LcOffliveLogServiceImpl implements LcOffliveLogService {
	@Autowired
	private LcOffliveLogDao lcOffliveLogDao;
    @Autowired
    private MessageService messageService;


    @Override
    public String save(UserInfoPOJO userInfo, String contextJson) {
        /*JSONArray json = JSONArray.parseArray(contextJson);
        if (json != null && !json.isEmpty()){
            for (int i = 0; i < json.size(); i++) {
                LcOffliveLogPOJO pojo = JSONUtil.jsonToBean(json.get(i).toString(), LcOffliveLogPOJO.class);
                LcOffliveLogEntity entity = new LcOffliveLogEntity();
                Date startDate = new Date(pojo.getLookStartTime());
                Date endDate = new Date(pojo.getLookEndTime());
                entity.setUserId(pojo.getUserId());
                entity.setVideoId(pojo.getVideoId());
                entity.setLookStartTime(startDate);
                entity.setLookEndTime(endDate);
                entity.setIsOfflive(pojo.getIsOfflive());
                entity.setVideoStartTime(pojo.getVideoStartTime());
                entity.setVideoEndTime(pojo.getVideoEndTime());
                entity.setVideoTotalTime(pojo.getVideoTotalTime());
                lcOffliveLogDao.save(entity);
            }
        }else {
            return "服务器内部错误,请稍后再试!";
        }*/
        JSONArray json = JSONArray.parseArray(contextJson);
        if (json != null && !json.isEmpty()){
            for (int i = 0; i < json.size(); i++) {
                LcOffliveLogPOJO pojo = JSONUtil.jsonToBean(json.get(i).toString(), LcOffliveLogPOJO.class);
                //通过手机获取用户id
              // Long userId =  lcOffliveLogDao.queryUserIdByMobile(pojo.getUserMobile());
                Map<String,Object> messageMap = new HashMap<String,Object>();
                messageMap.put("videoId", pojo.getVideoId());
                messageMap.put("userId", pojo.getUserId());
                messageMap.put("createTime", new Date().getTime());
                //type:时间的类型 0进入 1退出 2离线(有进入和退出时间)
                messageMap.put("type", 2);
                messageMap.put("joinTime", pojo.getLookStartTime());
                messageMap.put("leaveTime", pojo.getLookEndTime());
                messageMap.put("videoStartTime", pojo.getVideoStartTime());
                messageMap.put("videoEndTime", pojo.getVideoEndTime());
                messageMap.put("videoTotalTime", pojo.getVideoTotalTime());
                //是否离线  0离线(缓存)  1回放回调
                messageMap.put("isOfflive", pojo.getIsOfflive());
                messageService.pushReplayMessageToQueue(messageMap);
            }
        }else {
            return "服务器内部错误,请稍后再试!";
        }
       return null;
    }
}
