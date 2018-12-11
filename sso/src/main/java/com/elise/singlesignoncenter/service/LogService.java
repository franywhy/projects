package com.elise.singlesignoncenter.service;


/**
 * Created by Glenn on 2017/4/26 0026.
 */

public interface LogService {

    void createLoginRecord(String ip, String schoolId, Integer userId);

    void refreshFistActivateLog(Integer userId,String businessId);

    void createFistActivateRecord(Integer userId, String businessId);

    void createOpsRecord(Integer userId, String detail, String loginIp, String businessId, String method, String path);
}
