package com.hq.bi.offline.task.mapper;

import com.hq.bi.offline.task.entity.LogStudentAttendEntity;

import java.util.List;
import java.util.Map;

public interface LogStudentAttendMapper {

	List<LogStudentAttendEntity> queryUserplan(Map<String, Object> map);

	List<Map<String,Object>> queryLivePerByMobile(Map<String, Object> map);

	int insertBatch(List<LogStudentAttendEntity> list);
}
