package com.hq.learningapi.service.impl;

import com.hq.learningapi.dao.SysConfigDao;
import com.hq.learningapi.service.SysConfigService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DL on 2018/9/18.
 */
@Service("sysConfigService")
public class SysConfigServiceImpl implements SysConfigService {

    @Autowired
    private SysConfigDao sysConfigDao;

    @Override
    public String getValue(String key, String defaultValue) {
        String value = sysConfigDao.queryByKey(key);
        if(StringUtils.isBlank(value)){
            return defaultValue;
        }
        return value;
    }
}
