package com.school.service.impl;

import com.school.dao.CourseAbnormallRegistrationDao;
import com.school.dao.ExaminationResultDao;
import com.school.entity.CourseAbnormalRegistrationEntity;
import com.school.entity.ExaminationResultEntity;
import com.school.enums.AuditStatusEnum;
import com.school.pojo.CourseAbnormallRegistrationPOJO;
import com.school.pojo.ExaminationResultPOJO;
import com.school.pojo.OrderPOJO;
import com.school.rest.persistent.KGS;
import com.school.service.CourseAbnormallRegistrationService;
import com.school.service.ExaminationResultService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by DL on 2018/4/2.
 */
@Service("examinationResultService")
public class ExaminationResultServiceImpl implements ExaminationResultService {

    @Autowired
    private ExaminationResultDao examinationResultDao;

    @Autowired
    private CourseAbnormallRegistrationService courseAbnormallRegistrationService;

    @Override
    public List<ExaminationResultPOJO> queryList(Map<String, Object> map) {
        return examinationResultDao.queryPojoList(map);
    }

    @Override
    public void save(Long userId, Long registrationId, int score, String img) {
        ExaminationResultEntity er = new ExaminationResultEntity();
        er.setUserId(userId);
        er.setRegistrationId(registrationId);
        er.setScore(score);
        er.setImg(img);
        er.setExamType(0);//初考
        Integer count = courseAbnormallRegistrationService.queryRegisterCount(registrationId);
        if(count > 1){//补考
            er.setExamType(1);
        }
        examinationResultDao.save(er);
    }
}
