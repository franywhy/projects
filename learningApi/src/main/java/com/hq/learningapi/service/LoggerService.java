package com.hq.learningapi.service;

import com.hq.learningapi.pojo.UserInfoPOJO;

import java.util.Date;

public interface LoggerService {
    void info(int status, String message, UserInfoPOJO userInfo, Date now, String transactionID);
}
