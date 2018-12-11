package io.renren.service;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import io.renren.entity.MallOrderEntity;
import io.renren.entity.OrderMessageConsumerEntity;
import io.renren.entity.RecordInfoEntity;
import io.renren.pojo.RecordStudyPOJO;

public interface RecordStudyService {
	 
	
	List< RecordStudyPOJO> queryList(Map<String,Object> queryMap);
	 
	/**
	 * 查询总页数
	 *@param map
	 *@return
	 * @author lintf
	 * 2018年8月13日
	 */
	int queryTotal(Map<String, Object> queryMap);
	/**
	 * 根据时间取得要同步的学员信息
	 *@param queryMap
	 *@return
	 * @author lintf
	 * 2018年8月19日
	 */
	List<RecordStudyPOJO> getSynchronizeRecordStudyList(Map<String, Object> queryMap);

/**
 * 运算要同步的学员信息
 *@param list
 * @author lintf
 * 2018年8月19日
 */
	void runSynchronizeProcess(List<RecordStudyPOJO> list);

	void synchronizeRecordStudy(Map<String, Object> queryMap);
	/**
	 * 处理学员的听课完成率
	 *@param list
	 * @author lintf
	 * 2018年8月20日
	 */
	void ClassCompletionRateProcess(List<RecordStudyPOJO> list);
/**
 * 处理学员的统考成绩
 *@param list
 * @author lintf
 * 2018年8月20日
 */
	void examinationResultProcess(List<RecordStudyPOJO> list);
}
