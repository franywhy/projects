package io.renren.modules.job.service;

import io.renren.modules.job.entity.LogPolyvDetailEntity;

import java.util.Date;

/**
 * Created by DL on 2018/10/16.
 */
public interface LogPolyvDetailService {

    void save(LogPolyvDetailEntity entity);

    boolean checkDetail(String userId, String recordId, Date createdTime);
}
