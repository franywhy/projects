package com.hq.bi.search.service.impl;

import com.hq.bi.search.entity.LogStudentAttendEntity;
import com.hq.bi.search.mapper.LogStudentAttendMapper;
import com.hq.bi.search.service.LogStudentAttendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

/**
 * Created by longduyuan on 2018/12/29 0029.
 */
@Service
public class LogStudentAttendServiceImpl implements LogStudentAttendService {

    @Autowired
    private LogStudentAttendMapper logStudentAttendMapper;

    @Override
    public List<LogStudentAttendEntity> getStudentAttendList(Map<String, Object> map) {
        List<LogStudentAttendEntity> list = logStudentAttendMapper.queryStudentAttendList(map);
        if(null != list && !list.isEmpty()) {
            //总出勤 小计
            BigDecimal attendPer = new BigDecimal(0);
            for(LogStudentAttendEntity entity : list) {
                //暂时统一写死在读
                entity.setUserStatus("在读");
                if(null == entity.getMinWatchDur()){
                    entity.setMinWatchDur(new BigDecimal(0));
                }
                entity.setMinWatchDurTxt(entity.getMinWatchDur().setScale(0, RoundingMode.CEILING)+"");
                entity.setMinFullDurTxt(entity.getMinFullDur().setScale(0,RoundingMode.CEILING)+"");
                entity.setLivePerTxt(entity.getLivePer().multiply(new BigDecimal(100)).setScale(2,RoundingMode.CEILING) + "%");
                entity.setAttendPerTxt(entity.getAttendPer().multiply(new BigDecimal(100)).setScale(2,RoundingMode.CEILING) + "%");
                if(null == entity.getCompliancePer()) {
                    entity.setCompliancePer(new BigDecimal(0));
                }
                entity.setCompliancePerTxt(entity.getCompliancePer().multiply(new BigDecimal(100)).setScale(2,RoundingMode.CEILING) + "%");
                //总出勤 小计
                attendPer = attendPer.add(entity.getAttendPer());
            }
            LogStudentAttendEntity total = new LogStudentAttendEntity();
            total.setUserPlanId(0L);
            total.setUserId(0L);
            total.setUserName("小计");
            total.setDeptName(String.valueOf(list.size()));
            total.setUserStatus("--");
            total.setMobile("--");
            total.setAreaName("--");
            total.setClassName("--");
            total.setTeacherName("--");
            total.setLivePerTxt("--");
            total.setAttendPerTxt(attendPer.divide(new BigDecimal(list.size()),6).multiply(new BigDecimal(100)).setScale(2,RoundingMode.CEILING) + "%");
            total.setMinWatchDurTxt("--");
            total.setMinFullDurTxt("--");
            total.setClassplanLiveName("--");
            total.setCompliancePerTxt("--");
            total.setLivePer(new BigDecimal(0));
            total.setAttendPer(new BigDecimal(0));
            total.setMinWatchDur(new BigDecimal(0));
            total.setMinFullDur(new BigDecimal(0));
            total.setCompliancePer(new BigDecimal(0));
            list.add(total);
        }
        return list;
    }
}
