package com.hq.answerapi.service.impl;

import com.hq.answerapi.dao.VersionDao;
import com.hq.answerapi.entity.VersionEntity;
import com.hq.answerapi.pojo.CheckVersionPOJO;
import com.hq.answerapi.service.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Glenn on 2017/5/4 0004.
 */
@Service("VersionService")
public class VersionServiceImpl implements VersionService {

    @Autowired
    private VersionDao checkVersionDao;

    @Override
    public CheckVersionPOJO getVersionPOJO(String schoolId, String clientType, Integer userId, Integer versionCode) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("clientType", clientType);
        map.put("isActive", 1);
        map.put("schoolId", schoolId);
        map.put("versionCode", versionCode);
        List<VersionEntity> list = checkVersionDao.queryList(map);
        if (list == null || list.size() == 0) {
            return null;
        } else {
            CheckVersionPOJO pojo = new CheckVersionPOJO();
            VersionEntity entity;
            switch (list.size()) {
                //如果检索项数量为1，则默认改项为建议升级
                case 1:
                    entity = list.get(0).getVersionCode() > versionCode ? list.get(0) : null;
                    break;
                case 2:
                    int flag = (list.get(0).getIsGreyUpdate() << 1) ^ list.get(1).getIsGreyUpdate();
                    switch (flag) {
                        case 0:
                        case 3:
                        default:
                            entity = null;
                            break;
                        case 1:
                            if (list.get(1).getVersionCode() > versionCode) {
                                String[] userList = list.get(1).getUpdateUserList().split(",");
                                Boolean isGrey = false;
                                for (String userIdStr : userList) {
                                    if (Long.parseLong(userIdStr) == userId) {
                                        isGrey = true;
                                        break;
                                    }
                                }
                                if (isGrey) {
                                    entity = list.get(1);
                                } else {
                                    entity = list.get(0).getVersionCode() > versionCode ? list.get(0) : null;
                                }
                            } else {
                                entity = null;
                            }
                            break;
                        case 2:
                            if (list.get(0).getVersionCode() > versionCode) {
                                String[] userList = list.get(0).getUpdateUserList().split(",");
                                Boolean isGrey = false;
                                for (String userIdStr : userList) {
                                    if (Long.parseLong(userIdStr) == userId) {
                                        isGrey = true;
                                        break;
                                    }
                                }
                                if (isGrey) {
                                    entity = list.get(0);
                                } else {
                                    entity = list.get(1).getVersionCode() > versionCode ? list.get(0) : null;
                                }
                            } else {
                                entity = null;
                            }
                            break;
                    }
                    break;
                default:
                    entity = null;
                    break;
            }
            if (entity == null) {
                return null;
            }
            pojo.setForceUpdate(entity.getUpdateStrategy() == 0 ? false : true);
            pojo.setVersionCode(entity.getVersionCode());
            pojo.setVersionName(entity.getVersionName());
            pojo.setDownloadUrl(entity.getDownloadUrl());
            pojo.setUpdateDetail(entity.getUpdateDetail());
            pojo.setMd5(entity.getMd5());
            return pojo;
        }
    }
}