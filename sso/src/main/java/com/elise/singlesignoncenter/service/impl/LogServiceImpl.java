package com.elise.singlesignoncenter.service.impl;


import com.elise.singlesignoncenter.dao.LogFirstActivateDao;
import com.elise.singlesignoncenter.dao.LogLoginDao;
import com.elise.singlesignoncenter.dao.LogModifyUserInfoDao;
import com.elise.singlesignoncenter.entity.LogFirstActivateEntity;
import com.elise.singlesignoncenter.entity.LogLoginEntity;
import com.elise.singlesignoncenter.entity.LogModifyUserInfoEntity;
import com.elise.singlesignoncenter.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by Glenn on 2017/4/26 0026.
 */
@Service("LogService")
public class LogServiceImpl implements LogService {


    @Autowired
    private LogLoginDao logLoginDao;

    @Autowired
    private LogFirstActivateDao logFirstActivateDao;

    @Autowired
    private LogModifyUserInfoDao logModifyUserInfoDao;

    @Override
    public void createLoginRecord(String ip, String schoolId, Integer userId) {
        LogLoginEntity entity = new LogLoginEntity(userId,new Date(System.currentTimeMillis()),ip,schoolId);
        logLoginDao.insertLoginLog(entity);
    }

    @Override
    public void refreshFistActivateLog(Integer userId, String businessId) {
        LogFirstActivateEntity entity = new LogFirstActivateEntity(userId,userId,new Date(System.currentTimeMillis()),businessId,1);
        logFirstActivateDao.insertOnLogin(entity);
    }

    @Override
    public void createFistActivateRecord(Integer userId, String businessId) {
        LogFirstActivateEntity entity = new LogFirstActivateEntity(userId,userId,new Date(System.currentTimeMillis()),businessId,0);
        logFirstActivateDao.insertOnRegister(entity);
    }

    @Override
    public void createOpsRecord(Integer userId, String detail, String loginIp, String businessId, String method, String path) {
        Date date = new Date(System.currentTimeMillis());
        LogModifyUserInfoEntity entity = new LogModifyUserInfoEntity(userId,userId,date,detail,loginIp,businessId);
        entity.setMethod(method);
        entity.setPath(path);
        logModifyUserInfoDao.insertModifyLog(entity);
    }


}
