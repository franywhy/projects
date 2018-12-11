package com.elise.userinfocenter.service.impl;

import com.elise.userinfocenter.dao.CityDao;
import com.elise.userinfocenter.entity.CityEntity;
import com.elise.userinfocenter.pojo.CityPOJO;
import com.elise.userinfocenter.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by DL on 2017/12/28.
 */
@Service("cityService")
public class CityServiceImpl implements CityService {

    @Autowired
    private CityDao cityDao;
    @Override
    public Map<String,Object> getCityFileUrl() {
        return cityDao.getCityFileUrl();
    }

    @Override
    public Map<String, Object> getCity(Long userId) {
        return cityDao.getCity(userId);
    }

    @Override
    public boolean isExistCity(Long userId) {
        return cityDao.isExistCity(userId) > 0;
    }

    @Override
    public void update(Long userId, String cityCode, String cityName) {
        Map<String,Object> map = new HashMap();
        map.put("userId",userId);
        map.put("cityCode",cityCode);
        map.put("cityName",cityName);
        cityDao.update(map);
    }

    @Override
    public void save(Long userId, String cityCode, String cityName) {
        CityEntity entity = new CityEntity();
        entity.setUserId(userId);
        entity.setCityCode(cityCode);
        entity.setCityName(cityName);
        cityDao.save(entity);
    }

    @Override
    public Date getMaxTsTime() {
        return cityDao.getMaxTsTime();
    }

    @Override
    public List<CityPOJO> getCityList() {
         List<CityPOJO> cityPOJOList = cityDao.getParentCityList();
         if (cityPOJOList.size() >0 ){
             for (CityPOJO cityPOJO : cityPOJOList) {
                 cityPOJO.setChildrenCityList(cityDao.getChildrenCityList(cityPOJO.getCityId()));
             }
         }
        return cityPOJOList;
    }
}
