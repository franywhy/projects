package com.elise.singlesignoncenter.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Glenn on 2017/4/26 0026.
 */

@Repository
public interface SysBusinessDao {

    Integer checkValidationOfBusiness(@Param("businessId") String businessId);

    String querySmsSign(@Param("businessId") String businessId);

    Integer queryType(@Param("businessId") String businessId);
    /**
     * 查询业务线主键列表
     * @return
     */
    List<String> queryBusinessList();

}
