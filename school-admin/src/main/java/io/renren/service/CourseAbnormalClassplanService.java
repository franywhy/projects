package io.renren.service;

import io.renren.entity.CourseAbnormalClassplanEntity;
import io.renren.entity.CourseClassplanEntity;
import io.renren.enums.CourseAbormalTypeEnum;
import io.renren.pojo.CourseAbnormalClassplanPOJO;
import io.renren.pojo.query.CourseAbnormalClassplanQuery;
import io.renren.utils.RRException;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 弃考免考记录单
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2018-03-17 17:58:51
 */
public interface CourseAbnormalClassplanService {

	CourseAbnormalClassplanPOJO queryObject(Long id);

	/**
	 * 取消
	 * @param id	主键
	 */
	void updateCancle(Long id);
	
	List<CourseAbnormalClassplanPOJO> queryList(CourseAbnormalClassplanQuery query);
	
	int queryTotal(CourseAbnormalClassplanQuery query);
	
	void save(CourseAbnormalClassplanPOJO courseAbnormalClassplanPOJO);
	
	void update(CourseAbnormalClassplanPOJO courseAbnormalClassplanPOJO)throws RRException;

	/**
	 * 审核通过
	 * @param id
	 * @param userId
	 */
    void updateAdopt(Long id, Long userId);

	/**
	 * 批量上传
	 * @param file	文件流
	 * @return		处理结果
	 */
	String importExcelTemplateCourse(MultipartFile file, Long createUserId);

	/**
	 * 课程名称查询最近排课计划entity
	 * @param courseName	课程名称
	 * @return	排课计划entity
	 */
	CourseClassplanEntity queryCourseClassplanEntityByCourseName(String courseName);

	/**
	 * 校验学员是否有休学或者弃考同一门排课计划
	 * @param userId		用户PK
	 * @param classplanId	排课计划PK
	 */
	void verifyStatus(Long userId , String classplanId);

	/**
	 *
	 * @param userId
	 * @param classplanIds
	 */
	public boolean verifyQikao(Long userId, List classplanIds);
	/**
	 *
	 * @param userId
	 * @param classplanIds
	 */
	public boolean verifyMiankao(Long userId, List classplanIds);
}
