package com.school.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.school.dao.SysProductDao;
import com.school.entity.SysProductEntity;
import com.school.service.ProductService;

@Service("productService")
public class ProductServiceImpl implements ProductService{
	@Autowired
	private SysProductDao sysProductDao;
	
	@Override
	public SysProductEntity queryByclassplanLiveId(String classplanLiveId) {
		return this.sysProductDao.queryByclassplanLiveId(classplanLiveId);
	}

}
