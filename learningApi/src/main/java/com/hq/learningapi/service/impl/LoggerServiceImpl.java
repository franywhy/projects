package com.hq.learningapi.service.impl;

import com.hq.learningapi.dao.LoggerDao;
import com.hq.learningapi.pojo.UserInfoPOJO;
import com.hq.learningapi.service.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: learningapi
 * @description: 日志实现类
 * @author: Irving Wei
 * @create: 2018-09-11 11:24
 **/
@Service
public class LoggerServiceImpl implements LoggerService {
    @Autowired
    private LoggerDao loggerDao;

    @Override
    public void info(int status, String message, UserInfoPOJO userInfo, Date now, String transactionID) {
        Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("status",status);
        parameters.put("message",message);
        parameters.put("user_id",userInfo.getUserId());
        parameters.put("username",userInfo.getNickName());
        parameters.put("mobile",userInfo.getMobileNo());
        parameters.put("now",now);
        parameters.put("transactionID",transactionID);
        loggerDao.loginfo(parameters);
    }
}
