package com.hq.answerapi.dao;

import com.hq.answerapi.entity.VersionEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Glenn on 2017/5/3 0003.
 */
@Repository
public interface VersionDao {

     List<VersionEntity> queryList(Map<String, Object> map);

}
