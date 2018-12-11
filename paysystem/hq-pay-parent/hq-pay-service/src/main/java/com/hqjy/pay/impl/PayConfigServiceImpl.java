package com.hqjy.pay.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hqjy.pay.PayConfig;
import com.hqjy.pay.PayConfigService;
import com.hqjy.pay.mapper.PayConfigDao;
@Service
public class PayConfigServiceImpl implements PayConfigService {
    @Autowired
    private PayConfigDao payConfigDao;
	@Override
	public PayConfig find() {
		return payConfigDao.find();
	}

}
