package io.renren.dao.manage;

import io.renren.entity.manage.AppCommodity;
import io.renren.entity.manage.AppCommodityExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppCommodityMapper {
    int countByExample(AppCommodityExample example);

    int deleteByExample(AppCommodityExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AppCommodity record);

    int insertSelective(AppCommodity record);

    AppCommodity selectByExampleFetchOne(AppCommodityExample example);

    List<AppCommodity> selectByExample(AppCommodityExample example);

    AppCommodity selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AppCommodity record, @Param("example") AppCommodityExample example);

    int updateByExample(@Param("record") AppCommodity record, @Param("example") AppCommodityExample example);

    int updateByPrimaryKeySelective(AppCommodity record);

    int updateByPrimaryKey(AppCommodity record);
}