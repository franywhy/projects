package com.hq.bi.search.mapper;

import com.hq.bi.search.entity.ClassExamQualityEntity;
import com.hq.bi.search.util.PageQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Description:
 * Autor: wl
 * Date: 2018-12-15
 */
@Mapper
public interface ClassExamQualityMapper {

    List<ClassExamQualityEntity> getClassExamQualityList(PageQuery pageQuery);
    ;

}
