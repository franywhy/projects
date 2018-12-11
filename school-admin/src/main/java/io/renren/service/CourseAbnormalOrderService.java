package io.renren.service;

import io.renren.entity.CourseAbnormalOrderEntity;
import io.renren.enums.CourseAbormalTypeEnum;
import io.renren.pojo.CourseAbnormalOrderPOJO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 休学失联记录单
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-03-19 09:26:43
 */
public interface CourseAbnormalOrderService {


	CourseAbnormalOrderPOJO queryObject(Long id);
	
	List<CourseAbnormalOrderPOJO> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(CourseAbnormalOrderEntity courseAbnormalOrder);
	
	void update(CourseAbnormalOrderEntity courseAbnormalOrder);

	void auditPass(CourseAbnormalOrderEntity courseAbnormalOrder);

	void saveResumeClasses(CourseAbnormalOrderEntity courseAbnormalOrder);
	
	void updateCancelBatch(Integer status,Long[] ids);
	
	void deleteBatch(Map<String, Object> map);

	void updateCancelBatch(Integer auditStatus,Long[] ids,Long userId,Date date);


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
	String importExcel(Long userId,MultipartFile file);

	/**
	 * 检查订单是否异常
	 * @param orderId
	 * @param startTime
	 * @param endTime
	 */
	Integer verifyStatusCount(Long orderId,Date startTime,Date endTime);



    /**
     * 验证是否失联、休学
     * @return
     */
    CourseAbormalTypeEnum verifyXiuxueOrShilian(Long orderId, Date startTime);
}
