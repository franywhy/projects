package com.school.service;

/**
 * Created by DL on 2018/11/22.
 */
public interface SysConfigService {
    /**
     * 根据key，获取配置的value值
     *
     * @param key           key
     * @param defaultValue  缺省值
     */
    public String getValue(String key, String defaultValue);
}
