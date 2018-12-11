package io.renren.mongo.dao;

import java.util.List;
import java.util.Map;

import io.renren.mongo.entity.ChapterEntity;
import io.renren.mongo.entity.CourseEntity;
import io.renren.mongo.entity.MongoPagination;
import io.renren.mongo.entity.SectionEntity;

public interface ICourseDao {

	/**
	 * 查询所有的课程信息
	 * @return
	 */
	public MongoPagination findAllCourse(int pageNo, int pageSize);
	
	/**
	 * 所有课程的数量
	 * @return
	 */
	public int queryTotal();
	
	/**
	 * 根据名称模糊查询课程信息
	 * @param name 课程名称
	 * @return
	 */
	public CourseEntity findCourseById(String id) ;

	/**
	 * 根据名称模糊查询课程的数量
	 * @return
	 */
	public int queryTotalByName(String name) ;
	
	/**
	 * 根据名称模糊查询课程信息
	 * @param name 课程名称
	 * @return
	 */
	public MongoPagination findCourseByName(int pageNo, int pageSize,String name) ;
	
	public Map findCourseInfoById(String courseId,String chapterId,String verseId) ;
	
	public List<ChapterEntity> findChapterByCourseId(String courseId) ;
	
	public List<SectionEntity> findVerseById(String courseId,String chapterId) ;

	public List<CourseEntity> findAll();
}
