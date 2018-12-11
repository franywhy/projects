package com.izhubo.rest.web.data;

import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * 数据交换协议
 *
 * date: 12-8-20 下午3:12
 *
 * @author: wubinjie@ak.cc
 */
public interface Map2View {


    /**
     *
     * @param data
     * @return
     */
    ModelAndView exchange(Map data);
}
