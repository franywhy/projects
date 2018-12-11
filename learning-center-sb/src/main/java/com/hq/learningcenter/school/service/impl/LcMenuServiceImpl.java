package com.hq.learningcenter.school.service.impl;

import java.util.List;
import java.util.Map;

import com.hq.learningcenter.school.pojo.LcMenuPOJO;
import com.hq.learningcenter.school.service.LcMenuService;
import com.hq.learningcenter.school.dao.LcMenuDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
