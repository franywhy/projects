package com.hq.learningcenter.school.service.impl;

import com.hq.learningcenter.school.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hq.learningcenter.school.dao.SysProductDao;
import com.hq.learningcenter.school.entity.SysProductEntity;

@Service("productService")
public class ProductServiceImpl implements ProductService {
	@Autowired
	private SysProductDao sysProductDao;
	
	@Override
	public SysProductEntity queryByclassplanLiveId(String classplanLiveId) {
		return this.sysProductDao.queryByclassplanLiveId(classplanLiveId);
	}

}
