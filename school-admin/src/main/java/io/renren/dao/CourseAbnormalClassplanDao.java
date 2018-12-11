package io.renren.dao;

import io.renren.entity.CourseAbnormalClassplanEntity;
import io.renren.entity.CourseClassplanEntity;
import io.renren.pojo.CourseAbnormalClassplanPOJO;
import io.renren.pojo.query.CourseAbnormalClassplanQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 弃考免考记录单
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2018-03-17 17:58:51
 */
@Mapper
public interface CourseAbnormalClassplanDao {


    CourseAbnormalClassplanPOJO queryObject(@Param(value="id")Long id);

    /**
     * 查询列表
     */
    List<CourseAbnormalClassplanPOJO> queryList(CourseAbnormalClassplanQuery query);


    /**
     * 查询数量
     * @return	数量
     */
    int queryTotal(CourseAbnormalClassplanQuery query);

    /**
     * 新增
     */
    int save(CourseAbnormalClassplanEntity CsCourseEntity);

    /**
     * update
     */
    int update(CourseAbnormalClassplanEntity CsCourseEntity);

    /**
     * 取消
     */
    void updateCancle(@Param(value="id")Long id);

    void updateAdopt(@Param(value="id")Long id ,@Param(value="modifyPerson") Long modifyPerson ,@Param(value="modifiedTime") Date modifiedTime);

    /**
     * 课程名称查询最近排课计划entity
     * @param courseName	课程名称
     * @return	排课计划entity
     */
    CourseClassplanEntity queryCourseClassplanEntityByCourseName(@Param(value="courseName")String courseName);

    /**
     * 根据用户PK和排课计划PK查询
     * @param userId        学员PK
     * @param classplanIds  排课计划PK数组
     * @param courseAbormalType   状态
     * @return              数量
     */
    int queryTotalByClassplans(@Param(value="userId")Long userId , @Param(value="classplanIds")List classplanIds ,@Param(value="courseAbormalType") Integer courseAbormalType);

    /**
     * 学员是否存在相同排课计划的档案
     * @param userId        学员PK
     * @param classplanId   排课计划PK
     * @return              存在 返回entity对象;否则返回null
     */
    CourseAbnormalClassplanEntity queryObjectByClassplan(@Param(value="userId")Long userId , @Param(value="classplanId")String classplanId);
}
