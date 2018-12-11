package io.renren.dao;

import io.renren.entity.AppCity;

import java.util.List;
import java.util.Map;

public interface AppCityDao {

    List<AppCity> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

}