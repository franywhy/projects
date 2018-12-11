package com.bluewhale.common.anno;

import org.springframework.stereotype.Controller;
import java.lang.annotation.*;

/**
 * date: 13-6-4 下午5:25
 *
 * @author: wubinjie@ak.cc
 */
@java.lang.annotation.Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE})
public @interface NotAuth {
    Class[] value = {Controller.class};
}
