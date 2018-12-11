package io.renren.dao;

import java.util.List;
import java.util.Map;

import io.renren.entity.CoursesEntity;
import io.renren.entity.RecordInfoEntity;
import io.renren.pojo.RecordStudyPOJO;
import io.renren.pojo.SelectionItem;
import io.renren.pojo.course.CoursesPOJO;
/**
 * 学员档案-学习信息
 * @author lintf 
 *
 */
public interface RecordStudyDao extends BaseDao<RecordStudyPOJO> {
 /**
  * 根据ts同步学习信息
  *@param queryMap
  * @author lintf
  * 2018年8月17日
  */
	 List<RecordStudyPOJO> getSynchronizeRecordStudyList(Map<String,Object> queryMap);
}
