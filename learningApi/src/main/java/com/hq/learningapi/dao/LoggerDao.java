package com.hq.learningapi.dao;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface LoggerDao {
    void loginfo(Map<String, Object> map);
}
