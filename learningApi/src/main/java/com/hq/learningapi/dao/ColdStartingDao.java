package com.hq.learningapi.dao;

import com.hq.learningapi.entity.ColdStartingEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by DL on 2018/1/2.
 */
@Repository
public interface ColdStartingDao {

    List<ColdStartingEntity> getColdStartingList();

    ColdStartingEntity getLatestColdStarting();
}
