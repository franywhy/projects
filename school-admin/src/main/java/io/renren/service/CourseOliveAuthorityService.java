package io.renren.service;

import io.renren.entity.CourseOliveAuthority;

import java.util.List;
import java.util.Map;

/**
 * 公开课权限Service
 */
public interface CourseOliveAuthorityService {

    List<CourseOliveAuthority> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);


}
