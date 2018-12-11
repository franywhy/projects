package io.renren.service;

import io.renren.entity.AppCity;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/6 0006.
 */
public interface AppCityService {

    List<AppCity> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

}
