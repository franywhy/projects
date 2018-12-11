package com.hq.learningapi.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by LiuHai on 2017/12/29
 */

@Repository
public interface AppUserChannelsDao {

	List<String> queryChannelIdListByUserId(@Param("userId")Long userId);



}
