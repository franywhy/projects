package com.hq.learningapi.dao;

import com.hq.learningapi.entity.AppBannerEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Glenn on 2017/4/26 0026.
 */
@Repository
public interface AppBannerDao {

	List<AppBannerEntity> queryList(Map<String, Object> map);

}
