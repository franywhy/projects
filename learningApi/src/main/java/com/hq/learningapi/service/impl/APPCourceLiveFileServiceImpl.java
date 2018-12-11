package com.hq.learningapi.service.impl;


import com.hq.learningapi.dao.APPCourseLiveFileDao;
import com.hq.learningapi.service.APPCourseLiveFileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("appCourseLiveFileService")
public class APPCourceLiveFileServiceImpl implements APPCourseLiveFileService {

    @Autowired
    private APPCourseLiveFileDao appCourseLiveFileDao;

    @Override
    public List<Map<String, Object>> getFileList(String classplanLiveId, int fileType) {
        List<Map<String, Object>> list = new ArrayList<>();
        //文件类型为上期预习的资料
        if (fileType == 1) {
            Map<String, Object> fileList = appCourseLiveFileDao.getReviewFileList(classplanLiveId);
            if (null != fileList) {
                String reviewFileUrl = (String) fileList.get("reviewUrl");
                String reviewFileName = (String) fileList.get("reviewName");
                if (StringUtils.isNotBlank(reviewFileName) && StringUtils.isNotBlank(reviewFileUrl)) {
                    String[] firlUrlArr = reviewFileUrl.split(",");
                    String[] fileNameArr = reviewFileName.split(",");
                    for (int i = 0; i < firlUrlArr.length; i++) {
                        HashMap<String, Object> fileMap = new HashMap<String, Object>();
                        fileMap.put("fileUrl", firlUrlArr[i]);
                        fileMap.put("fileName", fileNameArr[i]);
                        list.add(fileMap);
                    }
                }
            }
        }
        //文件类型为本期复习的资料
        if (fileType == 2) {
            Map<String, Object> fileList = appCourseLiveFileDao.getPrepareFileList(classplanLiveId);
            if (null != fileList) {
                String prepareFileUrl = (String) fileList.get("prepareUrl");
                String prepareFileName = (String) fileList.get("prepareName");
                if (StringUtils.isNotBlank(prepareFileUrl) && StringUtils.isNotBlank(prepareFileName)) {
                    String[] firlUrlArr = prepareFileUrl.split(",");
                    String[] fileNameArr = prepareFileName.split(",");
                    for (int i = 0; i < firlUrlArr.length; i++) {
                        HashMap<String, Object> fileMap = new HashMap<String, Object>();
                        fileMap.put("fileUrl", firlUrlArr[i]);
                        fileMap.put("fileName", fileNameArr[i]);
                        list.add(fileMap);
                    }
                }
            }
        }
        //文件类型为课堂资料（自适应）的资料
        if (fileType == 3) {
            Map<String, Object> fileList = appCourseLiveFileDao.getCoursewareFileList(classplanLiveId);
            if (null != fileList) {
                String coursewareFileUrl = (String) fileList.get("coursewareUrl");
                String coursewareFileName = (String) fileList.get("coursewareName");
                if (StringUtils.isNotBlank(coursewareFileUrl) && StringUtils.isNotBlank(coursewareFileName)) {
                    String[] firlUrlArr = coursewareFileUrl.split(",");
                    String[] fileNameArr = coursewareFileName.split(",");
                    for (int i = 0; i < firlUrlArr.length; i++) {
                        HashMap<String, Object> fileMap = new HashMap<String, Object>();
                        fileMap.put("fileUrl", firlUrlArr[i]);
                        fileMap.put("fileName", fileNameArr[i]);
                        list.add(fileMap);
                    }
                }
            }
        }
        //文件类型课堂资（非自适应）的资料
        if (fileType == 4) {
            Map<String, Object> fileList = appCourseLiveFileDao.getFileList(classplanLiveId);
            if (null != fileList) {
                String fileUrl = (String) fileList.get("fileUrl");
                String fileName = (String) fileList.get("fileName");
                if ((StringUtils.isNotBlank(fileUrl) && StringUtils.isNotBlank(fileName))) {
                    String[] firlUrlArr = fileUrl.split(",");
                    String[] fileNameArr = fileName.split(",");
                    for (int i = 0; i < firlUrlArr.length; i++) {
                        HashMap<String, Object> fileMap = new HashMap<String, Object>();
                        fileMap.put("fileUrl", firlUrlArr[i]);
                        fileMap.put("fileName", fileNameArr[i]);
                        list.add(fileMap);
                    }
                }
            }
        }
        return list;
    }
}
