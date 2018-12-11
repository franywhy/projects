package com.hq.learningcenter.school.dao;

import com.hq.learningcenter.school.entity.CourseAbnormalAbandonExamEntity;
import com.hq.learningcenter.school.pojo.CourseAbnormalAbandonExamPOJO;
import com.hq.learningcenter.school.pojo.MallExamSchedulePOJO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 弃考档案表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-03-27 14:37:18
 */
@Repository
public interface CourseAbnormalAbandonExamDao {

	CourseAbnormalAbandonExamPOJO queryObject(Long id);
	
	List<CourseAbnormalAbandonExamPOJO> queryPOJOList(Map<String, Object> map);

	void save(CourseAbnormalAbandonExamEntity courseAbnormalAbandonExam);

	//修改状态
	void updateStatus(Map<String, Object> map);
	//考试时间表
	List<MallExamSchedulePOJO> queryScheduleDateList(Map<String,Object> map);

	void updateCancel(Map<String, Object> map);
}