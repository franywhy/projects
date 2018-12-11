package io.renren.dao.manage;

import io.renren.entity.manage.StudentCourse;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface StudentCourseDao {

    int insertBatch(List<StudentCourse> list);

    int deleteByNcId(@Param("ncId") String ncId);

    Set<String> queryCourseNoListByNcId(@Param("ncId") String ncId);

    int deleteByNcIdCourseNo(@Param("ncId") String ncId, @Param("courseNoSet") Set<String> courseNoSet);
}