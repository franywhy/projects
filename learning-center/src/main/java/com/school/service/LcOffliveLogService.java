package com.school.service;


import com.school.pojo.UserInfoPOJO;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-04-13 10:48:24
 */
public interface LcOffliveLogService {

    //保存
    String save(UserInfoPOJO userInfo, String contextJson);
}
