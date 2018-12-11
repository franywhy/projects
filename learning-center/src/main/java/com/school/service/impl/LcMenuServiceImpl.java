package com.school.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.school.dao.LcMenuDao;
import com.school.pojo.LcMenuPOJO;
import com.school.service.LcMenuService;
@Service("lcMenuService")
public class LcMenuServiceImpl implements LcMenuService {
	
	@Autowired
	private LcMenuDao lcMenuDao;
	
	@Override
	public List<LcMenuPOJO> queryLcMenu(String businessId) {
		return lcMenuDao.queryLcMenu(businessId);
	}

    @Override
    public List<LcMenuPOJO> queryLcUsermenu(Map<String, Object> map) {
        return lcMenuDao.queryLcUsermenu(map);
    }

}
