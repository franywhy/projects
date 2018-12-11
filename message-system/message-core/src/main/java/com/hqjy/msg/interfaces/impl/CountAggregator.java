package com.hqjy.msg.interfaces.impl;

import com.hqjy.msg.interfaces.Aggregator;

import java.util.List;

/**
 *
 * 计数聚合操作
 *
 * Created by Brandon on 2016/7/21.
 */
public class CountAggregator<T> implements Aggregator<T> {

    @Override
    public Object aggregate(Object key, List<T> values) {
        return values.size();
    }
}
