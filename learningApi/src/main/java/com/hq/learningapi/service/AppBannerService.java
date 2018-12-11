package com.hq.learningapi.service;

import com.hq.learningapi.entity.AppBannerEntity;

import java.util.List;

/**
 * 移动端banner档案
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-04-27 14:20:26
 */
public interface AppBannerService {

	List<AppBannerEntity> queryList(Long levelId, Long professionId, String schoolId);

		
}
