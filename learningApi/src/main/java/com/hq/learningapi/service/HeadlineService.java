package com.hq.learningapi.service;

import com.hq.learningapi.pojo.HeadlinePOJO;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/28 0028.
 */
public interface HeadlineService {

    List<HeadlinePOJO> queryPojoList(Map<String, Object> map);

    HeadlinePOJO queryPojoObject(Long headlineId);

    void addCommentNumber(Long headlineId);

    HeadlinePOJO queryPojoObjectForNumber(Long headlineId);

    List<Map<String,Object>> queryMapList(Map<String, Object> map, String token);
}
