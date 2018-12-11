package com.hq.learningcenter.school.service;

import java.util.List;
import java.util.Map;

public interface RecordCourseFileService {

    List<Map<String, Object>> getRecordFileList(int recordId, int fileType);
}
