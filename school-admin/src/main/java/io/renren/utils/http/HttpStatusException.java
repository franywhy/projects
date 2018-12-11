package io.renren.utils.http;

import java.io.IOException;

/**
 *
 * Http 异常
 *
 * date: 12-11-16 下午2:29
 *
 * @author: wubinjie@ak.cc
 */
public class HttpStatusException extends IOException {

    int code;

    public HttpStatusException() {
    }

    public HttpStatusException(int code, String message) {
        super(message);
        this.code =code;
    }

    public int getCode(){
        return code;
    }
}
