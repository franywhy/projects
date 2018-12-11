package com.hqjy.msg.interfaces;

import java.util.List;

/**
 *
 * 聚合操作
 *
 * Created by Brandon on 2016/7/21.
 */
public interface Aggregator<T> {

    /**
     * 每一组的聚合操作
     *
     * @param key 组别标识key
     * @param values 属于该组的元素集合
     * @return
     */
    Object aggregate(Object key , List<T> values);
}
