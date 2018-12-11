package com.hq.courses.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hq.courses.entity.CsCourseEntity;
import com.hq.courses.pojo.CsCoursePOJO;
import com.hq.courses.pojo.query.CsCourseQuery;

/**
 * 课程
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-06 16:48:13
 */
public interface CsCourseService {
	/**
	 * 根据ID查询查询课程
	 * @param courseId	id
	 * @return	课程
	 */
	CsCoursePOJO queryObject(Long courseId);
	
	/**
	 * 查询课程列表
	 * @param query	查询条件
	 * @return	课程列表
	 */
	List<CsCoursePOJO> queryList(CsCourseQuery query);
	
	/**
	 * 查询课程数量
	 * @return	课程数量
	 */
	int queryTotal(CsCourseQuery query);
	
	/**
	 * 新增课程
	 */
	int save(CsCourseEntity csCourse);
	
	/**
	 * 更新课程
	 */
	int update(CsCourseEntity csCourse);
	
	/**
	 * 按照ID删除
	 * @param courseId	id
	 */
	int delete(Long courseId);
	
	/**
	 * 启用
	 * @param courseId	ID
	 * @return
	 */
	int enable(Long courseId);
	/**
	 * 禁用
	 * @param courseId	ID
	 * @return
	 */
	int unenable(Long courseId);
	/**
	 * 校验课程编号是否合法
	 * @param courseId	课程ID
	 * @param courseNo	课程编号
	 */
	void checkCourseNo(Long courseId , String courseNo);
	
	/**
	 * 知识点是否被引用
	 * @param courseId	知识点ID
	 * @return				被引用说明从;未被引用返回NULL
	 */
	public void checkQuote(Long courseId);
	
	/**
	 * 批量导入课程
	 * @param file
	 * @return
	 */
	String importCourseExcelInitialization(MultipartFile file);
	
	/**
	 * 根据编码查询查询课程
	 * @param courseNo	编码
	 * @return	课程
	 */
	CsCourseEntity queryObject(String courseNo);
}
