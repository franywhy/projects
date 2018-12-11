package com.hq.answerapi.service;

import com.hq.answerapi.pojo.UserInfoPOJO;

import java.util.Map;

/**
 * Created by Administrator on 2018/9/20 0020.
 * @author hq
 */
public interface UserService {

    UserInfoPOJO getUserInfoByToken(String token, String schoolId);

    void synUserToMongo(UserInfoPOJO userInfo, String passWord);

    Map<String,Object> mongoCheck(String token, String passWord, String schoolId);

    boolean isTeacher(String mobileNo);

    int getMongoUidByToken(String token);

    String getRealNameByToken(String token);
}
