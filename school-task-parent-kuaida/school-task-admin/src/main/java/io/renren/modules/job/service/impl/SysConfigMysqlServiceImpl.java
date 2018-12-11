package io.renren.modules.job.service.impl;

import io.renren.modules.job.dao.SysConfigMysqlDao;
import io.renren.modules.job.service.SysConfigMysqlService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DL on 2018/8/20.
 */
@Service("sysConfigMysqlService")
public class SysConfigMysqlServiceImpl implements SysConfigMysqlService {
    @Autowired
    private SysConfigMysqlDao sysConfigMysqlDao;

    @Override
    public String getValue(String key, String defaultValue) {
        String value = sysConfigMysqlDao.queryByKey(key);
        if(StringUtils.isBlank(value)){
            return defaultValue;
        }
        return value;
    }
}
