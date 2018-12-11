package com.hq.learningcenter.school.service.impl;

import com.hq.learningcenter.school.service.LcBusinessMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import com.hq.learningcenter.school.dao.LcBusinessMenuDao;
import com.hq.learningcenter.school.entity.LcBusinessMenuEntity;


@Service("lcBusinessMenuService")
public class LcBusinessMenuServiceImpl implements LcBusinessMenuService {
	@Autowired
	private LcBusinessMenuDao lcBusinessMenuDao;

	@Override
	public LcBusinessMenuEntity queryByBusinessId(Map<String, Object> map) {
		return lcBusinessMenuDao.queryByBusinessId(map);
	}
	
	
}
