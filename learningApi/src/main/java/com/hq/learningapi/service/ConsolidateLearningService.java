package com.hq.learningapi.service;

import com.hq.learningapi.entity.KnowledgeFileEntity;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by liangdongbin on 2018/1/17 0017.
 */
public interface ConsolidateLearningService {

   
    public String  process(HttpServletRequest request);

    public int saveBath(List<KnowledgeFileEntity> knowledgeFiles);

    public String process(Long  userId, Long phaseId, String multiClassesId,Long courseId, Long trainType);
    
    public Long getUserIdByToken(String token,HttpServletRequest request);

    public List getKnowledgeFilesByMultiClassesId(String token,String multiClassesId,String examUUID,HttpServletRequest request);
    
    public String saveKnowledgePlayLog(HttpServletRequest request);

    public void cleanOldRecord(Long userId ,String multiClassesId,String examUUID);
    
}
