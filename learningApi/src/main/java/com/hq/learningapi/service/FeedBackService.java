package com.hq.learningapi.service;

import com.hq.learningapi.entity.AppFeedBackEntity;

/**
 * Created by DL on 2018/1/20.
 */
public interface FeedBackService {
    //保存反馈内容
    void saveFeedBack(AppFeedBackEntity entity);
}
