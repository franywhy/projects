package io.renren.modules.job.service.impl;

import io.renren.modules.job.dao.CourseRecordDetailDao;
import io.renren.modules.job.pojo.CourseRecordDetailPOJO;
import io.renren.modules.job.service.CourseRecordDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DL on 2018/10/16.
 */
@Service("courseRecordDetailService")
public class CourseRecordDetailServiceImpl implements CourseRecordDetailService {

    @Autowired
    private CourseRecordDetailDao courseRecordDetailDao;
    @Override
    public CourseRecordDetailPOJO queryRecordDetailPojo(String recordId, String vid) {
        CourseRecordDetailPOJO pojo = courseRecordDetailDao.queryRecordDetailPojoByRecordId(recordId);
        if (pojo == null){
            pojo = courseRecordDetailDao.queryRecordDetailPojoByVid(vid);
        }
        return pojo;
    }
}
