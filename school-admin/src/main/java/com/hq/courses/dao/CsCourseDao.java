package com.hq.courses.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hq.courses.entity.CsCourseEntity;
import com.hq.courses.pojo.query.CsCourseQuery;

/**
 * 课程
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-07 11:46:18
 */
public interface CsCourseDao {
	/**
	 * 根据ID查询查询课程
	 * @param courseId	自增id
	 * @return	课程
	 */
	public CsCourseEntity queryObject(@Param(value="courseId")Long courseId);
	
	/**
	 * 查询课程列表
	 * @return	课程列表
	 */
	public List<CsCourseEntity> queryList(CsCourseQuery query);
	
	/**
	 * 查询课程数量
	 * @return	课程数量
	 */
	public int queryTotal(CsCourseQuery query);
	
	/**
	 * 通过课程名称获取数量
	 * @param courseName
	 * @return
	 */
	public int queryByCourseName(String courseName);

	
	/**
	 * 新增课程
	 */
	public int save(CsCourseEntity CsCourseEntity);
	
	/**
	 * 更新课程
	 */
	public int update(CsCourseEntity CsCourseEntity);
	
	/**
	 * 按照ID删除
	 * @param courseId	自增id
	 */
	public int delete(@Param(value="courseId")Long courseId);
	/**
	 * 按照课程编号查询
	 * @param courseId	课程ID,不为空时查询不等该课程ID的对象
	 * @param courseNo	课程编号
	 * @return			
	 */
	public CsCourseEntity queryByCourseNo(@Param(value="courseId")Long courseId , @Param(value="courseNo")String courseNo);
	
	/**
	 * 修改状态
	 * @param courseId
	 * @param status
	 * @return
	 */
	public int updateStatus(@Param(value="courseId")Long courseId ,@Param(value="status")Integer status);
}
