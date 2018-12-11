package com.school.dao;

import com.school.entity.TeachEvaluate;
import com.school.entity.TeachEvaluateExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TeachEvaluateMapper {
    int countByExample(TeachEvaluateExample example);

    int deleteByExample(TeachEvaluateExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TeachEvaluate record);

    int insertSelective(TeachEvaluate record);

    List<TeachEvaluate> selectByExample(TeachEvaluateExample example);

    TeachEvaluate selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TeachEvaluate record, @Param("example") TeachEvaluateExample example);

    int updateByExample(@Param("record") TeachEvaluate record, @Param("example") TeachEvaluateExample example);

    int updateByPrimaryKeySelective(TeachEvaluate record);

    int updateByPrimaryKey(TeachEvaluate record);
}