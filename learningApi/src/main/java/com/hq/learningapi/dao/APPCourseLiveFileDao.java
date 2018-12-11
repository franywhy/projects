package com.hq.learningapi.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface APPCourseLiveFileDao {
    //获取上期复习的文件
    Map<String,Object> getReviewFileList(@Param("classplanLiveId") String classplanLiveId);

    //获取本期预习的文件
    Map<String,Object> getPrepareFileList(@Param("classplanLiveId") String classplanLiveId);

    //获取课堂资料的文件
    Map<String,Object> getCoursewareFileList(@Param("classplanLiveId") String classplanLiveId);

    //获取课堂资料(非自适应)的文件
    Map<String,Object> getFileList(@Param("classplanLiveId") String classplanLiveId);
}
