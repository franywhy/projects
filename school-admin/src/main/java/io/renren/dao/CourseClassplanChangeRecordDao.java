package io.renren.dao;

import io.renren.entity.CourseClassplanChangeRecordEntity;
import io.renren.entity.CourseClassplanEntity;
import io.renren.pojo.classplan.ClassplanPOJO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 排课计划变更记录表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-06 10:45:58
 */
public interface CourseClassplanChangeRecordDao extends BaseDao<CourseClassplanChangeRecordEntity> {
	/**
	 * 审核
	 * @param map
	 */
	int audited(Map<String, Object> map);

	int findCId(String classplanId);

    CourseClassplanChangeRecordEntity queryObject1(Map<String, Object> map1);


	CourseClassplanEntity queryObjectByClassplanId(@Param(value="classplanId")String classplanId,@Param(value="versionNo")int versionNo);

	void deleteOldObject(String classplanId);
}
