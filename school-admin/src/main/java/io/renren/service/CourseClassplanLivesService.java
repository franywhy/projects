package io.renren.service;

import io.renren.entity.CourseClassplanLivesChangeRecordEntity;
import io.renren.entity.CourseClassplanLivesEntity;
import io.renren.pojo.classplan.ClassplanLivePOJO;
import io.renren.utils.ClassplanLiveException;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 排课计划直播明细子表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-08 09:55:51
 */
public interface CourseClassplanLivesService {

    /**
     * 校验授课老师是否冲突(只校验不同排课的直播课冲突情况，同一排课下的冲突不校验)
     * @param courseClassplanLives
     * @param courseClassplanLives.teacher_id	教师ID
     * @param courseClassplanLives.day_time		时间
     * @param courseClassplanLives.time_bucket	时间段
     * @param courseClassplanLives.school_id	平台ID
     * @param courseClassplanLives.classplan_id	主表ID（可空）
     * @return	map=null 不冲突；map！=null冲突-冲突信息
     */
    Map<String, Object>	checkTeacher(CourseClassplanLivesEntity courseClassplanLives);

    /**
     * 校验直播室是否冲突(只校验不同排课的直播课冲突情况，同一排课下的冲突不校验)
     * @param courseClassplanLives
     * @param courseClassplanLives.studio_id	直播室ID
     * @param courseClassplanLives.day_time		时间
     * @param courseClassplanLives.time_bucket	时间段
     * @param courseClassplanLives.school_id	平台ID
     * @param courseClassplanLives.classplan_id	主表ID（可空）
     * @return	map=null 不冲突；map！=null冲突-冲突信息
     */
    Map<String, Object>	checkStudio(CourseClassplanLivesEntity courseClassplanLives);
    /**
     * 校验直播间是否冲突(只校验不同排课的直播课冲突情况，同一排课下的冲突不校验)
     * @param courseClassplanLives
     * @param courseClassplanLives.liveroom_id	直播室ID
     * @param courseClassplanLives.day_time		时间
     * @param courseClassplanLives.time_bucket	时间段
     * @param courseClassplanLives.school_id	平台ID
     * @param courseClassplanLives.classplan_id	主表ID（可空）
     * @return	map=null 不冲突；map！=null冲突-冲突信息
     */
    Map<String, Object>	checkLiveRoom(CourseClassplanLivesEntity courseClassplanLives);
    /**
     * 校验同一课程多次排课是否冲突（校验多次排课后的课次上课时间是否冲突）
     * @param courseClassplanLives
     * @param courseClassplanLives.course_id	课程ID
     * @param courseClassplanLives.day_time		时间
     * @param courseClassplanLives.time_bucket	时间段
     * @param courseClassplanLives.school_id	平台ID
     * @param courseClassplanLives.classplan_id	主表ID（可空）
     * @return	map=null 不冲突；map！=null冲突-冲突信息
     */
    Map<String, Object>	checkCourseLive(CourseClassplanLivesEntity courseClassplanLives);
    /**
     * 校验同一商品下的不同课程的排课次排课是否冲突（排课不可冲突的课程与该商品下的所有课程的排课课次时间不能冲突，排课可冲突的课程与该商品下的可冲突课程的上课时间不受影响，
     * 但是，直播室、直播间、授课老师不可冲突）
     * @param courseClassplanLives
     * @param courseClassplanLives.course_id	课程ID
     * @param courseClassplanLives.day_time		时间
     * @param courseClassplanLives.time_bucket	时间段
     * @param courseClassplanLives.school_id	平台ID
     * @param courseClassplanLives.classplan_id	主表ID（可空）
     * @return	map=null 不冲突；map！=null冲突-冲突信息
     */
    Map<String, Object> checkOneCommodityCourseLive(CourseClassplanLivesEntity courseClassplanLives);

    CourseClassplanLivesEntity queryObject(Map<String, Object> map);

    List<CourseClassplanLivesEntity> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    void save(CourseClassplanLivesEntity courseClassplanLives) throws ClassplanLiveException;

    void update(CourseClassplanLivesEntity courseClassplanLives);

    void delete(Long classplanLiveId);

    void deleteBatch(Map<String, Object> map);

    void pause(Long[] classplanLiveIds);

    void resume(Long[] classplanLiveIds);

    /**
     * 删除ID不等于classplanLiveIds的数据
     * @param classplanLiveIds = id数组
     * @param classplanId = 排课计划PK
     */
    void deleteBatchNotIn(Map<String, Object> map);
    /**
     * 查询子表
     */
    List<ClassplanLivePOJO> queryPojoList(Map<String, Object> map2);

    //判断是否有班型的引用
    int checkClassType(long id);

    /**
     * 根据课程id获取该课程下的课次
     * @param courseId 课程id
     * @param schoolId 平台id
     * @return
     */
    List<String> getCourseLives(Long courseId, String schoolId);

    /**
     * 根据日期查询当天排课子表及直播间信息
     * @param startTime 该日期0:00:00时间点
     * @param endTime 该日期23:59:59时间点
     * @param schoolId 平台id
     * @return
     */
    List<Map<String,Object>> queryByDate(Map<String, Object> map);

    /**
     * 根据回放id查询排课子表
     * @param backId 回放id
     * @param backType 回放类型
     * @param schoolId 平台id
     * @return
     */
    Map<String,Object> queryByBackId(Map<String, Object> map);

    /**
     * 直播明细列表
     * @param map
     * @return
     */
    List<ClassplanLivePOJO> queryPojoList1(Map<String, Object> map);

    /**
     * 直播明细详情
     * @param map
     * @return
     */
    ClassplanLivePOJO queryObject1(Map<String, Object> map);

    int findMid(String Mid);

    void updateBackUrl(Map<String,Object> map);

    void updateBackUrlByClassplanliveId(Map<String,Object> map);

    String findClassplanliveId(String mId);
    CourseClassplanLivesEntity queryByClassPlanLiveId(Map<String,Object> map);


    /**
     * 获得直播回放地址
     * @param map 查询排课子表的参数
     * @param parameters 拼接url的参数
     * @return
     */
    String queryReplayUrl(Map<String,Object> map, Map<String, Object> parameters);
    /**
     * 根据时间范围查询直播详情列表
     * @param map 查询排课子表的参数
     * @return 直播详情列表
     */
    List<CourseClassplanLivesEntity> queryCourseClassplanLivesByDate(Map<String,Object> map);



    /**
     * 获取并保存录制件
     * @param datePicker 日期选择 不填默认0为当天，1前一天
     * @param startCountTime 开始统计时间
     * @param endCountTime 结束统计时间
     * @return 返回码
     */
    Map<String,Object> updataPlaybackData(Integer datePicker,String startCountTime,String endCountTime);


    List<CourseClassplanLivesChangeRecordEntity> queryEntityListByClassplanId(String classplanId);

    List<CourseClassplanLivesEntity> queryListByClassplanId(String courseClassplanId);

}
