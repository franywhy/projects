package com.hq.learningapi.service;


import com.hq.learningapi.pojo.CheckVersionPOJO;

/**
 * Created by Glenn on 2017/5/4 0004.
 */
public interface VersionService {

    CheckVersionPOJO getVersionPOJO(String schoolId, String clientType, Integer userId, Integer versionCode);

}
