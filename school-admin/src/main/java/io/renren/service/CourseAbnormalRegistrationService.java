package io.renren.service;


import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import io.renren.entity.CourseAbnormalRegistrationEntity;
import io.renren.pojo.CourseAbnormalRegistrationPOJO;

/**
 * 报考档案表
 * 
 * @author vince
 * @date 2018-03-28 09:21:52
 */
public interface CourseAbnormalRegistrationService {
	
	CourseAbnormalRegistrationEntity queryObject(Long id);
	
	List<CourseAbnormalRegistrationEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(CourseAbnormalRegistrationEntity courseAbnormalRegistration);
	
	void update(CourseAbnormalRegistrationEntity courseAbnormalRegistration);
	
	List<CourseAbnormalRegistrationPOJO> queryRegistrationList(Map<String, Object> map);
	
	void cancellation(Long[] registrationIds);
	
	String audit(Long[] registrationIds);
	
	String opposeAudit(Long[] registrationIds);
	
	List<CourseAbnormalRegistrationPOJO> queryRegistrationLayList(Map<String, Object> map);
	
	/**
	 * excel模板
	 * @return
	 */
	String downExcel();
	
	/**
	 * 批量导入
	 * @param file
	 * @return
	 */
	String importExcel(Long userId, MultipartFile file);
	
	/**
	 * 保存实体
	 * @param courseAbnormalRegistration
	 * @return
	 */
	String saveEntity(CourseAbnormalRegistrationEntity courseAbnormalRegistration);
	
	/**
	 * 获取弹窗总条数
	 * @param courseAbnormalRegistration
	 * @return
	 */
	int queryLayTotal(Map<String, Object> map);
}
