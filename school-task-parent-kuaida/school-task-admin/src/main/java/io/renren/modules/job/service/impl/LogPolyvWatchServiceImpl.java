package io.renren.modules.job.service.impl;

import io.renren.modules.job.dao.LogPolyvWatchDao;
import io.renren.modules.job.entity.LogPolyvWatchEntity;
import io.renren.modules.job.service.LogPolyvWatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * Created by DL on 2018/10/16.
 */
@Service("logPolyvWatchService")
public class LogPolyvWatchServiceImpl implements LogPolyvWatchService {

    @Autowired
    private LogPolyvWatchDao logPolyvWatchDao;
    @Override
    public void saveOrUpdate(LogPolyvWatchEntity entity) {
        LogPolyvWatchEntity oldEntity = logPolyvWatchDao.queryObjectByUserId(entity.getUserId(),entity.getRecordId());
        if (oldEntity != null){
            oldEntity.setFullDuration(entity.getFullDuration());
            oldEntity.setPlayDuration(oldEntity.getPlayDuration()+entity.getPlayDuration());
            oldEntity.setAttentPer(durPer(oldEntity.getFullDuration(),oldEntity.getPlayDuration()));
            logPolyvWatchDao.update(oldEntity);
        }else {
            entity.setAttentPer(durPer(entity.getFullDuration(),entity.getPlayDuration()));
            entity.setCreateTime(new Date());
            entity.setTs(new Date());
            logPolyvWatchDao.save(entity);
        }
    }

    /**
     * 计算出勤率
     * 观看总时长watchDur/应观看总时长fullDur
     * @return
     */
    private static final BigDecimal durPer(Long fullDur , Long watchDur) {
        BigDecimal per = null;
        if (null == fullDur || null == watchDur || 0 == fullDur) {
            per = new BigDecimal(0);
        } else if (fullDur <= watchDur) {
            per = new BigDecimal(1);
        } else {
            //保留四位小数
            per = new BigDecimal(watchDur).divide(new BigDecimal(fullDur), 4, RoundingMode.HALF_UP);
        }
        return per;
    }
}
