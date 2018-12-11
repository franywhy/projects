package com.hq.learningapi.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hq.learningapi.pojo.AppSchoolPOJO;

public interface AppSchoolLastFourDao {

	List<AppSchoolPOJO> querySchoolList(@Param("businessId")String businessId, @Param("longitude")double longitude, @Param("latitude")double latitude);

}
