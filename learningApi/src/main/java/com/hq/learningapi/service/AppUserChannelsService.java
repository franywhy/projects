package com.hq.learningapi.service;

import java.util.List;

/**
 * Created by LiuHai on 2017/12/29
 */

public interface AppUserChannelsService {

	List<String> queryChannelIdListByUserId(Long userId);


		
}
