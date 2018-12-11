package io.renren.dao;

import java.util.List;
import java.util.Map;

import io.renren.entity.CoursesEntity;
import io.renren.entity.RecordReFundsEntity;
import io.renren.pojo.SelectionItem;
import io.renren.pojo.course.CoursesPOJO;

/**
 * 课程档案
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-22 14:06:37
 */
public interface RecordRefundsDao extends BaseDao<RecordReFundsEntity> {
	  /**
	   * 单表查询 只是关联了
	   *@param map
	   *@return
	   * @author lintf
	   * 2018年8月13日
	   */
	List<RecordReFundsEntity> simpleQueryList(Map<String, Object> map);
	
	
}
