package com.hq.learningapi.dao;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by DL on 2018/9/18.
 */
@Repository
public interface SysConfigDao {

    String queryByKey(String key);

    List<String> getPhurchaseNotes(String note);
}
