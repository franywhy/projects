package io.renren.dao;

import java.util.List;
import java.util.Map;

import io.renren.entity.NcAttendanceEntity;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-12 14:06:17
 */
public interface NcAttendanceDao extends BaseMDao<NcAttendanceEntity> {
	int saveBatch(List<NcAttendanceEntity> list);
	
	List getAttendanceInfo(Map<String, Object> map);
}
