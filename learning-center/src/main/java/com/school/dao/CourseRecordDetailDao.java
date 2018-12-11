package com.school.dao;

import com.school.entity.CourseRecordDetailEntity;
import com.school.entity.MallGoodsDetailsEntity;
import com.school.pojo.CourseRecordDetailPOJO;
import com.school.pojo.CoursesPOJO;
import com.school.pojo.LogWatchRecordPOJO;
import com.school.pojo.OrderPOJO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;


/**
 * 课程录播
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-08-11 16:49:39
 */
public interface CourseRecordDetailDao {
	
	List<Long> queryIdListByCourseId(@Param("courseId") Long courseId);

	List<Long> queryCourseId(@Param("orderId")Long orderId, @Param("areaId")Long areaId, @Param("commodityId")Long commodityId);

	int queryRecordCourseNum(@Param("courseId")Long courseId);

	CoursesPOJO queryRecordCourse(@Param("courseId")Long courseId, @Param("productIdList")List<Long> productIdList);

	List<CourseRecordDetailPOJO> queryRecordDetailList(@Param("courseId")Long courseId);

	List<CourseRecordDetailPOJO> queryRecordDetailList2(@Param("courseId")Long courseId, @Param("recordId")Long recordId);

	List<LogWatchRecordPOJO> queryLogWatchRecordList(@Param("userId")Long userId);

	int queryIsWatch(@Param("recordId")Long recordId);

	CourseRecordDetailPOJO getRecordInfo(@Param("recordId")Long recordId);

	//获取产品线id
	List<Long> queryProductId(@Param("businessId")String businessId);

}
