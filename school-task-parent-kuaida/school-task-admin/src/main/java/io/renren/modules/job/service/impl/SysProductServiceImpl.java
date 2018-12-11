package io.renren.modules.job.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.modules.job.dao.SysProductDao;
import io.renren.modules.job.entity.SysProductEntity;
import io.renren.modules.job.service.SysProductService;
import io.renren.modules.job.utils.Constant;

import java.util.List;
import java.util.Map;
import java.util.HashMap;



@Service("sysProductService")
public class SysProductServiceImpl implements SysProductService {
	@Autowired
	private SysProductDao sysProductDao;
	
	@Override
	public SysProductEntity queryObject(Map<String, Object> map){
		return sysProductDao.queryObject(map);
	}

	@Override
	public Float queryCoefficient(Long productId) {
		return this.sysProductDao.queryCoefficient(productId);
	}

    @Override
    public Float queryRecordEfficient(Long productId) {
        return this.sysProductDao.queryRecordEfficient(productId);
    }


}
