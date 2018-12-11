package com.hq.learningapi.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hq.learningapi.entity.TodayLearningEntity;

/**
 * Created by DL on 2018/1/12.
 */
@Repository
public interface TodayLearningDao {
	@Autowired(required=true)
    List<TodayLearningEntity> getColdStartingList();
	
	@Autowired(required=true)
    TodayLearningEntity getLatestColdStarting();
}
