package io.renren.modules.job.dao;

import io.renren.modules.job.pojo.CourseRecordDetailPOJO;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by DL on 2018/10/16.
 */
@Mapper
public interface CourseRecordDetailDao {

    CourseRecordDetailPOJO queryRecordDetailPojoByRecordId(String recordId);

    CourseRecordDetailPOJO queryRecordDetailPojoByVid(String vid);
}
