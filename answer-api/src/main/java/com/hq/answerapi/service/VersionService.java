package com.hq.answerapi.service;


import com.hq.answerapi.pojo.CheckVersionPOJO;

/**
 * Created by Glenn on 2017/5/4 0004.
 */
public interface VersionService {

    CheckVersionPOJO getVersionPOJO(String schoolId, String clientType, Integer userId, Integer versionCode);

}
