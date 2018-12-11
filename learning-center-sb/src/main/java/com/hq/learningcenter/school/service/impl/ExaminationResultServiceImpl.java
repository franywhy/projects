package com.hq.learningcenter.school.service.impl;

import com.hq.learningcenter.school.service.CourseAbnormallRegistrationService;
import com.hq.learningcenter.school.service.ExaminationResultService;
import com.hq.learningcenter.school.dao.ExaminationResultDao;
import com.hq.learningcenter.school.entity.ExaminationResultEntity;
import com.hq.learningcenter.school.pojo.ExaminationResultPOJO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
