package com.hq.learningcenter.school.dao;

import java.util.List;
import java.util.Map;

import com.hq.learningcenter.school.pojo.LogPolyvWatchPOJO;

public interface LogPolyvWatchDao {
	public List<LogPolyvWatchPOJO> queryObject(Map<String,Object> map);
}
