package io.renren.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import io.renren.entity.CourseAbnormalFreeAssessmentEntity;
import io.renren.pojo.CourseAbnormalFreeAssessmentPOJO;

/**
 * 免考记录单
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-03-19 09:26:43
 */
public interface CourseAbnormalFreeAssessmentService {


	CourseAbnormalFreeAssessmentPOJO queryObject(Long id);
	
	List<CourseAbnormalFreeAssessmentPOJO> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);

	void save(CourseAbnormalFreeAssessmentEntity courseAbnormalOrder);

	void updateStatus(Integer auditStatus,Long id);
	
	void updateCancelBatch(Integer auditStatus,Long[] ids,Long userId,Date date);

	void updateAuditPassBatch(Integer auditStatus,Long[] ids,Long userId,Date date);

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

}
