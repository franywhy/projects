package com.hq.learningcenter.school.service.impl;

import com.hq.learningcenter.school.dao.RecordCourseFileDao;
import com.hq.learningcenter.school.service.RecordCourseFileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("recordCourseFileService")
public class RecordCourseFileServiceImpl implements RecordCourseFileService {

    @Autowired
    private RecordCourseFileDao recordCourseFileDao;

    @Override
    public List<Map<String, Object>> getRecordFileList(int recordId, int fileType) {
        List<Map<String, Object>> list = new ArrayList<>();
        //文件类型为录播课文件
        if (fileType == 5) {
            Map<String, Object> fileList = recordCourseFileDao.getRecordFileList(recordId);
            if (null != fileList) {
                String fileUrl = (String) fileList.get("fileUrl");
                String fileName = (String) fileList.get("fileName");
                if (StringUtils.isNotBlank(fileName)) {
                    String[] firlUrlArr = fileUrl.split(",");
                    if (StringUtils.isNotBlank(fileUrl)) {
                        String[] fileNameArr = fileName.split(",");
                        for (int i = 0; i < firlUrlArr.length; i++) {
                            HashMap<String, Object> fileMap = new HashMap<String, Object>();
                            fileMap.put("fileUrl", firlUrlArr[i]);
                            fileMap.put("fileName", fileNameArr[i]);
                            list.add(fileMap);
                        }
                    } else {
                        for (int i = 0; i < firlUrlArr.length; i++) {
                            HashMap<String, Object> fileMap = new HashMap<String, Object>();
                            fileMap.put("fileUrl", firlUrlArr[i]);
                            fileMap.put("fileName", "录播课文件");
                            list.add(fileMap);
                        }
                    }
                }
            }
        }
        return list;
    }
}
