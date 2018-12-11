package io.renren.dao;

import java.util.List;
import java.util.Map;

import io.renren.entity.NcCoursesEntity;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-12 14:06:17
 */
public interface NcCoursesDao extends BaseMDao<NcCoursesEntity> {
	int saveBatch(List<NcCoursesEntity> list);
	
	int deleteAll();
}
