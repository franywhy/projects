package io.renren.modules.job.service;

import io.renren.modules.job.pojo.CourseRecordDetailPOJO;

/**
 * Created by DL on 2018/10/16.
 */
public interface CourseRecordDetailService {

    CourseRecordDetailPOJO queryRecordDetailPojo(String recordId, String vid);
}
