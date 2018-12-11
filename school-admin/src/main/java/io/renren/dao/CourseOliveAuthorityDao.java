package io.renren.dao;

import io.renren.entity.CourseOliveAuthority;

import java.util.List;
import java.util.Map;

public interface CourseOliveAuthorityDao extends BaseMDao<CourseOliveAuthority>{

    List<CourseOliveAuthority> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    CourseOliveAuthority queryObjectByAuthorityId(Integer authorityId);

}