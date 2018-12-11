package com.hq.learningapi.dao;

import com.hq.learningapi.pojo.HeadlinePOJO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface HeadlineDao {

    void addReadNumber(@Param("headlineId") Long headlineId);

    void addCommentNumber(@Param("headlineId") Long headlineId);

    HeadlinePOJO queryPojoObject(@Param("headlineId") Long headlineId);

    List<HeadlinePOJO> queryPojoList(Map<String, Object> map);

    List<Map<String,Object>> queryMapList(Map<String, Object> map);
}