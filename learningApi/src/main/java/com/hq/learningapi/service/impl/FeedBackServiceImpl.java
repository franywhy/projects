package com.hq.learningapi.service.impl;

import com.hq.learningapi.dao.FeedBackDao;
import com.hq.learningapi.entity.AppFeedBackEntity;
import com.hq.learningapi.service.FeedBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DL on 2018/1/20.
 */
@Service("feedBackService")
public class FeedBackServiceImpl implements FeedBackService {

    @Autowired
    private FeedBackDao feedBackDao;
    @Override
    public void saveFeedBack(AppFeedBackEntity entity) {
        feedBackDao.saveFeedBack(entity);
    }
}
