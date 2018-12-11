package com.hq.learningapi.service.impl;

import com.hq.learningapi.dao.SysProductDao;
import com.hq.learningapi.service.SysProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Administrator on 2018/3/29 0029.
 */
@Service("sysProductService")
public class SysProductServiceImpl implements SysProductService {

    @Autowired
    private SysProductDao sysProductDao;

    @Override
    public Map<String, Object> queryByProductId(Long productId) {
        return sysProductDao.queryByProductId(productId);
    }
}
