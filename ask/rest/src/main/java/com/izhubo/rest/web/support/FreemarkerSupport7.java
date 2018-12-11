package com.izhubo.rest.web.support;

import java.lang.reflect.Method;

import static com.izhubo.rest.web.support.ControllerSupport7.*;

/**
 * TODO Comment here.
 * date: 13-6-9 下午3:53
 *
 * @author: wubinjie@ak.cc
 */
public class FreemarkerSupport7 extends FreemarkerSupport {

    protected MethodExec zeroArg(Method method,Object self){
        return staticZeroArg(method,self);
    }
    protected MethodExec twoArg(Method method,Object self){
        return staticTwoArg(method, self);
    }
    protected MethodExec requestArg(Method method,Object self){
        return staticRequestArg(method, self);
    }
    protected MethodExec responseArg(Method method,Object self){
        return staticResponseArg(method, self);
    }
}
