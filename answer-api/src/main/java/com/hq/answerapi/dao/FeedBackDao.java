package com.hq.answerapi.dao;

import com.hq.answerapi.entity.AppFeedBackEntity;
import org.springframework.stereotype.Repository;

/**
 * Created by DL on 2018/1/20.
 */
@Repository
public interface FeedBackDao {

    void saveFeedBack(AppFeedBackEntity entity);
}
