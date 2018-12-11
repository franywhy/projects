package io.renren.modules.job.service;

import java.util.Map;

/**
 * Created by longduyuan on 2018/11/22 0022.
 */
public interface TopicsService {

    void save(Map<String,Object> topicsGensee, String classplanLiveId, String productName);
}
