package com.izhubo.rest.persistent;

/**
 *
 * kgs server.
 * 产生下一个id
 * 根据id定位存储服务器
 *
 *
 *
 * @author: wubinjie@ak.cc
 */
public interface KGS {
    int nextId();

    Integer nextId(String nameSpace);
}
