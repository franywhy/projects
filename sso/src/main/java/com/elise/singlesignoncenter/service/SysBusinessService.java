package com.elise.singlesignoncenter.service;

import org.apache.ibatis.annotations.Param;

public interface SysBusinessService {

    Integer checkValidationOfBusiness(@Param("businessId") String businessId);

    String querySmsSign(@Param("businessId") String businessId);

    Integer queryType(@Param("businessId") String businessId);
}
