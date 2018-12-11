package com.school.accountant.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.school.accountant.dao.NewsAdvertMapper;
import com.school.accountant.entity.NewsAdvert;
import com.school.accountant.entity.NewsAdvertExample;
import com.school.accountant.service.NewsAdvertService;

@Service
public class NewsAdvertServiceImpl implements NewsAdvertService {

	@Autowired
	private NewsAdvertMapper newsAdvertMapper; 

	@Override
	public List<NewsAdvert> bannerList(int advertposition) {
		NewsAdvertExample example = new NewsAdvertExample();
		example.createCriteria().andAdvertpositionEqualTo(advertposition).addCriterion("(dr = 0 or dr is null)");
		example.setOrderByClause("code ASC");
		List<NewsAdvert> banners = newsAdvertMapper.selectByExample(example);
		return banners;
	}
	
}
