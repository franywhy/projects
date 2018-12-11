package io.renren.modules.job.service.impl;

import io.renren.modules.job.dao.LogPolyvDetailDao;
import io.renren.modules.job.entity.LogPolyvDetailEntity;
import io.renren.modules.job.service.LogPolyvDetailService;
import io.renren.modules.job.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by DL on 2018/10/16.
 */
@Service("logPolyvDetailService")
public class LogPolyvDetailServiceImpl implements LogPolyvDetailService {

    @Autowired
    private LogPolyvDetailDao polyvDetailDao;
    @Override
    public void save(LogPolyvDetailEntity entity) {
        polyvDetailDao.save(entity);
    }

    @Override
    public boolean checkDetail(String userId, String recordId, Date createdTime) {

        return polyvDetailDao.checkDetail(userId, recordId, createdTime) == 0;
    }
}
