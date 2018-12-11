package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.SexDao;
import io.renren.entity.SexEntity;
import io.renren.service.SexService;
import io.renren.utils.Constant;



@Service("sexService")
public class SexServiceImpl implements SexService {
	@Autowired
	private SexDao sexDao;
	
	@Override
	public List<SexEntity> findSexList() {
		return sexDao.findSexList();
	}
}
