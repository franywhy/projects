package com.hq.bi.offline.task.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface TkMmUserJobMapper {

    Map<String,Object> queryUserJobByLessonUser(@Param("lessonId") String lessonId, @Param("userId") Long userId);
}