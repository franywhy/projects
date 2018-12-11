package io.renren.service;

import io.renren.entity.CourseAbnormalAbandonExamEntity;
import io.renren.pojo.CourseAbnormalAbandonExamPOJO;
import io.renren.pojo.CourseAbnormalRegistrationPOJO1;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

/**
 * 弃考档案表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-03-27 14:37:18
 */
public interface CourseAbnormalAbandonExamService {
	
		
	CourseAbnormalAbandonExamEntity queryObject(Long id);
	
	List<CourseAbnormalAbandonExamEntity> queryList(Map<String, Object> map);	
	
	//查询POJO
	List<CourseAbnormalAbandonExamPOJO> queryPOJOList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(CourseAbnormalAbandonExamEntity courseAbnormalAbandonExam);
	
	String saveExamEntity(CourseAbnormalAbandonExamEntity courseAbnormalAbandonExam);
	
	void update(CourseAbnormalAbandonExamEntity courseAbnormalAbandonExam);
		
	//作废状态
	public void cancel(Integer status,Long[] ids,Long userId,Date date);
	//审核通过
	public void pass(Integer status,Long[] ids,Long userId,Date date);
	
	//批量保存
	public void saveBatch(List<CourseAbnormalAbandonExamEntity> list);
	
	//根据订单号查询
	CourseAbnormalRegistrationPOJO1 queryByOrderNo(String orderNo);	
	//根据课程名称 订单号 报考时间名称 报考省份名称查询
	CourseAbnormalRegistrationPOJO1 queryByNo(Map<String, Object> map);	
	//报考订单数据
	CourseAbnormalRegistrationPOJO1  queryByRegistrationNo(Map<String, Object> map);	
	//弃考模板
	String downExcel();
	//导入模板
	String importExcel(Long userId, MultipartFile file);

		
}
