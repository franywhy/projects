package com.hq.bi.search.service;

import com.hq.bi.search.entity.LogStudentAttendEntity;

import java.util.List;
import java.util.Map;

/**
 * @author hq
 */
public interface LogStudentAttendService {

	List<LogStudentAttendEntity> getStudentAttendList(Map<String, Object> map);

}
