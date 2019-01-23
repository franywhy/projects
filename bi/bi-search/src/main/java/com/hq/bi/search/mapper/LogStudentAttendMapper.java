package com.hq.bi.search.mapper;


import com.hq.bi.search.entity.LogStudentAttendEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface LogStudentAttendMapper {

	List<LogStudentAttendEntity> queryStudentAttendList(Map<String, Object> map);
}
