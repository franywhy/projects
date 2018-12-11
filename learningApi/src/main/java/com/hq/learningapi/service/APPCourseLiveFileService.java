package com.hq.learningapi.service;

import java.util.List;
import java.util.Map;

public interface APPCourseLiveFileService {
    List<Map<String,Object>> getFileList(String classplanLiveId, int fileType);
}
