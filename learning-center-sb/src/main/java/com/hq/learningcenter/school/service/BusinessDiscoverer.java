package com.hq.learningcenter.school.service;

import javax.servlet.http.HttpServletRequest;

import com.hq.learningcenter.school.exception.SchoolNotFoundException;

/**
 * 根据页面请求来发现网校ID。
 * @author XingNing OU
 */
public interface BusinessDiscoverer {

    /**
     * 根据请求获取网校ID
     * @param request HTTP请求
     * @return 当前HTTP请求的网校ID
     * @throws SchoolNotFoundException 如果获取不到网校ID则抛出SchoolNotFoundException
     */
    String getBusinessId(HttpServletRequest request) throws SchoolNotFoundException;

}
