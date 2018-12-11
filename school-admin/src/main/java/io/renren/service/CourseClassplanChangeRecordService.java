package io.renren.service;

import io.renren.entity.CourseClassplanChangeRecordEntity;
import io.renren.entity.CourseClassplanEntity;
import io.renren.entity.CourseClassplanLivesChangeRecordEntity;
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
public interface CourseClassplanChangeRecordService {


	/**
	 * 排课计划审核通过备份
	 * @param courseClassplanId
	 */
	void saveAuait(String courseClassplanId,Long userId);
	//排课计划变更申请
	public void saveApply(ClassplanPOJO courseClassplan);

	void save(CourseClassplanChangeRecordEntity entity,List<CourseClassplanLivesChangeRecordEntity> livesEntityList);

	/**
	 * 审核通过
	 * */
	void accept(String classplanId,int versionNo,Long userId);

	/**
	 * 审核未过
	 * */
	void reject(String classplanId,int versionNo,Long userId);

	/**
	 * 获取数量
	 * @param classplanId
	 * @return
	 */
	int findCId(String classplanId);

	CourseClassplanEntity queryObjectByClassplanId(String classplanId, int versionNo);

	//查询排课变更表列表
    List<CourseClassplanChangeRecordEntity> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    CourseClassplanChangeRecordEntity queryObject1(Map<String, Object> map1);

	void deleteOldObject(String classplanId);
}
