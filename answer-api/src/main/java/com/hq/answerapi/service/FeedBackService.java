package com.hq.answerapi.service;


import com.hq.answerapi.entity.AppFeedBackEntity;

/**
 * Created by DL on 2018/1/20.
 */
public interface FeedBackService {
    //保存反馈内容
    void saveFeedBack(AppFeedBackEntity entity);
}
