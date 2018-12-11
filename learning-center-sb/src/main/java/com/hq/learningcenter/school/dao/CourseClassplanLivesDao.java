package com.hq.learningcenter.school.dao;

import java.util.List;
import java.util.Map;

/*import com.school.entity.LiveShowDetailEntity;*/
/*import com.school.entity.VideoLogBean;*/
import com.hq.learningcenter.school.entity.CourseClassplanLivesEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Glenn on 2017/5/15 0015.
 */
@Repository
public interface CourseClassplanLivesDao {

    List<CourseClassplanLivesEntity> queryList(Map<String,Object> map);

    List<String> queryClassPlanLiveIdList(Map<String,Object> map);

    Integer queryTotalScheduledCourse(Map<String,Object> map);

    Integer queryLatestScheduledCourse(Map<String,Object> map);

    CourseClassplanLivesEntity queryByClassPlanLiveId(@Param("classPlanLiveId")String classPlanLiveId,@Param("dr")Integer dr);

    List<Map<String,Object>> queryByClassplanId(Map<String, Object> map);
    
    List<CourseClassplanLivesEntity> queryEntityByClassplanId(Map<String, Object> map);
    
    CourseClassplanLivesEntity queryByLiveId(Map<String, Object> map);
    
    void update(CourseClassplanLivesEntity classplanLive);
}
