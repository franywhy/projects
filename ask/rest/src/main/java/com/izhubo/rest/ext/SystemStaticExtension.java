package com.izhubo.rest.ext;

/**
 *        扩展 system -> groovy
 *        date: 13-7-29 下午1:51
 *
 * @author: wubinjie@ak.cc
 */
public final class SystemStaticExtension {

    public static long unixTime(System selfType) {
        return System.currentTimeMillis()/1000;
    }

    public static long currentSeconds(System selfType) {
        return System.currentTimeMillis()/1000;
    }

}
