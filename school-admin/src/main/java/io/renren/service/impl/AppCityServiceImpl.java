package io.renren.service.impl;

import io.renren.dao.AppCityDao;
import io.renren.entity.AppCity;
import io.renren.service.AppCityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/6 0006.
 */
@Service
public class AppCityServiceImpl implements AppCityService {

    @Autowired
    private AppCityDao appCityDao;

    @Override
    public List<AppCity> queryList(Map<String, Object> map) {
        return appCityDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return appCityDao.queryTotal(map);
    }
}
