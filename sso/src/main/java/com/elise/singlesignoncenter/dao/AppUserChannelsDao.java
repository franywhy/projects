package com.elise.singlesignoncenter.dao;

import org.springframework.stereotype.Repository;

import com.elise.singlesignoncenter.entity.AppUserChannelsEntity;

/**
 * 
 * @author Created by LiuHai 2018/02/05
 *
 */
@Repository
public interface AppUserChannelsDao {

	int save(AppUserChannelsEntity entity);

}
