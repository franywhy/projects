package com.school.dao;

import java.util.List;
import java.util.Map;

import com.school.pojo.LogPolyvWatchPOJO;

public interface LogPolyvWatchDao {
	public List<LogPolyvWatchPOJO> queryObject(Map<String,Object> map);
}
