package com.elise.userinfocenter.service;

import com.elise.userinfocenter.pojo.CityPOJO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by DL on 2017/12/28.
 */
public interface CityService {

    //获取城市列表文件的服务地址
    Map<String,Object> getCityFileUrl();
    //获取用户的城市
    Map<String,Object> getCity(Long userId);
    //查询用户是否有城市记录
    boolean isExistCity(Long userId);
    //更新用户城市
    void update(Long userId, String cityCode, String cityName);
    //保存用户城市
    void save(Long userId, String cityCode, String cityName);

    //获取最大ts时间
    Date getMaxTsTime();

    List<CityPOJO> getCityList();
}
