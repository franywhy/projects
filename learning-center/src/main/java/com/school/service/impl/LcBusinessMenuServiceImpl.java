package com.school.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.school.dao.LcBusinessMenuDao;
import com.school.entity.LcBusinessMenuEntity;
import com.school.service.LcBusinessMenuService;



@Service("lcBusinessMenuService")
public class LcBusinessMenuServiceImpl implements LcBusinessMenuService {
	@Autowired
	private LcBusinessMenuDao lcBusinessMenuDao;

	@Override
	public LcBusinessMenuEntity queryByBusinessId(Map<String, Object> map) {
		return lcBusinessMenuDao.queryByBusinessId(map);
	}
	
	
}
