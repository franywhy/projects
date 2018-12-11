package com.hq.learningcenter.school.dao;


import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface RecordCourseFileDao {
    //获取录播课文件
    Map<String, Object> getRecordFileList(@Param("recordId") int recordId);
}
